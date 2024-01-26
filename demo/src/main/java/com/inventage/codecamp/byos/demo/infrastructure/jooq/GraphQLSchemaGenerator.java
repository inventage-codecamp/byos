package com.inventage.codecamp.byos.demo.infrastructure.jooq;

import graphql.scalars.ExtendedScalars;
import graphql.schema.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jooq.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.time.Year;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static graphql.Scalars.*;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;

@ApplicationScoped
public class GraphQLSchemaGenerator {



    @Inject
    DSLContext jooq;

    public GraphQLSchema createSchema() {
        Meta meta = jooq.meta();
        Catalog catalog = meta.getCatalogs().get(0);
        Schema publicSchema = catalog.getSchema("public");
        GraphQLSchema schema = from(publicSchema);

        return schema;
    }

    public GraphQLSchema from(Schema schema) {
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

    public GraphQLObjectType from(String tableName, Table table) {
        GraphQLObjectType.Builder typeBuilder = newObject().name(tableName);
        Arrays.stream(table.fields()).forEach(field -> typeBuilder.field(from(field, table.getReferences())));
        return typeBuilder.build();
    }

    private GraphQLFieldDefinition.Builder from(Field<?> field, List<ForeignKey> foreignKeys) {
        final String fieldName = replaceSpacesWithUnderscores(field.getName());

        final List<Key> fks = foreignKeys
            .stream()
            .filter(e -> e.getFields().contains(field))
            .map(e -> e.getKey())
            .collect(Collectors.toList());
        System.out.println(fks);

        // assuming there is at most one foreign key with this fields name
        if (!fks.isEmpty()){
            if (fks.size() > 1) {
                System.out.println("ASSUMPTION INVALID");
            }
            
            final String tableName = fks.get(0).getTable().getName();
            final String capitalizedTableName = capitalize(tableName);
            
            System.out.println(fks);
            System.out.println(capitalizedTableName);

            return newFieldDefinition()
                    .name(tableName)
                    .type(GraphQLTypeReference.typeRef(capitalizedTableName));
        } else {
            return newFieldDefinition()
                    .name(fieldName)
                    .type(from(fieldName, field.getDataType()));
        }
    }

    private GraphQLOutputType from(String fieldName, DataType datatype) {
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

        if (!datatype.nullable()) {
            targetType = GraphQLNonNull.nonNull(targetType);
        }

        return targetType;
    }

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    
    private String replaceSpacesWithUnderscores(String str) {
    return str.replace(" ", "_"); // TODO is this smart?
    }

}
