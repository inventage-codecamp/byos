/*
 * This file is generated by jOOQ.
 */
package db.jooq.generated;


import db.jooq.generated.tables.Actor;
import db.jooq.generated.tables.ActorInfo;
import db.jooq.generated.tables.Address;
import db.jooq.generated.tables.Category;
import db.jooq.generated.tables.City;
import db.jooq.generated.tables.Country;
import db.jooq.generated.tables.Customer;
import db.jooq.generated.tables.CustomerList;
import db.jooq.generated.tables.Film;
import db.jooq.generated.tables.FilmActor;
import db.jooq.generated.tables.FilmCategory;
import db.jooq.generated.tables.FilmInStock;
import db.jooq.generated.tables.FilmList;
import db.jooq.generated.tables.FilmNotInStock;
import db.jooq.generated.tables.Inventory;
import db.jooq.generated.tables.Language;
import db.jooq.generated.tables.NicerButSlowerFilmList;
import db.jooq.generated.tables.Payment;
import db.jooq.generated.tables.PaymentP2007_01;
import db.jooq.generated.tables.PaymentP2007_02;
import db.jooq.generated.tables.PaymentP2007_03;
import db.jooq.generated.tables.PaymentP2007_04;
import db.jooq.generated.tables.PaymentP2007_05;
import db.jooq.generated.tables.PaymentP2007_06;
import db.jooq.generated.tables.Rental;
import db.jooq.generated.tables.RewardsReport;
import db.jooq.generated.tables.SalesByFilmCategory;
import db.jooq.generated.tables.SalesByStore;
import db.jooq.generated.tables.Staff;
import db.jooq.generated.tables.StaffList;
import db.jooq.generated.tables.Store;
import db.jooq.generated.tables.records.FilmInStockRecord;
import db.jooq.generated.tables.records.FilmNotInStockRecord;
import db.jooq.generated.tables.records.RewardsReportRecord;

import java.math.BigDecimal;

import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Result;


/**
 * Convenience access to all tables in public.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * The table <code>public.actor</code>.
     */
    public static final Actor ACTOR = Actor.ACTOR;

    /**
     * The table <code>public.actor_info</code>.
     */
    public static final ActorInfo ACTOR_INFO = ActorInfo.ACTOR_INFO;

    /**
     * The table <code>public.address</code>.
     */
    public static final Address ADDRESS = Address.ADDRESS;

    /**
     * The table <code>public.category</code>.
     */
    public static final Category CATEGORY = Category.CATEGORY;

    /**
     * The table <code>public.city</code>.
     */
    public static final City CITY = City.CITY;

    /**
     * The table <code>public.country</code>.
     */
    public static final Country COUNTRY = Country.COUNTRY;

    /**
     * The table <code>public.customer</code>.
     */
    public static final Customer CUSTOMER = Customer.CUSTOMER;

    /**
     * The table <code>public.customer_list</code>.
     */
    public static final CustomerList CUSTOMER_LIST = CustomerList.CUSTOMER_LIST;

    /**
     * The table <code>public.film</code>.
     */
    public static final Film FILM = Film.FILM;

    /**
     * The table <code>public.film_actor</code>.
     */
    public static final FilmActor FILM_ACTOR = FilmActor.FILM_ACTOR;

    /**
     * The table <code>public.film_category</code>.
     */
    public static final FilmCategory FILM_CATEGORY = FilmCategory.FILM_CATEGORY;

    /**
     * The table <code>public.film_in_stock</code>.
     */
    public static final FilmInStock FILM_IN_STOCK = FilmInStock.FILM_IN_STOCK;

    /**
     * Call <code>public.film_in_stock</code>.
     */
    public static Result<FilmInStockRecord> FILM_IN_STOCK(
          Configuration configuration
        , Integer pFilmId
        , Integer pStoreId
    ) {
        return configuration.dsl().selectFrom(db.jooq.generated.tables.FilmInStock.FILM_IN_STOCK.call(
              pFilmId
            , pStoreId
        )).fetch();
    }

    /**
     * Get <code>public.film_in_stock</code> as a table.
     */
    public static FilmInStock FILM_IN_STOCK(
          Integer pFilmId
        , Integer pStoreId
    ) {
        return db.jooq.generated.tables.FilmInStock.FILM_IN_STOCK.call(
            pFilmId,
            pStoreId
        );
    }

    /**
     * Get <code>public.film_in_stock</code> as a table.
     */
    public static FilmInStock FILM_IN_STOCK(
          Field<Integer> pFilmId
        , Field<Integer> pStoreId
    ) {
        return db.jooq.generated.tables.FilmInStock.FILM_IN_STOCK.call(
            pFilmId,
            pStoreId
        );
    }

    /**
     * The table <code>public.film_list</code>.
     */
    public static final FilmList FILM_LIST = FilmList.FILM_LIST;

    /**
     * The table <code>public.film_not_in_stock</code>.
     */
    public static final FilmNotInStock FILM_NOT_IN_STOCK = FilmNotInStock.FILM_NOT_IN_STOCK;

    /**
     * Call <code>public.film_not_in_stock</code>.
     */
    public static Result<FilmNotInStockRecord> FILM_NOT_IN_STOCK(
          Configuration configuration
        , Integer pFilmId
        , Integer pStoreId
    ) {
        return configuration.dsl().selectFrom(db.jooq.generated.tables.FilmNotInStock.FILM_NOT_IN_STOCK.call(
              pFilmId
            , pStoreId
        )).fetch();
    }

    /**
     * Get <code>public.film_not_in_stock</code> as a table.
     */
    public static FilmNotInStock FILM_NOT_IN_STOCK(
          Integer pFilmId
        , Integer pStoreId
    ) {
        return db.jooq.generated.tables.FilmNotInStock.FILM_NOT_IN_STOCK.call(
            pFilmId,
            pStoreId
        );
    }

    /**
     * Get <code>public.film_not_in_stock</code> as a table.
     */
    public static FilmNotInStock FILM_NOT_IN_STOCK(
          Field<Integer> pFilmId
        , Field<Integer> pStoreId
    ) {
        return db.jooq.generated.tables.FilmNotInStock.FILM_NOT_IN_STOCK.call(
            pFilmId,
            pStoreId
        );
    }

    /**
     * The table <code>public.inventory</code>.
     */
    public static final Inventory INVENTORY = Inventory.INVENTORY;

    /**
     * The table <code>public.language</code>.
     */
    public static final Language LANGUAGE = Language.LANGUAGE;

    /**
     * The table <code>public.nicer_but_slower_film_list</code>.
     */
    public static final NicerButSlowerFilmList NICER_BUT_SLOWER_FILM_LIST = NicerButSlowerFilmList.NICER_BUT_SLOWER_FILM_LIST;

    /**
     * The table <code>public.payment</code>.
     */
    public static final Payment PAYMENT = Payment.PAYMENT;

    /**
     * The table <code>public.payment_p2007_01</code>.
     */
    public static final PaymentP2007_01 PAYMENT_P2007_01 = PaymentP2007_01.PAYMENT_P2007_01;

    /**
     * The table <code>public.payment_p2007_02</code>.
     */
    public static final PaymentP2007_02 PAYMENT_P2007_02 = PaymentP2007_02.PAYMENT_P2007_02;

    /**
     * The table <code>public.payment_p2007_03</code>.
     */
    public static final PaymentP2007_03 PAYMENT_P2007_03 = PaymentP2007_03.PAYMENT_P2007_03;

    /**
     * The table <code>public.payment_p2007_04</code>.
     */
    public static final PaymentP2007_04 PAYMENT_P2007_04 = PaymentP2007_04.PAYMENT_P2007_04;

    /**
     * The table <code>public.payment_p2007_05</code>.
     */
    public static final PaymentP2007_05 PAYMENT_P2007_05 = PaymentP2007_05.PAYMENT_P2007_05;

    /**
     * The table <code>public.payment_p2007_06</code>.
     */
    public static final PaymentP2007_06 PAYMENT_P2007_06 = PaymentP2007_06.PAYMENT_P2007_06;

    /**
     * The table <code>public.rental</code>.
     */
    public static final Rental RENTAL = Rental.RENTAL;

    /**
     * The table <code>public.rewards_report</code>.
     */
    public static final RewardsReport REWARDS_REPORT = RewardsReport.REWARDS_REPORT;

    /**
     * Call <code>public.rewards_report</code>.
     */
    public static Result<RewardsReportRecord> REWARDS_REPORT(
          Configuration configuration
        , Integer minMonthlyPurchases
        , BigDecimal minDollarAmountPurchased
    ) {
        return configuration.dsl().selectFrom(db.jooq.generated.tables.RewardsReport.REWARDS_REPORT.call(
              minMonthlyPurchases
            , minDollarAmountPurchased
        )).fetch();
    }

    /**
     * Get <code>public.rewards_report</code> as a table.
     */
    public static RewardsReport REWARDS_REPORT(
          Integer minMonthlyPurchases
        , BigDecimal minDollarAmountPurchased
    ) {
        return db.jooq.generated.tables.RewardsReport.REWARDS_REPORT.call(
            minMonthlyPurchases,
            minDollarAmountPurchased
        );
    }

    /**
     * Get <code>public.rewards_report</code> as a table.
     */
    public static RewardsReport REWARDS_REPORT(
          Field<Integer> minMonthlyPurchases
        , Field<BigDecimal> minDollarAmountPurchased
    ) {
        return db.jooq.generated.tables.RewardsReport.REWARDS_REPORT.call(
            minMonthlyPurchases,
            minDollarAmountPurchased
        );
    }

    /**
     * The table <code>public.sales_by_film_category</code>.
     */
    public static final SalesByFilmCategory SALES_BY_FILM_CATEGORY = SalesByFilmCategory.SALES_BY_FILM_CATEGORY;

    /**
     * The table <code>public.sales_by_store</code>.
     */
    public static final SalesByStore SALES_BY_STORE = SalesByStore.SALES_BY_STORE;

    /**
     * The table <code>public.staff</code>.
     */
    public static final Staff STAFF = Staff.STAFF;

    /**
     * The table <code>public.staff_list</code>.
     */
    public static final StaffList STAFF_LIST = StaffList.STAFF_LIST;

    /**
     * The table <code>public.store</code>.
     */
    public static final Store STORE = Store.STORE;
}
