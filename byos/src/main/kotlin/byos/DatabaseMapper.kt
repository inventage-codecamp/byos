package byos

import org.jooq.Condition
import org.jooq.Record
import org.jooq.Table

interface DatabaseMapper {

    fun getTableWithAlias(relation: InternalQueryNode.Relation): Table<out Record>

    fun getConditionForRelationship(
            relationshipName: String,
            left: Table<*>,
            right: Table<*>
    ): Condition?
}
