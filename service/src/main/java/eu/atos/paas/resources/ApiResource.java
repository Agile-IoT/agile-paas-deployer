/**
 * Copyright 2016 Atos
 * Contact: Atos <roman.sosa@atos.net>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
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
