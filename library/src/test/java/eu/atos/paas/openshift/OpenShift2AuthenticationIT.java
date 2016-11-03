package eu.atos.paas.openshift;

import static org.testng.AssertJUnit.assertTrue;
import static org.testng.AssertJUnit.fail;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import eu.atos.paas.AuthenticationException;
import eu.atos.paas.PaasClient;
import eu.atos.paas.PaasClientFactory;
import eu.atos.paas.TestConfigProperties;
import eu.atos.paas.credentials.UserPasswordCredentials;

public class OpenShift2AuthenticationIT {

    PaasClient client;
    
    public OpenShift2AuthenticationIT() {
        // TODO Auto-generated constructor stub
    }

    @BeforeTest
    public void initialize()
    {
        client = new PaasClientFactory().getClient("openshift2");
    }
    
    @Test
    public void testRightAuthentication() {
        client.getSession(new UserPasswordCredentials(
                TestConfigProperties.getInstance().getOp_user(), 
                TestConfigProperties.getInstance().getOp_password()));

    }

    @Test
    public void testWrongAuthentication() {
        
        try {
            client.getSession(new UserPasswordCredentials("wrong-user", "wrong-password"));
            fail("Did not throw exception");
            
        } catch (AuthenticationException e) {
            
            assertTrue(true);
            return;
        }
        fail("Did not throw AuthenticationExcepton");
    }
    
}
