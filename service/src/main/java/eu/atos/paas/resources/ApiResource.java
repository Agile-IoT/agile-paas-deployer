/**
 *  Copyright (c) 2017 Atos, and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License 2.0
 *  which accompanies this distribution, and is available at
 *  https://www.eclipse.org/legal/epl-2.0/
 *
 *  Contributors:
 *  Atos - initial implementation
 */
package eu.atos.paas.resources;

import java.util.HashMap;
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
    public Map<String, Provider> getProvidersSet() {
        Map<String, Provider> providers = new HashMap<>();
        for (Map.Entry<String, PaasResource>entry : map.entrySet()) {
            providers.put(entry.getKey(), entry.getValue().getProvider());
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
