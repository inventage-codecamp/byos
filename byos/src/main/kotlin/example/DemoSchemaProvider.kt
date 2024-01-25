package example

import byos.SchemaProvider
import graphql.schema.GraphQLSchema
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import java.io.File

class DemoSchemaProvider : SchemaProvider {

    override fun getSchema(): GraphQLSchema {
        val schemaFile = File("src/main/resources/graphql/schema.graphqls")
        val schema =
                SchemaGenerator()
                        .makeExecutableSchema(
                                SchemaParser().parse(schemaFile),
                                RuntimeWiring.newRuntimeWiring().build()
                        )
        return schema
    }
}
