package eu.atos.paas.openshift;

import static org.testng.AssertJUnit.assertTrue;
import static org.testng.AssertJUnit.fail;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import eu.atos.paas.AuthenticationException;
import eu.atos.paas.Groups;
import eu.atos.paas.PaasClient;
import eu.atos.paas.TestConfigProperties;
import eu.atos.paas.credentials.UserPasswordCredentials;
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
    }
    
}
