package com.inventage.codecamp.byos.demo.infrastructure.psql;

import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitorAdapter;

import java.util.Map;

public class SelectItemVisitor extends SelectItemVisitorAdapter {

    private final Map<String, ColumnDefinition> viewDefinition;
    private final String schemaNameOfTable;

    public SelectItemVisitor(Map<String, ColumnDefinition> viewDefinition, String schemaNameOfTable) {
        this.viewDefinition = viewDefinition;
        this.schemaNameOfTable = schemaNameOfTable;
    }

    @Override
    public void visit(SelectExpressionItem item) {
        item.getExpression().accept(new ExpressionVisitor(viewDefinition, item.getAlias(), schemaNameOfTable));
    }
}
