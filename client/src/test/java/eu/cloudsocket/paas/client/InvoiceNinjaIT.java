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
