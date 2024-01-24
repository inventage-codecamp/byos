/*
 * This file is generated by jOOQ.
 */
package db.jooq.generated.routines;


import db.jooq.generated.Public;

import org.jooq.Field;
import org.jooq.Parameter;
import org.jooq.impl.AbstractRoutine;
import org.jooq.impl.Internal;
import org.jooq.impl.SQLDataType;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class InventoryInStock extends AbstractRoutine<Boolean> {

    private static final long serialVersionUID = 1L;

    /**
     * The parameter <code>public.inventory_in_stock.RETURN_VALUE</code>.
     */
    public static final Parameter<Boolean> RETURN_VALUE = Internal.createParameter("RETURN_VALUE", SQLDataType.BOOLEAN, false, false);

    /**
     * The parameter <code>public.inventory_in_stock.p_inventory_id</code>.
     */
    public static final Parameter<Integer> P_INVENTORY_ID = Internal.createParameter("p_inventory_id", SQLDataType.INTEGER, false, false);

    /**
     * Create a new routine call instance
     */
    public InventoryInStock() {
        super("inventory_in_stock", Public.PUBLIC, SQLDataType.BOOLEAN);

        setReturnParameter(RETURN_VALUE);
        addInParameter(P_INVENTORY_ID);
    }

    /**
     * Set the <code>p_inventory_id</code> parameter IN value to the routine
     */
    public void setPInventoryId(Integer value) {
        setValue(P_INVENTORY_ID, value);
    }

    /**
     * Set the <code>p_inventory_id</code> parameter to the function to be used
     * with a {@link org.jooq.Select} statement
     */
    public void setPInventoryId(Field<Integer> field) {
        setField(P_INVENTORY_ID, field);
    }
}
