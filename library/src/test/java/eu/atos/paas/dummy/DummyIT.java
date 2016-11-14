package eu.atos.paas.dummy;

import java.net.URL;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import eu.atos.paas.AbstractProviderIT;
import eu.atos.paas.Groups;
import eu.atos.paas.PaasSession.DeployParameters;
import eu.atos.paas.credentials.Credentials;
import eu.atos.paas.credentials.UserPasswordCredentials;

@Test(groups = Groups.DUMMY)
public class DummyIT extends AbstractProviderIT {

    @BeforeClass
    public void initialize() {
        System.out.println("---" + this.getClass().getName() + "---");
        super.initialize();
        
        Credentials credentials = new UserPasswordCredentials(DummyClient.USER, DummyClient.PASSWORD);
        this.session = new DummyClient().getSession(credentials);
        
        this.params = new DeployParameters() {
            
            @Override
            public String getPath() {
                return "";
            }
            
            @Override
            public URL getGitUrl() {
                return null;
            }
            
            @Override
            public String getCartridge() {
                return "";
            }
            
            @Override
            public String getBuildpackUrl() {
                return "";
            }
        };
    }
}
