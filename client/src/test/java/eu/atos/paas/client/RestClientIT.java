package eu.atos.paas.client;

import org.testng.annotations.Test;

import eu.atos.paas.client.RestClient.ProviderClient;
import eu.atos.paas.credentials.UserPasswordCredentials;
import eu.atos.paas.data.Application;
import eu.atos.paas.data.CredentialsMap;
import eu.atos.paas.data.Provider;
import eu.atos.paas.resources.Constants;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.ws.rs.core.Response.Status;

import org.testng.annotations.BeforeClass;
import static org.testng.AssertJUnit.*;

public class RestClientIT {
    
    RestClient client;
    ProviderClient provider;
    
    @BeforeClass
    public void beforeClass() throws MalformedURLException {
        
        client = new RestClient(new URL("http://localhost:8080/api"));
        CredentialsMap credentials = CredentialsMap.builder()
                .item(UserPasswordCredentials.USER, "admin")
                .item(UserPasswordCredentials.PASSWORD, "pwd")
                .build();
        provider = client.getProvider("dummy", credentials);
    }

    @Test
    public void getProviderDescription() {
        Provider p = provider.getProviderDescription();
        
        assertEquals(Constants.Providers.DUMMY, p.getName());
    }
    
    @Test
    public void createApplication() throws IOException {
        
        boolean result = provider.createApplication("test", RestClient.class.getResourceAsStream("/SampleApp1.war"));
        assertTrue(result);
        Application app = provider.getApplication("test");
        assertEquals("test", app.getName());
    }
    
    @Test
    public void getApplicationShouldFail() {
        
        try {
            provider.getApplication("noexiste");
            /* fails below */
        }
        catch (RestClientException e) {
            assertEquals(Status.NOT_FOUND.getStatusCode(), e.getStatus());
            return;
        }
        fail("Should have failed with 404");
    }
}
