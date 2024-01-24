/*
 * This file is generated by jOOQ.
 */
package db.jooq.generated.tables;


import db.jooq.generated.Public;
import db.jooq.generated.enums.MpaaRating;
import db.jooq.generated.tables.records.FilmListRecord;

import java.math.BigDecimal;
import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function8;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row8;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class FilmList extends TableImpl<FilmListRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.film_list</code>
     */
    public static final FilmList FILM_LIST = new FilmList();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<FilmListRecord> getRecordType() {
        return FilmListRecord.class;
    }

    /**
     * The column <code>public.film_list.fid</code>.
     */
    public final TableField<FilmListRecord, Integer> FID = createField(DSL.name("fid"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>public.film_list.title</code>.
     */
    public final TableField<FilmListRecord, String> TITLE = createField(DSL.name("title"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>public.film_list.description</code>.
     */
    public final TableField<FilmListRecord, String> DESCRIPTION = createField(DSL.name("description"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.film_list.category</code>.
     */
    public final TableField<FilmListRecord, String> CATEGORY = createField(DSL.name("category"), SQLDataType.VARCHAR(25), this, "");

    /**
     * The column <code>public.film_list.price</code>.
     */
    public final TableField<FilmListRecord, BigDecimal> PRICE = createField(DSL.name("price"), SQLDataType.NUMERIC(4, 2), this, "");

    /**
     * The column <code>public.film_list.length</code>.
     */
    public final TableField<FilmListRecord, Short> LENGTH = createField(DSL.name("length"), SQLDataType.SMALLINT, this, "");

    /**
     * The column <code>public.film_list.rating</code>.
     */
    public final TableField<FilmListRecord, MpaaRating> RATING = createField(DSL.name("rating"), SQLDataType.VARCHAR.asEnumDataType(db.jooq.generated.enums.MpaaRating.class), this, "");

    /**
     * The column <code>public.film_list.actors</code>.
     */
    public final TableField<FilmListRecord, String> ACTORS = createField(DSL.name("actors"), SQLDataType.CLOB, this, "");

    private FilmList(Name alias, Table<FilmListRecord> aliased) {
        this(alias, aliased, null);
    }

    private FilmList(Name alias, Table<FilmListRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.view("""
        create view "film_list" as  SELECT film.film_id AS fid,
          film.title,
          film.description,
          category.name AS category,
          film.rental_rate AS price,
          film.length,
          film.rating,
          group_concat((((actor.first_name)::text || ' '::text) || (actor.last_name)::text)) AS actors
         FROM ((((category
           LEFT JOIN film_category ON ((category.category_id = film_category.category_id)))
           LEFT JOIN film ON ((film_category.film_id = film.film_id)))
           JOIN film_actor ON ((film.film_id = film_actor.film_id)))
           JOIN actor ON ((film_actor.actor_id = actor.actor_id)))
        GROUP BY film.film_id, film.title, film.description, category.name, film.rental_rate, film.length, film.rating;
        """));
    }

    /**
     * Create an aliased <code>public.film_list</code> table reference
     */
    public FilmList(String alias) {
        this(DSL.name(alias), FILM_LIST);
    }

    /**
     * Create an aliased <code>public.film_list</code> table reference
     */
    public FilmList(Name alias) {
        this(alias, FILM_LIST);
    }

    /**
     * Create a <code>public.film_list</code> table reference
     */
    public FilmList() {
        this(DSL.name("film_list"), null);
    }

    public <O extends Record> FilmList(Table<O> child, ForeignKey<O, FilmListRecord> key) {
        super(child, key, FILM_LIST);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public FilmList as(String alias) {
        return new FilmList(DSL.name(alias), this);
    }

    @Override
    public FilmList as(Name alias) {
        return new FilmList(alias, this);
    }

    @Override
    public FilmList as(Table<?> alias) {
        return new FilmList(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public FilmList rename(String name) {
        return new FilmList(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public FilmList rename(Name name) {
        return new FilmList(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public FilmList rename(Table<?> name) {
        return new FilmList(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row8 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row8<Integer, String, String, String, BigDecimal, Short, MpaaRating, String> fieldsRow() {
        return (Row8) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function8<? super Integer, ? super String, ? super String, ? super String, ? super BigDecimal, ? super Short, ? super MpaaRating, ? super String, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function8<? super Integer, ? super String, ? super String, ? super String, ? super BigDecimal, ? super Short, ? super MpaaRating, ? super String, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
