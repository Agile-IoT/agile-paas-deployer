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

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import eu.atos.paas.PaasClient;
import eu.atos.paas.credentials.Credentials;
import eu.atos.paas.credentials.UserPasswordCredentials;
import eu.atos.paas.data.CredentialsMap;
import eu.atos.paas.data.Provider;

public class DummyResource extends PaasResource {

    public DummyResource(Provider provider, ClientMap clientMap) {
        super(provider, clientMap, new ParametersTranslatorImpl());
    }

    public DummyResource(PaasClient dummyClient) {
        this(
            new Provider(Constants.Providers.DUMMY, "http://www.example.com", dummyClient.getVersion()),
            ClientMap.builder().client(dummyClient).build()
        );
    }
    
    @PUT
    @Path("/applications/{name}/bind/{service}")
    @Override
    public Response bindApplication(@PathParam("name") String name, @PathParam("service") String service, 
            @Context HttpHeaders headers) {
        
        throw new WebApplicationException("Not supported", Status.NOT_IMPLEMENTED);
    }

    @PUT
    @Path("/applications/{name}/unbind/{service}")
    @Override
    public Response unbindApplication(@PathParam("name") String name, @PathParam("service") String service, 
            @Context HttpHeaders headers) {
        
        throw new WebApplicationException("Not supported", Status.NOT_IMPLEMENTED);
    }

    @Override
    protected Credentials buildCredentialsFromFieldsMap(CredentialsMap credentialsMap) {
        return new UserPasswordCredentials(credentialsMap);
    }
    
}
