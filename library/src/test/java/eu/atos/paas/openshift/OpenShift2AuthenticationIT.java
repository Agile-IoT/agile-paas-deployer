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
package eu.atos.paas.openshift;

import static org.testng.AssertJUnit.assertTrue;
import static org.testng.AssertJUnit.fail;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import eu.atos.paas.AuthenticationException;
import eu.atos.paas.Groups;
import eu.atos.paas.PaasClient;
import eu.atos.paas.TestConfigProperties;
import eu.atos.paas.credentials.ApiUserPasswordCredentials;
import eu.atos.paas.openshift2.Openshift2Client;

@Test(groups = Groups.OPENSHIFT2)
public class OpenShift2AuthenticationIT {

    PaasClient client;
    
    public OpenShift2AuthenticationIT() {
    }

    @BeforeTest
    public void initialize()
    {
        client = new Openshift2Client();
    }
    
    @Test
    public void testRightAuthentication() {
        client.getSession(new ApiUserPasswordCredentials(
                TestConfigProperties.getInstance().getOp_api(),
                TestConfigProperties.getInstance().getOp_user(), 
                TestConfigProperties.getInstance().getOp_password()));

    }

    @Test
    public void testWrongAuthentication() {
        
        try {
            client.getSession(new ApiUserPasswordCredentials(
                    TestConfigProperties.getInstance().getOp_api(), "wrong-user", "wrong-password"));
            fail("Did not throw exception");
            
        } catch (AuthenticationException e) {
            
            assertTrue(true);
            return;
        }
    }
    
    @Test
    public void testWrongApi() {
        
        try {
            
            client.getSession(new ApiUserPasswordCredentials("", "user", "password"));
            fail("Did not throw exception");
            
        } catch (AuthenticationException e) {
            
            assertTrue(true);
        }
    }
}
