package byos

import graphql.schema.GraphQLSchema

interface SchemaProvider {

    fun getSchema() : GraphQLSchema
}