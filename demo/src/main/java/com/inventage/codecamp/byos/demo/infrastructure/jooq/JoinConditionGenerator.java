package com.inventage.codecamp.byos.demo.infrastructure.jooq;

import byos.DatabaseMapper;
import byos.InternalQueryNode;
import db.jooq.generated.Public;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.jooq.impl.QOM;
import org.jooq.impl.TableImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class JoinConditionGenerator implements DatabaseMapper {

    @Inject
    DSLContext jooq;

    Map<String,Table<?>> tables = new HashMap<>();
    
    Map<Rel,ForeignKey> joins = new HashMap<>();

    private String schemaName;

    public JoinConditionGenerator init(String schemaName) {
        this.schemaName = schemaName;
        loadTables();
        loadJoins();
        return this;
    }

    @NotNull
    @Override
    public Table<? extends Record> getTableWithAlias(@NotNull InternalQueryNode.Relation relation) {
        Table table = tables.get(relation.getFieldTypeInfo().getRelationName());
        return table.as(relation.getSqlAlias());
    }
    
    @Nullable
    @Override
    public Condition getConditionForRelationship(@NotNull String relationshipName, @NotNull Table<?> left, @NotNull Table<?> right) {
        ForeignKey foreignKey = joins.entrySet().stream()
                .filter(entry -> lookup(entry.getKey(), relationshipName, left, right))
                .map(entry -> entry.getValue())
                .findFirst()
                .orElse(null);
        TableField o = (TableField) foreignKey.getKey().getFields().get(0);
        Condition condition = left.field((TableField) foreignKey.getFields().get(0)).eq(right.field(o.getName()));
        return condition;
    }

    private boolean lookup(Rel rel, String relationshipName, Table<?> left, Table<?> right) {
        return relationshipName.equals(rel.relationName) &&
                ((QOM.TableAlias)left).$table().equals(rel.left) &&
                ((QOM.TableAlias)right).$table().equals(rel.right);
    }

    private void loadTables() {
        Meta meta = jooq.meta();
        Catalog catalog = meta.getCatalogs().get(0);
        Schema schema = catalog.getSchema(schemaName);
        schema.tableStream()
                .forEach(table -> tables.put(table.getName(), table));
    }

    private void loadJoins() {
        tables.values().stream().forEach(table -> from(table));
    }
    
    private void from(Table table) {
        List<ForeignKey> references = table.getReferences();
        // TODO: 1:n or n:m
        references.stream().forEach(foreignKey -> from(table, foreignKey));
    }

    protected void from(Table left, ForeignKey right) {
        List references = left.getReferences();
        boolean hasInverse = hasInverse(right, references);
        List<TableField> leftFields = right.getFields();
        List<TableField> rightFields = right.getKeyFields();
        if (hasInverse) {
        }
        else {
            Condition condition = left.field((TableField) right.getFields().get(0)).eq(right.getKey().getFields().get(0));
            joins.put(new Rel(((TableField<?, ?>) right.getFields().get(0)).getName(), right.getTable(), right.getKey().getTable()), right);
        }
    }

    private boolean hasInverse(ForeignKey relation, List<ForeignKey> references) {
        return references.stream()
                .filter(ref -> isPair(relation, ref))
                .findFirst().isPresent();
    }

    private boolean isPair(ForeignKey a, ForeignKey b) {
        if (a.getName().equals(b.getName())) {
            return false;
        }
        Table tableA = a.getTable();
        Table tableB = b.getTable();
        boolean match = a.getKey().getTable().equals(tableB) &&
                b.getKey().getTable().equals(tableA);
        return match;
    }

    public class Rel {
        private final String relationName;
        private final Table left;
        private final Table right;

        public Rel(String relationName, Table left, Table right) {
            this.relationName = relationName;
            this.left = left;
            this.right = right;
        }
    }
}
