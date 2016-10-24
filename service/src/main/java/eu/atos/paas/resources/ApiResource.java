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


@Path("/")
public class ApiResource {

    private Map<String, PaaSResource> map;
    
    public ApiResource(Map<String, PaaSResource> subResourceMap) {
        this.map = new HashMap<>(subResourceMap);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Set<String> getProvidersSet() {
        return map.keySet();
    }
    
    @Path("{provider}")
    public PaaSResource getProvider(@PathParam("provider") String provider) {
        if (map.containsKey(provider)) {
            return map.get(provider);
        }
        throw new WebApplicationException("Provider " + provider + " not found", Status.NOT_FOUND);
    }
}
