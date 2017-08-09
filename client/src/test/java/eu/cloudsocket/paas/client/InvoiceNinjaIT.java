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
package eu.cloudsocket.paas.client;

import static eu.atos.paas.credentials.ApiKeyCredentials.API_KEY;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import eu.atos.paas.Groups;
import eu.atos.paas.TestConfigProperties;
import eu.atos.paas.TestConstants;
import eu.atos.paas.client.RestClient;
import eu.atos.paas.client.RestClient.ProviderClient;
import eu.atos.paas.data.Application;
import eu.atos.paas.data.ApplicationToCreate;
import eu.atos.paas.data.CredentialsMap;

@Test(groups = Groups.HEROKU, enabled = false)
public class InvoiceNinjaIT {

    private static final Map<String, String> EMPTY_PROPERTIES = Collections.<String, String>emptyMap();
    private static final String APP_NAME = "daniel-invoiceninja";
    RestClient client;
    ProviderClient provider;
    
    @BeforeClass
    public void beforeClass() throws MalformedURLException {
        
        client = new RestClient(TestConstants.SERVER_URL);
        CredentialsMap credentials = CredentialsMap.builder()
                .item(API_KEY, TestConfigProperties.getInstance().getHeroku_apiKey())
                .build();
        provider = client.getProvider("heroku", credentials);
    }
    
    public void createApplication() throws IOException {
        
        ApplicationToCreate appToCreate = new ApplicationToCreate(
                APP_NAME, 
                new URL("https://github.com/seybi87/cs-invoice-ninja-source"),
                "PhP",
                EMPTY_PROPERTIES);
        Application createdApp = provider.createApplication(appToCreate);
        assertNotNull(createdApp);
        Application app = provider.getApplication(APP_NAME);
        assertEquals(APP_NAME, app.getName());
    }
    
}
