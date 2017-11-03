/**
 * Copyright 2017 Atos
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
package eu.atos.paas.openshift3;

import static org.testng.AssertJUnit.assertTrue;
import static org.testng.AssertJUnit.fail;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import eu.atos.paas.AuthenticationException;
import eu.atos.paas.Groups;
import eu.atos.paas.PaasClient;
import eu.atos.paas.PaasClientFactory;
import eu.atos.paas.TestConfigProperties;
import eu.atos.paas.credentials.ApiTokenCredentials;
import eu.atos.paas.credentials.ApiUserPasswordCredentials;
import eu.atos.paas.credentials.UserPasswordCredentials;

@Test(groups = Groups.OPENSHIFT3)
public class OpenShift3AutenticationIT {

    PaasClient client;
    
    @BeforeTest
    public void initialize()
    {
        System.out.println("---" + this.getClass().getName() + "---");
        client = new PaasClientFactory().getClient("openshift3");
    }
    
    @Test
    public void testRightAuthenticationWithUserPwd() {
        client.getSession(new ApiUserPasswordCredentials(
                TestConfigProperties.getInstance().getOp3_api(), 
                TestConfigProperties.getInstance().getOp3_user(),
                TestConfigProperties.getInstance().getOp3_password()));

    }

    @Test
    public void testRightAuthenticationWithToken() {
        client.getSession(new ApiTokenCredentials(
                TestConfigProperties.getInstance().getOp3_api(), 
                TestConfigProperties.getInstance().getOp3_token()));

    }

    @Test 
    public void testWrongUrl() {
        
        try {
            
            client.getSession(new ApiUserPasswordCredentials(
                    "http://localhost:18443", 
                    TestConfigProperties.getInstance().getOp3_user(),
                    TestConfigProperties.getInstance().getOp3_password()));
            fail("Did not throw exception");
            
        } catch (AuthenticationException e) {
            
            assertTrue(true);
            return;
        }
        fail("Did not throw AuthenticationExcepton");
    }
    
    @Test
    public void testWrongAuthentication() {
        
        try {
            client.getSession(new ApiUserPasswordCredentials(
                    TestConfigProperties.getInstance().getOp3_api(), 
                    "wrong-user", ""));
            /*
             * Empty password because minishift creates users on the fly if pwd is not empty.
             */
            fail("Did not throw exception");
            
        } catch (AuthenticationException e) {
            
            assertTrue(true);
            return;
        }
        fail("Did not throw AuthenticationExcepton");
    }
    
    @Test
    public void testWrongCredentialType() {
        try {
            
            client.getSession(new UserPasswordCredentials("don't care", "don't care"));
            fail("Did not throw UnsupportedOperationException");
            
        } catch (UnsupportedOperationException e) {
            
            assertTrue(true);
            return;
        }
    }
}
