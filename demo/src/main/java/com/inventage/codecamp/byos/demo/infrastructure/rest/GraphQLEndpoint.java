package com.inventage.codecamp.byos.demo.infrastructure.rest;

import byos.GraphQLService;
import byos.RequestInfo;
import com.inventage.codecamp.byos.demo.infrastructure.graphql.GraphQLSchemaGenerator;
import com.inventage.codecamp.byos.demo.infrastructure.jooq.JoinConditionGenerator;
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

    @Inject
    JoinConditionGenerator joinConditionGenerator;

    GraphQLEndpoint() {}

    @Inject
    GraphQLEndpoint(DSLContext jooq, GraphQLSchemaGenerator graphQLSchemaGenerator, JoinConditionGenerator joinConditionGenerator) {
        this.jooq = jooq;
        String schema = "public";
        this.graphQLSchemaGenerator = graphQLSchemaGenerator;
        this.joinConditionGenerator = joinConditionGenerator.init(schema);
        this.graphQLService = new GraphQLService(
                graphQLSchemaGenerator.createSchema(schema),
                joinConditionGenerator, jooq);
    }

    @POST
    public String graphql(String body) {
        RequestInfo requestInfo = graphQLService.extractRequestInfoFromBody(body);
        String result = graphQLService.executeGraphQLQuery(requestInfo);
        return result;
    }
}
