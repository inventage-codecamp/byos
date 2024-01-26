package com.inventage.codecamp.byos.demo.infrastructure.psql;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.schema.Column;

import java.util.Map;

public class ExpressionVisitor extends ExpressionVisitorAdapter {

    private final Map<String, ColumnDefinition> viewDefinition;
    private final Alias alias;
    private final String schemaName;

    public ExpressionVisitor(Map<String, ColumnDefinition> viewDefinition, Alias alias, String schemaName) {
        this.viewDefinition = viewDefinition;
        this.alias = alias;
        this.schemaName = schemaName;
    }

    @Override
    public void visit(Column column) {
        if (column.getTable() != null) {
            viewDefinition.put(alias == null ? column.getColumnName() : alias.getName(), new ColumnDefinition(schemaName, column.getTable().getName(), column.getColumnName()));
        }
    }
}
