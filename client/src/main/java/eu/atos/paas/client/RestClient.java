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
package eu.atos.paas.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.StreamDataBodyPart;

import eu.atos.paas.data.Application;
import eu.atos.paas.data.ApplicationToCreate;
import eu.atos.paas.data.CredentialsMap;
import eu.atos.paas.data.Provider;
import eu.atos.paas.resources.Constants;
import eu.atos.paas.data.CredentialsMap.PlainTransformer;

public class RestClient {
    private static GenericType<List<Application>> APPLICATION_LIST_TYPE = new GenericType<List<Application>>(){};
    /*
     * URL of root api (/api)
     */
    private URL root;
    private Client client;
    private ResponseHandler responseHandler = new ResponseHandler();
    
    public RestClient(URL root) {
        this.root = root;
        this.client = ClientBuilder.newClient()
                .register(MultiPartFeature.class)
                .register(JacksonFeature.class);
    }

    /**
     * Gets a Provider resource, which will use the default provider version for the operations.
     * @param provider provider subpath from root (e.g. "cloudfoundry")
     * @param credentials CredentialsMap with the credentials to connect to the specified provider. The content of
     * the credentials depends on the provider
     */
    public ProviderClient getProvider(String provider, CredentialsMap credentials) {
        return new ProviderClient(provider, "", credentials);
    }

    /**
     * Gets a Provider resource, specifying a provider version for the operations.
     * @param provider provider subpath from root (e.g. "cloudfoundry")
     * @param apiVersion version to use to connect to provider. Available versions are returned in provider description.
     * @param credentials CredentialsMap with the credentials to connect to the specified provider. The content of
     * the credentials depends on the provider
     */
    public ProviderClient getProvider(String provider, String apiVersion, CredentialsMap credentials) {
        return new ProviderClient(provider, apiVersion, credentials);
    }
    
    public class ProviderClient {
        
        private String provider;
        private String apiVersion;
        private CredentialsMap credentials;
        private PlainTransformer transformer;
        /*
         * The rootResource of the session, i.e., the provider resource (e.g. /api/heroku)
         */
        private WebTarget rootTarget;
        private WebTarget appsTarget;
        
        public ProviderClient(String provider, String apiVersion, CredentialsMap credentials) {
            this.credentials = credentials;
            this.apiVersion = apiVersion;
            this.provider = provider;
            this.rootTarget = client.target(root.toString()).path(this.provider);
            this.appsTarget = rootTarget.path("applications");
            this.transformer = new PlainTransformer();
        }

        public Provider getProviderDescription() {
            Response response = rootTarget.request(MediaType.APPLICATION_JSON).get();
            Provider provider = responseHandler.readEntity(response, Provider.class);
            return provider;
        }
        
        public List<Application> getApplications() {
            Response response = appsTarget.request(MediaType.APPLICATION_JSON)
                .header(Constants.Headers.CREDENTIALS, transformer.serialize(credentials))
                .header(Constants.Headers.PROVIDER_VERSION, apiVersion)
                .get();
            List<Application> applications = responseHandler.readEntity(response, APPLICATION_LIST_TYPE);
            return applications;
        }
        
        public Application getApplication(String name) {
            WebTarget appTarget = appsTarget.path(name);
            Response response = appTarget.request(MediaType.APPLICATION_JSON)
                .header(Constants.Headers.CREDENTIALS, transformer.serialize(credentials))
                .header(Constants.Headers.PROVIDER_VERSION, apiVersion)
                .get();
            Application application = responseHandler.readEntity(response, Application.class);
            return application;
        }
        
        public Application createApplication(ApplicationToCreate application) throws IOException {
            return createApplicationImpl(application, application.getArtifact());
        }
        
        private Application createApplicationImpl(ApplicationToCreate application, InputStream is)
                throws IOException {
            
            try (FormDataMultiPart multipart = new FormDataMultiPart()) {
                
                multipart
                    .field(Constants.MultiPartFields.MODEL, application, MediaType.APPLICATION_JSON_TYPE);
                if (is != null) {
                    final StreamDataBodyPart filePart = new StreamDataBodyPart(Constants.MultiPartFields.FILE, is);
                    multipart.bodyPart(filePart);
                }

                Response response = appsTarget.request()
                        .accept(MediaType.APPLICATION_JSON_TYPE)
                        .header(Constants.Headers.CREDENTIALS, transformer.serialize(credentials))
                        .header(Constants.Headers.PROVIDER_VERSION, apiVersion)
                        .post(Entity.entity(multipart, multipart.getMediaType()));

                Application createdApplication = responseHandler.readEntity(response, Application.class);
                return createdApplication;
            }
        }
        
    }
    
    /**
     * Handles a response, returning the entity as the specified type, or throwing an exception.
     */
    public static class ResponseHandler {
        
        public <T> T readEntity(Response response, Class<T> entityType) throws RestClientException {
            if (Family.SUCCESSFUL.equals(response.getStatusInfo().getFamily())) {
                return response.readEntity(entityType);
            }
            else {
                throw new RestClientException(response);
            }
        }

        
        public <T> T readEntity(Response response, GenericType<T> entityType) {
            if (Family.SUCCESSFUL.equals(response.getStatusInfo().getFamily())) {
                return response.readEntity(entityType);
            }
            else {
                throw new RestClientException(response);
            }
        }
    }
}
