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

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.atos.paas.PaasSession.DeployParameters;
import eu.atos.paas.credentials.ApiUserPasswordCredentials;
import eu.atos.paas.credentials.Credentials;
import eu.atos.paas.data.ApplicationToCreate;
import eu.atos.paas.data.CredentialsMap;
import eu.atos.paas.data.Provider;
import eu.atos.paas.resources.exceptions.ResourceException;
import static eu.atos.deployer.faas.openwhisk.Constants.Kind;

public class OpenwhiskResource extends PaasResource
{
    private static Logger log = LoggerFactory.getLogger(OpenwhiskResource.class);


    /**
     * 
     * Constructor
     * @param client
     */
    public OpenwhiskResource(Provider provider, ClientMap clientMap)
    {
        super(provider, clientMap, new OpenwhiskParametersTranslator());
    }



    @PUT
    @Path("/applications/{name}/bind/{service}")
    @Override
    public Response bindApplication(@PathParam("name") String name, @PathParam("service") String service, @Context HttpHeaders headers)
    {
        log.info("bindApplication({}, {})", name, service);
        throw new UnsupportedOperationException();
    }


    @PUT
    @Path("/applications/{name}/unbind/{service}")
    @Override
    public Response unbindApplication(@PathParam("name") String name, @PathParam("service") String service, @Context HttpHeaders headers)
    {
        log.info("unbindApplication({}, {})", name, service);
        throw new UnsupportedOperationException();
    }


    @Override
    protected Credentials buildCredentialsFromFieldsMap(CredentialsMap credentialsMap)
            throws IllegalArgumentException {

        /*
         * Set API field to provider URL in case using a specific OpenShift provider
         */
        if (!this.getProvider().getName().equals(Constants.Providers.OPENWHISK)) {
            credentialsMap.put(ApiUserPasswordCredentials.API, provider.getUrl().toString());
        }
        return new ApiUserPasswordCredentials(credentialsMap);
    }

    public static class OpenwhiskParametersTranslator implements ParametersTranslator {

        @Override
        public DeployParameters translate(ApplicationToCreate application, File uploadedFile) {
            String language;
            switch (application.getProgrammingLanguage()) {
            case "Java": 
                language = Kind.JAVA;
                break;
            case "Python": 
                language = Kind.PYTHON;
                break;
            case "PhP": 
                language = Kind.PHP7;
                break;
            case "Swift": 
                language = Kind.SWIFT;
                break;
            case "Node.JS":
                language = Kind.DEFAULT;
                break;
            default:
                throw new ResourceException(
                        new ErrorEntity(
                                Status.NOT_IMPLEMENTED,
                                "Programming language not supported: " + application.getProgrammingLanguage()));
            }

            DeployParameters params = ParametersTranslatorImpl.getBuilder(application, uploadedFile)
                    .property(DeployParameters.Properties.LANGUAGE, language)
                    .build();

            return params;
        }

    }
}
