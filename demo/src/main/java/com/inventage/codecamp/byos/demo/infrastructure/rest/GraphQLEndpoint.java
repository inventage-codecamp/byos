package com.inventage.codecamp.byos.demo.infrastructure.rest;

import example.DemoDatabaseMapper;
import example.DemoSchemaProvider;
import byos.GraphQLService;
import byos.RequestInfo;
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

    GraphQLEndpoint() {}

    @Inject
    GraphQLEndpoint(DSLContext jooq) {
        this.jooq = jooq;
        this.graphQLService = new GraphQLService(new DemoSchemaProvider().getSchema(), new DemoDatabaseMapper(), jooq);
    }

    @POST
    public String graphql(String body) {
        RequestInfo requestInfo = graphQLService.extractRequestInfoFromBody(body);
        String result = graphQLService.executeGraphQLQuery(requestInfo);
        return result;
    }
}
