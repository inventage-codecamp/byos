package com.inventage.codecamp.byos.demo.infrastructure.rest;

import byos.GraphQLService;
import byos.RequestInfo;
import com.inventage.codecamp.byos.demo.infrastructure.jooq.SchemaMetadataGenerator;
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
    SchemaMetadataGenerator schemaMetadataGenerator;

    GraphQLEndpoint() {}

    @Inject
    GraphQLEndpoint(DSLContext jooq, SchemaMetadataGenerator schemaMetadataGenerator) {
        this.jooq = jooq;
        this.schemaMetadataGenerator = schemaMetadataGenerator;
        this.graphQLService = new GraphQLService(schemaMetadataGenerator.doit(), new DemoDatabaseMapper(), jooq);
    }

    @POST
    public String graphql(String body) {
        RequestInfo requestInfo = graphQLService.extractRequestInfoFromBody(body);
        String result = graphQLService.executeGraphQLQuery(requestInfo);
        return result;
    }
}
