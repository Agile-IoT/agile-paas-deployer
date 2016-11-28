package eu.atos.paas.resources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import eu.atos.paas.data.Provider;
import eu.atos.paas.dummy.DummyClient;


@Path("/")
public class ApiResource {

    private static final String DUMMY_PATH = "dummy";
    private Map<String, PaasResource> map;
    private DummyResource dummyResource;
    
    public ApiResource(Map<String, PaasResource> subResourceMap) {
        this.map = new HashMap<>(subResourceMap);
        DummyClient dummyClient = new DummyClient();
        this.dummyResource = new DummyResource(dummyClient);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Provider> getProvidersSet() {
        List<Provider> providers = new ArrayList<>();
        for (PaasResource resource : map.values()) {
            providers.add(resource.getProvider());
        }
        return providers;
    }
    
    @Path("{provider}")
    public PaasResource getProvider(@PathParam("provider") String provider) {
        if (DUMMY_PATH.equals(provider)) {
            return dummyResource;
        }
        if (map.containsKey(provider)) {
            return map.get(provider);
        }
        throw new WebApplicationException("Provider " + provider + " not found", Status.NOT_FOUND);
    }
}
