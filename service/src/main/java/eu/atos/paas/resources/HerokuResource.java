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

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.atos.paas.Module;
import eu.atos.paas.PaasSession;
import eu.atos.paas.ServiceApp;
import eu.atos.paas.credentials.ApiKeyCredentials;
import eu.atos.paas.credentials.Credentials;
import eu.atos.paas.credentials.UserPasswordCredentials;
import eu.atos.paas.data.CredentialsMap;
import eu.atos.paas.data.Provider;


public class HerokuResource extends PaasResource
{
    private static Logger log = LoggerFactory.getLogger(HerokuResource.class);


    /**
     * 
     * @param client
     */
    public HerokuResource(Provider provider, ClientMap clientMap)
    {
        super(provider, clientMap, new ParametersTranslatorImpl());
    }
    
    
    @PUT
    @Path("/applications/{name}/bind/{service}")
    @Override
    public Response bindApplication(@PathParam("name") String name, @PathParam("service") String service, @Context HttpHeaders headers)
    {
        log.info("bindApplication({}, {})", name, service);
        PaasSession session = getSession(headers);
        
        Module m = session.getModule(name);
        // heroku ... cleardb:ignite
        ServiceApp serviceapp = new ServiceApp(service);
        
        session.bindToService(m, serviceapp);

        // Response
        return generateJSONResponse(Response.Status.OK, OperationResult.OK,
                                    "PUT /applications/" + name + "/bind/" + service, 
                                    "service " + service + " binded to app: " + name);
    }
    
    
    @PUT
    @Path("/applications/{name}/unbind/{service}")
    @Override
    public Response unbindApplication(@PathParam("name") String name, @PathParam("service") String service, @Context HttpHeaders headers)
    {
        log.info("unbindApplication({}, {})", name, service);
        PaasSession session = getSession(headers);
        
        Module m = session.getModule(name);
        // heroku ... cleardb
        ServiceApp serviceapp = new ServiceApp(service);
        
        session.unbindFromService(m, serviceapp);

        // Response
        return generateJSONResponse(Response.Status.OK, OperationResult.OK,
                                    "PUT /applications/" + name + "/unbind/" + service, 
                                    "service " + service + " unbinded from app: " + name);
    }

    @Override
    protected Credentials buildCredentialsFromFieldsMap(CredentialsMap credentialsMap) 
            throws IllegalArgumentException {
        
        if (credentialsMap.containsKey(ApiKeyCredentials.API_KEY)) {
            return new ApiKeyCredentials(credentialsMap);
        }
        if (credentialsMap.containsKey(UserPasswordCredentials.USER)) {
            return new UserPasswordCredentials(credentialsMap);
        }
        throw new IllegalArgumentException("Wrong credential scheme for provider: " + credentialsMap);
    }
}
