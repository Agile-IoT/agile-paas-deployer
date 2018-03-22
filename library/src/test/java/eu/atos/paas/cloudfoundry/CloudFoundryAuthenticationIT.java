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
package eu.atos.paas.cloudfoundry;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import eu.atos.paas.AuthenticationException;
import eu.atos.paas.Groups;
import eu.atos.paas.PaasClient;
import eu.atos.paas.PaasClientFactory;
import eu.atos.paas.TestConfigProperties;
import eu.atos.paas.credentials.ApiUserPasswordOrgSpaceCredentials;

import static org.testng.AssertJUnit.*;

@Test(groups = Groups.CLOUDFOUNDRY)
public class CloudFoundryAuthenticationIT {

    PaasClient client; 
    
    public CloudFoundryAuthenticationIT() {
    }

    @BeforeTest
    public void initialize()
    {
        System.out.println("---" + this.getClass().getName() + "---");
        client = new PaasClientFactory().getClient("cloudfoundry");
    }
    
    @Test
    public void testRightAuthentication() {
        client.getSession(new ApiUserPasswordOrgSpaceCredentials(
                TestConfigProperties.getInstance().getCf_api(), 
                TestConfigProperties.getInstance().getCf_user(),
                TestConfigProperties.getInstance().getCf_password(), 
                TestConfigProperties.getInstance().getCf_org(), 
                TestConfigProperties.getInstance().getCf_space(), 
                TestConfigProperties.getInstance().isCf_trustSelfSignedCerts()));

    }

    @Test public void testWrongUrl() {
        
        try {
            
            client.getSession(new ApiUserPasswordOrgSpaceCredentials(
                    "http://localhost:18080", 
                    TestConfigProperties.getInstance().getCf_user(),
                    TestConfigProperties.getInstance().getCf_password(), 
                    TestConfigProperties.getInstance().getCf_org(), 
                    TestConfigProperties.getInstance().getCf_space(), 
                    TestConfigProperties.getInstance().isCf_trustSelfSignedCerts()));
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
            client.getSession(new ApiUserPasswordOrgSpaceCredentials(
                    TestConfigProperties.getInstance().getCf_api(), 
                    "wrong-user", "wrong-pwd", 
                    TestConfigProperties.getInstance().getCf_org(), 
                    TestConfigProperties.getInstance().getCf_space(), 
                    TestConfigProperties.getInstance().isCf_trustSelfSignedCerts()));
            fail("Did not throw exception");
            
        } catch (AuthenticationException e) {
            
            assertTrue(true);
            return;
        }
        fail("Did not throw AuthenticationExcepton");
    }
}
