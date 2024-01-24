/*
 * This file is generated by jOOQ.
 */
package db.jooq.generated.tables.records;


import db.jooq.generated.tables.PaymentP2007_05;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.jooq.Field;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.TableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PaymentP2007_05Record extends TableRecordImpl<PaymentP2007_05Record> implements Record6<Integer, Integer, Integer, Integer, BigDecimal, LocalDateTime> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.payment_p2007_05.payment_id</code>.
     */
    public void setPaymentId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.payment_p2007_05.payment_id</code>.
     */
    public Integer getPaymentId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>public.payment_p2007_05.customer_id</code>.
     */
    public void setCustomerId(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.payment_p2007_05.customer_id</code>.
     */
    public Integer getCustomerId() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>public.payment_p2007_05.staff_id</code>.
     */
    public void setStaffId(Integer value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.payment_p2007_05.staff_id</code>.
     */
    public Integer getStaffId() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>public.payment_p2007_05.rental_id</code>.
     */
    public void setRentalId(Integer value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.payment_p2007_05.rental_id</code>.
     */
    public Integer getRentalId() {
        return (Integer) get(3);
    }

    /**
     * Setter for <code>public.payment_p2007_05.amount</code>.
     */
    public void setAmount(BigDecimal value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.payment_p2007_05.amount</code>.
     */
    public BigDecimal getAmount() {
        return (BigDecimal) get(4);
    }

    /**
     * Setter for <code>public.payment_p2007_05.payment_date</code>.
     */
    public void setPaymentDate(LocalDateTime value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.payment_p2007_05.payment_date</code>.
     */
    public LocalDateTime getPaymentDate() {
        return (LocalDateTime) get(5);
    }

    // -------------------------------------------------------------------------
    // Record6 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row6<Integer, Integer, Integer, Integer, BigDecimal, LocalDateTime> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    @Override
    public Row6<Integer, Integer, Integer, Integer, BigDecimal, LocalDateTime> valuesRow() {
        return (Row6) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return PaymentP2007_05.PAYMENT_P2007_05.PAYMENT_ID;
    }

    @Override
    public Field<Integer> field2() {
        return PaymentP2007_05.PAYMENT_P2007_05.CUSTOMER_ID;
    }

    @Override
    public Field<Integer> field3() {
        return PaymentP2007_05.PAYMENT_P2007_05.STAFF_ID;
    }

    @Override
    public Field<Integer> field4() {
        return PaymentP2007_05.PAYMENT_P2007_05.RENTAL_ID;
    }

    @Override
    public Field<BigDecimal> field5() {
        return PaymentP2007_05.PAYMENT_P2007_05.AMOUNT;
    }

    @Override
    public Field<LocalDateTime> field6() {
        return PaymentP2007_05.PAYMENT_P2007_05.PAYMENT_DATE;
    }

    @Override
    public Integer component1() {
        return getPaymentId();
    }

    @Override
    public Integer component2() {
        return getCustomerId();
    }

    @Override
    public Integer component3() {
        return getStaffId();
    }

    @Override
    public Integer component4() {
        return getRentalId();
    }

    @Override
    public BigDecimal component5() {
        return getAmount();
    }

    @Override
    public LocalDateTime component6() {
        return getPaymentDate();
    }

    @Override
    public Integer value1() {
        return getPaymentId();
    }

    @Override
    public Integer value2() {
        return getCustomerId();
    }

    @Override
    public Integer value3() {
        return getStaffId();
    }

    @Override
    public Integer value4() {
        return getRentalId();
    }

    @Override
    public BigDecimal value5() {
        return getAmount();
    }

    @Override
    public LocalDateTime value6() {
        return getPaymentDate();
    }

    @Override
    public PaymentP2007_05Record value1(Integer value) {
        setPaymentId(value);
        return this;
    }

    @Override
    public PaymentP2007_05Record value2(Integer value) {
        setCustomerId(value);
        return this;
    }

    @Override
    public PaymentP2007_05Record value3(Integer value) {
        setStaffId(value);
        return this;
    }

    @Override
    public PaymentP2007_05Record value4(Integer value) {
        setRentalId(value);
        return this;
    }

    @Override
    public PaymentP2007_05Record value5(BigDecimal value) {
        setAmount(value);
        return this;
    }

    @Override
    public PaymentP2007_05Record value6(LocalDateTime value) {
        setPaymentDate(value);
        return this;
    }

    @Override
    public PaymentP2007_05Record values(Integer value1, Integer value2, Integer value3, Integer value4, BigDecimal value5, LocalDateTime value6) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached PaymentP2007_05Record
     */
    public PaymentP2007_05Record() {
        super(PaymentP2007_05.PAYMENT_P2007_05);
    }

    /**
     * Create a detached, initialised PaymentP2007_05Record
     */
    public PaymentP2007_05Record(Integer paymentId, Integer customerId, Integer staffId, Integer rentalId, BigDecimal amount, LocalDateTime paymentDate) {
        super(PaymentP2007_05.PAYMENT_P2007_05);

        setPaymentId(paymentId);
        setCustomerId(customerId);
        setStaffId(staffId);
        setRentalId(rentalId);
        setAmount(amount);
        setPaymentDate(paymentDate);
    }
}
