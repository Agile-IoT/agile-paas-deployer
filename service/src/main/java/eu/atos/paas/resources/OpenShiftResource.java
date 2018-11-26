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

import java.io.File;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openshift.client.cartridge.IStandaloneCartridge;

import eu.atos.paas.data.Application;
import eu.atos.paas.data.ApplicationToCreate;
import eu.atos.paas.data.CredentialsMap;
import eu.atos.paas.credentials.ApiUserPasswordCredentials;
import eu.atos.paas.credentials.Credentials;
import eu.atos.paas.data.Provider;
import eu.atos.paas.resources.exceptions.ResourceException;
import io.swagger.annotations.ApiOperation;
import eu.atos.paas.Module;
import eu.atos.paas.PaasSession;
import eu.atos.paas.PaasSession.DeployParameters;
import eu.atos.paas.ServiceApp;


public class OpenShiftResource extends PaasResource
{
    private static Logger log = LoggerFactory.getLogger(OpenShiftResource.class);

    
    /**
     * 
     * Constructor
     * @param client
     */
    public OpenShiftResource(Provider provider, ClientMap clientMap)
    {
        super(provider, clientMap, new OpenShiftParametersTranslator());
    }

    @GET
    @Path("applications/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="Return the status of an application")
    public Application getApplication(
            @PathParam("name") String name, @Context HttpHeaders headers) {
        throw new ResourceException(
                new ErrorEntity(Status.NOT_FOUND, "Use /{provider}/applications/{project}/{name} endpoint"));
    }

    @GET
    @Path("applications/{project}/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="Return the status of an application")
    public Application getApplication(
            @PathParam("project") String project, @PathParam("name") String name, @Context HttpHeaders headers) {
        
        String composedName = buildComposedName(project, name);
        return super.getApplication(composedName, headers);
    }

    @DELETE
    @Path("applications/{project}/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="Removes an application in the provider")
    public Response deleteApplication(
            @PathParam("project") String project, @PathParam("name") String name, @Context HttpHeaders headers) {
        
        String composedName = buildComposedName(project, name);
        return super.deleteApplication(composedName, headers);
    }

    @PUT
    @Path("applications/{project}/{name}/start")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="Removes an application in the provider")
    public Response startApplication(
            @PathParam("project") String project, @PathParam("name") String name, @Context HttpHeaders headers) {
        
        String composedName = buildComposedName(project, name);
        return super.startApplication(composedName, headers);
    }

    @PUT
    @Path("applications/{project}/{name}/stop")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="Removes an application in the provider")
    public Response stopApplication(
            @PathParam("project") String project, @PathParam("name") String name, @Context HttpHeaders headers) {
        
        String composedName = buildComposedName(project, name);
        return super.stopApplication(composedName, headers);
    }

    
    @PUT
    @Path("/applications/{name}/bind/{service}")
    @Override
    public Response bindApplication(@PathParam("name") String name, @PathParam("service") String service, @Context HttpHeaders headers)
    {
        log.info("bindApplication({}, {})", name, service);
        PaasSession session = getSession(headers);
        
        Module m = session.getModule(name);
        // openshift ... mysql-5.5
        session.bindToService(m, new ServiceApp(service));

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
        // openshift ... mysql-5.5
        session.unbindFromService(m, new ServiceApp(service));

        // Response
        return generateJSONResponse(Response.Status.OK, OperationResult.OK,
                                    "PUT /applications/" + name + "/unbind/" + service, 
                                    "service " + service + " unbinded from app: " + name);
    }


    @Override
    protected Credentials buildCredentialsFromFieldsMap(CredentialsMap credentialsMap)
            throws IllegalArgumentException {
        
        /*
         * Set API field to provider URL in case using a specific OpenShift provider
         */
        if (!this.getProvider().getName().equals(Constants.Providers.OPENSHIFT)) {
            credentialsMap.put(ApiUserPasswordCredentials.API, provider.getUrl().toString());
        }
        return new ApiUserPasswordCredentials(credentialsMap);
    }

    public static class OpenShiftParametersTranslator implements ParametersTranslator {

        @Override
        public DeployParameters translate(ApplicationToCreate application, File uploadedFile) {
            String cartridge;
            switch (application.getProgrammingLanguage()) {
            case "Java": 
                cartridge = IStandaloneCartridge.NAME_JBOSSEWS;
                break;
            case "Python": 
                cartridge = IStandaloneCartridge.NAME_PYTHON;
                break;
            case "PhP": 
                cartridge = IStandaloneCartridge.NAME_PHP;
                break;
            case "Perl": 
                cartridge = IStandaloneCartridge.NAME_PERL;
                break;
            case "Ruby": 
                cartridge = IStandaloneCartridge.NAME_RUBY;
                break;
            case "Node.JS":
                cartridge = "nodejs-0.10";
                break;
            default:
                throw new ResourceException(
                        new ErrorEntity(
                                Status.NOT_IMPLEMENTED,
                                "Programming language not supported: " + application.getProgrammingLanguage()));
            }
            
            DeployParameters params = ParametersTranslatorImpl.getBuilder(application, uploadedFile)
                    .property(DeployParameters.Properties.CARTRIDGE, cartridge)
                    .build();
            
            return params;
        }
        
    }
}
