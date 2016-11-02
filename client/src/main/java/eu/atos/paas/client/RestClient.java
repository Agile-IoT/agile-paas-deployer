package eu.atos.paas.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.StreamDataBodyPart;

import eu.atos.paas.data.Application;
import eu.atos.paas.data.CredentialsMap;
import eu.atos.paas.data.Provider;
import eu.atos.paas.resources.Constants;
import eu.atos.paas.data.CredentialsMap.PlainTransformer;

public class RestClient {

    /*
     * URL of root api (/api)
     */
    private URL root;
    private Client client;
    
    public RestClient(URL root) {
        this.root = root;
        this.client = ClientBuilder.newClient()
                .register(MultiPartFeature.class)
                .register(JacksonFeature.class);
    }
    
    public ProviderClient getProvider(String provider, CredentialsMap credentials) {
        return new ProviderClient(provider, credentials);
    }

    public class ProviderClient {
        
        private String provider;
        private CredentialsMap credentials;
        private PlainTransformer transformer;
        /*
         * The rootResource of the session, i.e., the provider resource (e.g. /api/heroku)
         */
        private WebTarget rootTarget;
        private WebTarget appsTarget;
        
        public ProviderClient(String provider, CredentialsMap credentials) {
            this.credentials = credentials;
            this.provider = provider;
            this.rootTarget = client.target(root.toString()).path(this.provider);
            this.appsTarget = rootTarget.path("applications");
            this.transformer = new PlainTransformer();
        }

        public Provider getProviderDescription() {
            Provider provider = rootTarget.request(MediaType.APPLICATION_JSON)
                    .get(Provider.class);
            return provider;
        }
        
        public Application getApplication(String name) {
            Application application;
            WebTarget appTarget = appsTarget.path(name);
            try {
                application = appTarget.request(MediaType.APPLICATION_JSON)
                        .header(Constants.Headers.CREDENTIALS, transformer.serialize(credentials))
                        .get(Application.class);
            } catch (WebApplicationException e) {
                throw new RestClientException(e.getMessage(), e, e.getResponse().getStatus());
            }
            return application;
        }
        
        public boolean createApplication(String name, InputStream is) throws IOException {

            try (FormDataMultiPart multipart = new FormDataMultiPart()) {
                
                Application application = new Application(name);
                final StreamDataBodyPart filePart = new StreamDataBodyPart(Constants.MultiPartFields.FILE, is);
                multipart
                    .field(Constants.MultiPartFields.MODEL, application, MediaType.APPLICATION_JSON_TYPE)
                    .bodyPart(filePart);

                Response response = appsTarget.request()
                        .accept(MediaType.APPLICATION_JSON_TYPE)
                        .header(Constants.Headers.CREDENTIALS, transformer.serialize(credentials))
                        .post(Entity.entity(multipart, multipart.getMediaType()));
                
                return Family.SUCCESSFUL.equals(response.getStatusInfo().getFamily());
            }
        }
    }
}
