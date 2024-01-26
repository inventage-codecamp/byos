package com.inventage.codecamp.byos.demo.infrastructure.rest;

import byos.GraphQLService;
import byos.RequestInfo;
import com.inventage.codecamp.byos.demo.infrastructure.jooq.GraphQLSchemaGenerator;
import example.DemoDatabaseMapper;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jooq.DSLContext;

@Path("/graphql")
@Produces({MediaType.APPLICATION_JSON})
public class GraphQLEndpoint {

    DSLContext jooq;

    GraphQLService graphQLService;

    @Inject
    GraphQLSchemaGenerator graphQLSchemaGenerator;

    GraphQLEndpoint() {}

    @Inject
    GraphQLEndpoint(DSLContext jooq, GraphQLSchemaGenerator graphQLSchemaGenerator) {
        this.jooq = jooq;
        this.graphQLSchemaGenerator = graphQLSchemaGenerator;
        this.graphQLService = new GraphQLService(graphQLSchemaGenerator.createSchema(), new DemoDatabaseMapper(), jooq);
    }

    @POST
    public String graphql(String body) {
        graphQLSchemaGenerator.createSchema();
        RequestInfo requestInfo = graphQLService.extractRequestInfoFromBody(body);
        String result = graphQLService.executeGraphQLQuery(requestInfo);
        return result;
    }
}
