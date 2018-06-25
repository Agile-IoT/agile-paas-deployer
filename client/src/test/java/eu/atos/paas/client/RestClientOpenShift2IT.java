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
import java.net.MalformedURLException;
import java.net.URL;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import eu.atos.paas.Groups;
import eu.atos.paas.TestConfigProperties;
import eu.atos.paas.TestConstants;
import eu.atos.paas.client.RestClient.ProviderClient;
import eu.atos.paas.data.Application;
import eu.atos.paas.data.ApplicationToCreate;
import eu.atos.paas.data.CredentialsMap;

import static eu.atos.paas.credentials.ApiUserPasswordCredentials.*;
import static org.testng.AssertJUnit.*;

@Test(groups = Groups.OPENSHIFT2)
public class RestClientOpenShift2IT {

        private static final String APP_NAME = "paaslibraryexample";
        RestClient client;
        ProviderClient provider;
        
        @BeforeClass
        public void beforeClass() throws MalformedURLException {
            
            client = new RestClient(TestConstants.SERVER_URL);
            CredentialsMap credentials = CredentialsMap.builder()
                    .item(API, TestConfigProperties.getInstance().getOp_api())
                    .item(USER, TestConfigProperties.getInstance().getOp_user())
                    .item(PASSWORD, TestConfigProperties.getInstance().getOp_password())
                    .build();
            provider = client.getProvider("openshift", credentials);
        }
        
        @Test
        public void createApplication() throws IOException {
            
            ApplicationToCreate appToCreate = new ApplicationToCreate.Builder(
                        APP_NAME, new URL("https://github.com/OpenMEAP/openshift-openmeap-quickstart"))
                    .programmingLanguage("Java")
                    .build();
            
            Application createdApp = provider.createApplication(appToCreate);
            assertNotNull(createdApp);
            Application app = provider.getApplication(APP_NAME);
            assertEquals(APP_NAME, app.getName());
        }
        
}
