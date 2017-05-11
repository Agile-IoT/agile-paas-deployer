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
package eu.atos.paas.client;

import org.testng.annotations.Test;

import eu.atos.paas.Groups;
import eu.atos.paas.TestConstants;
import eu.atos.paas.client.RestClient.ProviderClient;
import eu.atos.paas.credentials.UserPasswordCredentials;
import eu.atos.paas.data.Application;
import eu.atos.paas.data.ApplicationToCreate;
import eu.atos.paas.data.CredentialsMap;
import eu.atos.paas.data.Provider;
import eu.atos.paas.resources.Constants;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

import javax.ws.rs.core.Response.Status;

import org.testng.annotations.BeforeClass;
import static org.testng.AssertJUnit.*;

@Test(groups = Groups.DUMMY)
public class RestClientIT {
    
    private static final Map<String, String> EMPTY_PROPERTIES = Collections.<String, String>emptyMap();
    
    RestClient client;
    ProviderClient provider;
    CredentialsMap credentials;
    
    @BeforeClass
    public void beforeClass() throws MalformedURLException {
        
        client = new RestClient(TestConstants.SERVER_URL);
        credentials = CredentialsMap.builder()
                .item(UserPasswordCredentials.USER, "user")
                .item(UserPasswordCredentials.PASSWORD, "password")
                .build();
        provider = client.getProvider("dummy", credentials);
    }

    @Test(priority = 1)
    public void testAuthentication() {
        /*
         * Just check if authorization do not fail
         */
        provider.getApplications();
    }
    
    @Test(priority = 1)
    public void testWrongAuthentication() {
        
        CredentialsMap wrongCredentials = CredentialsMap.builder()
                .item(UserPasswordCredentials.USER, "user")
                .item(UserPasswordCredentials.PASSWORD, "wrong-password")
                .build();
        ProviderClient provider = client.getProvider("dummy", wrongCredentials);
        try {
            provider.getApplications();
            fail("Did not throw exception");
        } catch (RestClientException e) {
            assertEquals(Status.UNAUTHORIZED.getStatusCode(), e.getError().getCode());
        }
    }
    
    @Test(priority = 5)
    public void testSetWrongApiVersion() {
        
        try {
            
            ProviderClient provider = client.getProvider("dummy", "donotexists", credentials);
            provider.getApplications();
        }
        catch (RestClientException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getError().getCode());
            return;
        }
        
    }

    @Test(priority = 5)
    public void testSetApiVersion() {
        ProviderClient provider = client.getProvider("dummy", "v1", credentials);
        provider.getApplications();
    }
    
    @Test(priority = 10)
    public void getProviderDescription() {
        Provider p = provider.getProviderDescription();
        
        assertEquals(Constants.Providers.DUMMY, p.getName());
    }
    
    @Test(priority = 20)
    public void createApplicationWithArtifact() throws IOException {
        
        String appName = "test";
        ApplicationToCreate appToCreate = new ApplicationToCreate(
                appName, RestClient.class.getResourceAsStream("/SampleApp1.war"), "", EMPTY_PROPERTIES);
        Application app1 = provider.createApplication(appToCreate);
        assertNotNull(app1);
        assertEquals(appName, app1.getName());
        Application app2 = provider.getApplication(appName);
        assertEquals(appName, app2.getName());
        
        assertTrue(!app1.getUrl().toString().isEmpty());
        assertEquals(app1.getUrl(), app2.getUrl());
    }
    
    @Test(priority = 21)
    public void createApplicationWithGitUrl() throws IOException {
        
        String appName = "test2";
        ApplicationToCreate appToCreate = new ApplicationToCreate(
                appName, new URL("https://github.com/octocat/Hello-World.git"), "Java", EMPTY_PROPERTIES);
        Application app1 = provider.createApplication(appToCreate);
        assertNotNull(app1);
        assertEquals(appName, app1.getName());
        Application app2 = provider.getApplication(appName);
        assertEquals(appName, app2.getName());
        
        assertTrue(!app1.getUrl().toString().isEmpty());
        assertEquals(app1.getUrl(), app2.getUrl());
    }

    @Test(priority = 10)
    public void getApplicationShouldFail() {
        
        try {
            provider.getApplication("noexiste");
            /* fails below */
        }
        catch (RestClientException e) {
            assertEquals(Status.NOT_FOUND.getStatusCode(), e.getError().getCode());
            return;
        }
        fail("Should have failed with 404");
    }
}
