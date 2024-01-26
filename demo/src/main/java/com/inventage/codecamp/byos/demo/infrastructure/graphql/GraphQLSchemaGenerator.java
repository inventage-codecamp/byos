package com.inventage.codecamp.byos.demo.infrastructure.graphql;

import com.inventage.codecamp.byos.demo.infrastructure.psql.ColumnDefinition;
import com.inventage.codecamp.byos.demo.infrastructure.psql.ExpressionVisitor;
import com.inventage.codecamp.byos.demo.infrastructure.psql.SelectItemVisitor;
import graphql.scalars.ExtendedScalars;
import graphql.schema.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.create.view.CreateView;
import net.sf.jsqlparser.statement.select.*;
import org.jooq.*;
import org.jooq.util.xml.jaxb.InformationSchema;
import org.jooq.util.xml.jaxb.View;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.sql.Date;
import java.time.OffsetDateTime;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

import static graphql.Scalars.*;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;

@ApplicationScoped
public class GraphQLSchemaGenerator {

    @Inject
    DSLContext jooq;

    public GraphQLSchema createSchema(String schemaName) {
        Meta meta = jooq.meta();
        Catalog catalog = meta.getCatalogs().get(0);
        Schema schema = catalog.getSchema(schemaName);
        return from(schema);
    }

    private Map<String, ColumnDefinition> getViewDefinition(Table table) {
        System.out.println("getViewDefinition: " +table.getName());
        InformationSchema informationSchema = jooq.informationSchema(table.getCatalog());
        List<View> views = informationSchema.getViews();
        View view = views.stream().filter(v -> v.getTableName().equals(table.getName())).findFirst().orElseThrow();
        String memberViewDefinition = view.getViewDefinition();
        try {
            CreateView createViewStatement = (CreateView) CCJSqlParserUtil.parse(memberViewDefinition);
            net.sf.jsqlparser.schema.Table fromTable = findFromItem(((PlainSelect) createViewStatement.getSelect().getSelectBody()).getFromItem());
            Map<String, ColumnDefinition> viewDefinition = new HashMap<>();
            if (fromTable != null) {
                List<SelectItem> selectItems = ((PlainSelect) createViewStatement.getSelect().getSelectBody()).getSelectItems();
                selectItems.stream().forEach(selectItem -> selectItem.accept(new SelectItemVisitor(viewDefinition, fromTable.getSchemaName())));
            }
            return viewDefinition;
        } catch (JSQLParserException e) {
            throw new RuntimeException(e);
        }
    }

    protected net.sf.jsqlparser.schema.Table findFromItem(FromItem fromItem) throws RuntimeException {
        if (fromItem instanceof net.sf.jsqlparser.schema.Table) {
            return (net.sf.jsqlparser.schema.Table) fromItem;
        } else if (fromItem instanceof SubSelect) {
            PlainSelect selectBody = (PlainSelect) ((SubSelect) fromItem).getSelectBody();
            return findFromItem(selectBody.getFromItem());
        } else if (fromItem instanceof ParenthesisFromItem) {
            return findFromItem(((ParenthesisFromItem) fromItem).getFromItem());
        }
        return null;
        //throw new RuntimeException("found no matching fromItem type for: " + fromItem.getClass().getName());
    }


    protected GraphQLSchema from(Schema schema) {
        GraphQLSchema.Builder builder = GraphQLSchema.newSchema();
        GraphQLObjectType.Builder queryBuilder = newObject().name("Query");

        String pluralSuffix = "s"; // TODO hoist

        schema
           .tableStream()
           .forEach(table -> {
               String tableName = table.getName();
               String capitalizedTableName = capitalize(tableName);
               builder.additionalType(from(capitalizedTableName, table));
               queryBuilder.field(newFieldDefinition()
                       .name(capitalizedTableName+pluralSuffix)
                       .type(GraphQLList.list(GraphQLTypeReference.typeRef(capitalizedTableName))));
           });

        builder.query(queryBuilder.build());
        return builder.build();
    }

    protected GraphQLObjectType from(String tableName, Table table) {
        System.out.println(String.format("Processing table '%s'", tableName));
        GraphQLObjectType.Builder typeBuilder = newObject().name(tableName);
        List<ForeignKey> references = getReferences(table);
        Arrays.stream(table.fields()).forEach(field -> typeBuilder.field(from(field, references)));
        return typeBuilder.build();
    }
    
    protected List<ForeignKey> getReferences(Table table) {
        TableOptions.TableType tableType = table.getTableType();
        switch (tableType) {
            case TABLE:
                return table.getReferences();
            case VIEW: {
                Map<String, ColumnDefinition> viewDefinition = getViewDefinition(table);
                List<ForeignKey> collect = viewDefinition.entrySet().stream().map(entry -> {
                    Schema schema = table.getCatalog().getSchema(entry.getValue().schemaName());
                    if (schema == null) {
                        schema = table.getSchema();
                    }
                    Table<?> underlyingTable = schema.getTable(entry.getValue().tableName());
                    return underlyingTable.getReferences();
                }).flatMap(list -> list.stream()).collect(Collectors.toList());
                return collect;
            }
            default:
                throw new UnsupportedOperationException(String.format("Unable to process table type '%s'", tableType));
        }
    }

    protected GraphQLFieldDefinition.Builder from(Field<?> field, List<ForeignKey> foreignKeys) {
        final String fieldName = replaceSpacesWithUnderscores(field.getName());

        final List<Key> fks = foreignKeys
            .stream()
            .filter(e -> e.getFields().contains(field))
            .map(e -> e.getKey())
            .collect(Collectors.toList());

        // assuming there is at most one foreign key with this fields name
        if (!fks.isEmpty()){
            if (fks.size() > 1) {
                throw new IllegalStateException(String.format("Key '%s' is configured with multiple foreign keys: '%s'", fieldName, fks.toString()));
            }
            
            final String referencedTableName = fks.get(0).getTable().getName();
            final String capitalizedTableName = capitalize(referencedTableName);
            
            return newFieldDefinition()
                    .name(fieldName)
                    .type(checkNullable(field.getDataType(), GraphQLTypeReference.typeRef(capitalizedTableName)));
        } else {
            return newFieldDefinition()
                    .name(fieldName)
                    .type(from(fieldName, field.getDataType()));
        }
    }

    protected GraphQLOutputType from(String fieldName, DataType datatype) {
        Class type = datatype.getType();
        GraphQLOutputType targetType;
        
        // primitive types
        if (type.equals(String.class)) {
            targetType = GraphQLString;
        } else if (type.equals(Boolean.class)) {
            targetType = GraphQLBoolean;
        } else if (type.equals(Integer.class)) {
            targetType = GraphQLInt;
        } else if (type.equals(Float.class)) {
            targetType = GraphQLFloat;
        } else if (type.equals(UUID.class)) {
            targetType = GraphQLID;

        // extended types
        } else if (type.equals(Long.class)) {
            targetType = ExtendedScalars.GraphQLLong;
        } else if (type.equals(Short.class)) {
            targetType = ExtendedScalars.GraphQLShort;
        } else if (type.equals(Byte.class)) {
            targetType = ExtendedScalars.GraphQLByte;
        } else if (type.equals(BigDecimal.class)) {
            targetType = ExtendedScalars.GraphQLBigDecimal;
        } else if (type.equals(BigInteger.class)) {
            targetType = ExtendedScalars.GraphQLBigInteger;

        // special types
        } else if (type.equals(Timestamp.class)) {
            targetType = ExtendedScalars.Time; // TODO is this correct?
        } else if (type.equals(Date.class)) {
            targetType = ExtendedScalars.Date; //
        } else if (type.equals(OffsetDateTime.class)) {
            targetType = ExtendedScalars.DateTime; //
        } else if (type.equals(Year.class)) {
            targetType = GraphQLString; // TODO define custom type
        } else if (type.equals(Object.class)) {
            targetType = ExtendedScalars.Object;

        // arrays of primitive types
        } else if (type.equals(String[].class)) {
            targetType = GraphQLList.list(GraphQLString);
        } else if (type.equals(Boolean[].class)) {
            targetType = GraphQLList.list(GraphQLBoolean);
        } else if (type.equals(Integer[].class)) {
            targetType = GraphQLList.list(GraphQLInt);
        } else if (type.equals(Float[].class)) {
            targetType = GraphQLList.list(GraphQLFloat);
            
        // arrays of extended types
        } else if (type.equals(Long[].class)) {
            targetType = GraphQLList.list(ExtendedScalars.GraphQLLong);
        } else if (type.equals(Short[].class)) {
            targetType = GraphQLList.list(ExtendedScalars.GraphQLShort);
        } else if (type.equals(Byte[].class)) {
            targetType = GraphQLList.list(ExtendedScalars.GraphQLByte);
        } else if (type.equals(byte[].class)) {
            targetType = GraphQLList.list(ExtendedScalars.GraphQLByte);

        } else {
            throw new IllegalArgumentException(String.format("unknown datatype for field '%s': %s", fieldName, type.toString()));
        }

        return checkNullable(datatype, targetType);
    }

    protected GraphQLOutputType checkNullable(DataType datatype, GraphQLOutputType type) {
        if (!datatype.nullable()) {
            return GraphQLNonNull.nonNull(type);
        }
        return type;
    }

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    
    private String replaceSpacesWithUnderscores(String str) {
    return str.replace(" ", "_"); // TODO is this smart?
    }

}
