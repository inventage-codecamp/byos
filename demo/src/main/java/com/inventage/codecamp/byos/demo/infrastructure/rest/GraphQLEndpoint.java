package com.inventage.codecamp.byos.demo.infrastructure.rest;

import example.GraphQLService;
import example.RequestInfo;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/graphql")
@Produces({MediaType.APPLICATION_JSON})
public class GraphQLEndpoint {

    final private GraphQLService graphQLService;

    public GraphQLEndpoint() {
        graphQLService = new GraphQLService();
    }

    @POST
    public String graphql(String body) {
        RequestInfo requestInfo = graphQLService.extractRequestInfoFromBody(body);
        String result = graphQLService.executeGraphQLQuery(requestInfo);
        return result;
    }
}
