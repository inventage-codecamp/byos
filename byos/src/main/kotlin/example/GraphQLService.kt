package example

import byos.QueryTranspiler
import byos.WhereCondition
import byos.executeJooqQuery
import byos.formatGraphQLResponse
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import graphql.GraphQL
import graphql.introspection.IntrospectionQuery
import graphql.language.Document
import graphql.language.OperationDefinition
import graphql.parser.Parser
import graphql.validation.Validator
import java.util.*

class GraphQLService() {

    companion object {
        private val schema = DemoSchemaProvider().getSchema()
        private val graphQL = GraphQL.newGraphQL(schema).build()
        private val objectMapper = ObjectMapper()
        private val parser = Parser()
        private val databaseMapper = DemoDatabaseMapper()
        private val queryTranspiler = QueryTranspiler(WhereCondition(Companion.databaseMapper), schema, Companion.databaseMapper)
    }

    fun executeGraphQLQuery(requestInfo: RequestInfo): String {
        val (document, selectedQuery, _) = requestInfo

        val errors = Validator().validateDocument(schema, document, Locale.ENGLISH)
        if (errors.isNotEmpty()) {
            return objectMapper.writeValueAsString(
                mapOf("data" to null, "errors" to errors.map { it.toSpecification() })
            )
        }

        val ast = selectedQuery?.let { document.getOperationDefinition(it).get() }
            ?: document.definitions.filterIsInstance<OperationDefinition>().single()

        if (ast.name == "IntrospectionQuery") {
            val result = graphQL.execute(IntrospectionQuery.INTROSPECTION_QUERY)
            return objectMapper.writeValueAsString(result.toSpecification())
        }

        val queryTrees = queryTranspiler.buildInternalQueryTrees(ast)
        val results =
            queryTrees.map { tree ->
                executeJooqQuery { ctx ->
                    ctx.select(queryTranspiler.resolveInternalQueryTree(tree)).fetch()
                }
            }
        results.map(::println)
        return results.formatGraphQLResponse()
    }

    fun executeGraphQLQuery(query: String) = executeGraphQLQuery(RequestInfo(parser.parseDocument(query), null, emptyMap()))

    fun extractRequestInfoFromBody(requestBody: String): RequestInfo? {
        val jsonNode = objectMapper.readTree(requestBody)
        val queries = jsonNode["query"]?.textValue()
        if (queries.isNullOrBlank()) return null
        val document = parser.parseDocument(queries)
        val variableDefinitions = jsonNode["variables"]?.fields()?.asSequence()?.associate { (key, value) -> key to value } ?: emptyMap()
        val selectedQuery = jsonNode["operationName"]?.textValue()
        return RequestInfo(document, selectedQuery, variableDefinitions)
    }
}

data class RequestInfo(val document: Document, val selectedQuery: String?, val variables: Map<String, JsonNode>)
