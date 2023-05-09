package byos

import db.jooq.generated.Tables.BOOK
import db.jooq.generated.Tables.BOOK_TO_BOOKSTORE
import db.jooq.generated.tables.Author
import db.jooq.generated.tables.Book
import db.jooq.generated.tables.BookToBookstore
import db.jooq.generated.tables.Bookstore
import db.jooq.generated.tables.Language
import db.jooq.generated.tables.Shoporder
import db.jooq.generated.tables.Shopuser
import db.jooq.generated.tables.Tree
import graphql.language.Argument
import graphql.language.ArrayValue
import graphql.language.BooleanValue
import graphql.language.EnumValue
import graphql.language.FloatValue
import graphql.language.IntValue
import graphql.language.NullValue
import graphql.language.StringValue
import graphql.language.Value
import org.jooq.Condition
import org.jooq.Field
import org.jooq.Table
import org.jooq.impl.DSL

object WhereCondition {
    fun getForRelationship(relationshipName: String, left: Table<*>, right: Table<*>): Condition =
        when {
            relationshipName == "author" && left is Book && right is Author -> left.AUTHORID.eq(right.ID)
            relationshipName == "books" && left is Author && right is Book -> right.AUTHORID.eq(left.ID)
            relationshipName == "user" && left is Shoporder && right is Shopuser -> right.USER_ID.eq(left.USER_ID)
            relationshipName == "orders" && left is Shopuser && right is Shoporder -> right.USER_ID.eq(left.USER_ID)
            relationshipName == "children" && left is Tree && right is Tree -> left.ID.eq(right.PARENT_ID)
            relationshipName == "parent" && left is Tree && right is Tree -> right.ID.eq(left.PARENT_ID)
            relationshipName == "books" && left is Bookstore && right is Book -> DSL.exists(
                DSL.selectOne().from(BOOK_TO_BOOKSTORE).where(left.NAME.eq(BOOK_TO_BOOKSTORE.NAME).and(BOOK_TO_BOOKSTORE.BOOKID.eq(right.ID)))
            )

            relationshipName == "b2b" && left is Bookstore && right is BookToBookstore -> left.NAME.eq(right.NAME)
            relationshipName == "book" && left is BookToBookstore && right is Book -> left.BOOKID.eq(right.ID)
            relationshipName == "language" && left is Book && right is Language -> left.LANGUAGEID.eq(right.ID)
            relationshipName == "publicationLanguages" && left is Author && right is Language -> DSL.exists(
                DSL.selectOne().from(BOOK).where(left.ID.eq(BOOK.AUTHORID).and(BOOK.LANGUAGEID.eq(right.ID)))
            )

            else -> error("No relationship called $relationshipName found for tables $left and $right")
        }
    
    fun getForArgument(argument: Argument, table: Table<*>): Condition {
        val field = table.field(argument.name) as Field<Any>?
            ?: error("No field called ${argument.name} found for table $table")

        return when (val value = extractValue(argument.value)) {
            is List<*> -> field.`in`(value)
            else -> field.eq(value)
        }
    }

    private fun extractValue(value: Value<Value<*>>): Any? =
        when (value) {
            is IntValue -> value.value
            is FloatValue -> value.value
            is BooleanValue -> value.isValue
            is StringValue -> value.value
            is EnumValue -> value.name
            is NullValue -> null
            is ArrayValue -> value.values.map { extractValue(it) }
            else -> error("Unsupported argument type ${value.javaClass}")
        }

}
