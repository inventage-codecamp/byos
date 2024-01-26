package byos

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import graphql.GraphQL
import graphql.introspection.IntrospectionQuery
import graphql.language.Document
import graphql.language.OperationDefinition
import graphql.parser.Parser
import graphql.schema.GraphQLSchema
import graphql.validation.Validator
import org.jooq.DSLContext
import java.util.*

data class RequestInfo(
        val document: Document,
        val operationName: String?,
        val variables: Map<String, JsonNode>
)


class GraphQLService(val schema: GraphQLSchema, val databaseMapper: DatabaseMapper, val jooq: DSLContext) {

    private val introspectionQuery = "IntrospectionQuery"
    private val locale = Locale.ENGLISH

    private val jsonObjectMapper = ObjectMapper()

    private val schemaValidator = Validator()

    private val graphQL = GraphQL.newGraphQL(schema).build()
    private val graphqlParser = Parser()

    private val queryTranspiler =
        QueryTranspiler(
            WhereCondition(databaseMapper),
            schema,
            databaseMapper
        )

    fun extractRequestInfoFromBody(requestBody: String): RequestInfo? {
        val jsonNode = jsonObjectMapper.readTree(requestBody)

        val queries = jsonNode["query"]?.textValue()
        if (queries.isNullOrBlank()) {
            return null
        }

        val document = graphqlParser.parseDocument(queries)
        val variables =
                jsonNode["variables"]?.fields()?.asSequence()?.associate { (key, value) ->
                    key to value
                }
                        ?: emptyMap()

        val operationName = jsonNode["operationName"]?.textValue()
        return RequestInfo(document, operationName, variables)
    }

    fun executeGraphQLQuery(requestInfo: RequestInfo): String {
        val (document, operationName, _) = requestInfo // TODO support variables

        val errors = schemaValidator.validateDocument(schema, document, locale)
        if (errors.isNotEmpty()) {
            return jsonObjectMapper.writeValueAsString(
                    mapOf("data" to null, "errors" to errors.map { it.toSpecification() })
            )
        }

        val ast =
                operationName?.let { document.getOperationDefinition(it).get() }
                        ?: document.definitions.filterIsInstance<OperationDefinition>().single()

        if (ast.name == introspectionQuery) {
            val result = graphQL.execute(IntrospectionQuery.INTROSPECTION_QUERY)
            return jsonObjectMapper.writeValueAsString(result.toSpecification())
        }

        val queryTrees = queryTranspiler.buildInternalQueryTrees(ast)
        val results =
                queryTrees.map { tree ->
                    jooq.select(queryTranspiler.resolveInternalQueryTree(tree)).fetch()
                }

        results.map(::println) // TODO rm debug statement
        return results.formatGraphQLResponse()
    }
}