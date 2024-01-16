package stroom.security.shared;

import stroom.util.shared.FetchWithIntegerId;
import stroom.util.shared.ResourcePaths;
import stroom.util.shared.RestResource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.fusesource.restygwt.client.DirectRestService;

import java.util.Collection;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Tag(name = "API Key")
@Path("/apikey" + ResourcePaths.V2)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ApiKeyResource extends RestResource, DirectRestService, FetchWithIntegerId<HashedApiKey> {

    @POST
    @Path("/")
    @Operation(
            summary = "Creates a new API key",
            operationId = "createApiKey")
    CreateHashedApiKeyResponse create(
            @Parameter(description = "request", required = true) final CreateHashedApiKeyRequest request);

    @GET
    @Path("/{id}")
    @Operation(
            summary = "Fetch a dictionary doc by its UUID",
            operationId = "fetchApiKey")
    HashedApiKey fetch(@PathParam("id") Integer id);

    @PUT
    @Path("/{id}")
    @Operation(
            summary = "Update a dictionary doc",
            operationId = "updateApiKey")
    HashedApiKey update(@PathParam("id") final int id,
                        @Parameter(description = "apiKey", required = true) final HashedApiKey apiKey);

    @Operation(
            summary = "Delete an API key by ID.",
            operationId = "deleteApiKey")
    @DELETE
    @Path("/{id}")
    boolean delete(@PathParam("id") final int id);

    @Operation(
            summary = "Delete a batch of API keys by ID.",
            operationId = "deleteApiKey")
    @DELETE
    @Path("/deleteBatch")
    int deleteBatch(@Parameter(description = "ids", required = true) final Collection<Integer> ids);

    @POST
    @Path("/find")
    @Operation(
            summary = "Find the API keys matching the supplied criteria",
            operationId = "findApiKeysByCriteria")
    ApiKeyResultPage find(@Parameter(description = "criteria", required = true) FindApiKeyCriteria criteria);
}
