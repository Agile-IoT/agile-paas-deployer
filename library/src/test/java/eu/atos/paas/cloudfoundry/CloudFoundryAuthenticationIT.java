package eu.atos.paas.cloudfoundry;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import eu.atos.paas.AuthenticationException;
import eu.atos.paas.PaasClient;
import eu.atos.paas.PaasClientFactory;
import eu.atos.paas.TestConfigProperties;
import eu.atos.paas.credentials.ApiUserPasswordOrgSpaceCredentials;

import static org.testng.AssertJUnit.*;

public class CloudFoundryAuthenticationIT {

    PaasClient client; 
    
    public CloudFoundryAuthenticationIT() {
    }

    @BeforeTest
    public void initialize()
    {
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
