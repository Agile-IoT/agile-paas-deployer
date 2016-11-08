package eu.atos.paas.client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.openshift.client.cartridge.IStandaloneCartridge;

import eu.atos.paas.TestConfigProperties;
import eu.atos.paas.client.RestClient.ProviderClient;
import eu.atos.paas.data.Application;
import eu.atos.paas.data.ApplicationToCreate;
import eu.atos.paas.data.CredentialsMap;

import static eu.atos.paas.credentials.UserPasswordCredentials.*;
import static org.testng.AssertJUnit.*;

public class RestClientOpenShift2IT {

        private static final String APP_NAME = "paaslibraryexample";
        RestClient client;
        ProviderClient provider;
        
        @BeforeClass
        public void beforeClass() throws MalformedURLException {
            
            client = new RestClient(new URL("http://localhost:8080/api"));
            CredentialsMap credentials = CredentialsMap.builder()
                    .item(USER, TestConfigProperties.getInstance().getOp_user())
                    .item(PASSWORD, TestConfigProperties.getInstance().getOp_password())
                    .build();
            provider = client.getProvider("openshift", credentials);
        }
        
        @Test
        public void createApplication() throws IOException {
            
            ApplicationToCreate appToCreate = new ApplicationToCreate(
                    APP_NAME, 
                    new URL("https://github.com/OpenMEAP/openshift-openmeap-quickstart"),
                    IStandaloneCartridge.NAME_JBOSSEWS);
            Application createdApp = provider.createApplication(appToCreate);
            assertNotNull(createdApp);
            Application app = provider.getApplication(APP_NAME);
            assertEquals(APP_NAME, app.getName());
        }
        
}
