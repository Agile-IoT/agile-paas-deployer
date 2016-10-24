package eu.atos.paas.resources;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import eu.atos.paas.dummy.DummyClient;


@Path("/")
public class ApiResource {

    private static final String DUMMY_PATH = "dummy";
    private Map<String, PaaSResource> map;
    DummyResource dummyResource;
    public ApiResource(Map<String, PaaSResource> subResourceMap) {
        this.map = new HashMap<>(subResourceMap);
        this.dummyResource = new DummyResource(new DummyClient());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Set<String> getProvidersSet() {
        return map.keySet();
    }
    
    @Path("{provider}")
    public PaaSResource getProvider(@PathParam("provider") String provider) {
        if (DUMMY_PATH.equals(provider)) {
            return dummyResource;
        }
        if (map.containsKey(provider)) {
            return map.get(provider);
        }
        throw new WebApplicationException("Provider " + provider + " not found", Status.NOT_FOUND);
    }
}
