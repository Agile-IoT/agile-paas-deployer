package eu.atos.paas.openshift;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;
import static org.testng.AssertJUnit.fail;

import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.openshift.client.cartridge.IStandaloneCartridge;

import eu.atos.paas.Groups;
import eu.atos.paas.Module;
import eu.atos.paas.PaasClient;
import eu.atos.paas.PaasClientFactory;
import eu.atos.paas.PaasSession;
import eu.atos.paas.ServiceApp;
import eu.atos.paas.TestConfigProperties;
import eu.atos.paas.credentials.UserPasswordCredentials;
import eu.atos.paas.PaasSession.ScaleUpDownCommand;
import eu.atos.paas.PaasSession.StartStopCommand;
import eu.atos.paas.openshift2.DeployParameters;


/**
 * 
 * URL with git code examples: 
 * 		https://github.com/openshift/openshift-java-client/blob/master/src/test/resources/samples/get-api-quickstarts.json
 * 
 * URL with available cartridges:
 * 		https://github.com/openshift/openshift-java-client/blob/master/src/test/resources/samples/get-cartridges.json
 * 
 */
@Test(groups = Groups.OPENSHIFT2)
public class Openshift2IT
{

    
    // Application
    private static final String APP_NAME = "rsucasastest01";
    private static final String GIT_APP_URL = "https://github.com/OpenMEAP/openshift-openmeap-quickstart";
    private static final String SERV_NAME = "mysql-5.5";

    private URL gitAppUrl;
    
    // session
    private PaasSession session;
    
    // log
    private static Logger logger = LoggerFactory.getLogger(Openshift2IT.class);

    public Openshift2IT() throws MalformedURLException {
        
        gitAppUrl = new URL(GIT_APP_URL);
    }
    
    @BeforeTest
    public void initialize()
    {
        logger.info("### INTEGRATION TESTS > Openshift 2 ...");
        // login / connect to PaaS
        PaasClientFactory factory = new PaasClientFactory();
        PaasClient client = factory.getClient("openshift2");
        session = client.getSession(new UserPasswordCredentials(TestConfigProperties.getInstance().getOp_user(),
                                                                TestConfigProperties.getInstance().getOp_password()));
    }
    
    
    private boolean checkResult(eu.atos.paas.Module m, String exeFunc, String operation, int expectedValue, int seconds)
    {
        for (int i = 0; i < 10; i++)
        {
            try
            {
                logger.info(">> " + exeFunc + " >> " + operation + " == " + expectedValue + " ?");
                Thread.sleep(seconds*1000);
                m = session.getModule(APP_NAME);

                if (("instances".equalsIgnoreCase(operation)) && (m.getRunningInstances() == expectedValue))
                {
                    logger.info(">> " + operation + " = " + expectedValue);
                    return true;
                }
            }
            catch (Exception e)
            {
                fail(e.getMessage());
                break;
            }
        }

        return false;
    }


    @Test(priority = 10)
    public void deploy() 
    {
        logger.info("### TEST > Openshift 2 > deploy()");

        Module m = session.deploy(APP_NAME, new DeployParameters(gitAppUrl, IStandaloneCartridge.NAME_JBOSSEWS));

        assertNotNull(m);
        logger.info(">> " + String.format("name='%s',  url='%s'", m.getName(), m.getUrl()));
        assertEquals(APP_NAME, m.getName());
        
        if (!checkResult(m, "deploying / starting application", "instances", 1, 10))
            fail(APP_NAME + " not started");
        else
            assertTrue(true);
    }
    
    
    @Test (priority = 20)
    public void stop() 
    {
        logger.info("### TEST > Openshift 2 > stop()");

        eu.atos.paas.Module m = session.getModule(APP_NAME);
        session.startStop(m, StartStopCommand.STOP);
        
        if (!checkResult(m, "stopping application", "instances", 0, 2))
            fail(APP_NAME + " not stopped");
        else
            assertTrue(true);
    }
    
    
    @Test (priority = 30)
    public void start() 
    {
        logger.info("### TEST > Openshift 2 > start()");

        eu.atos.paas.Module m = session.getModule(APP_NAME);
        session.startStop(m, StartStopCommand.START);
        
        if (!checkResult(m, "starting application", "instances", 1, 5))
            fail(APP_NAME + " not started");
        else
            assertTrue(true);
    }
    
    
    @Test (priority = 40)
    public void scaleUp() 
    {
        logger.info("### TEST > Openshift 2 > scaleUp()");

        eu.atos.paas.Module m = session.getModule(APP_NAME);
        session.scaleUpDown(m, ScaleUpDownCommand.SCALE_UP_INSTANCES);
        
        if (!checkResult(m, "scaling application", "instances", 2, 5))
            fail(APP_NAME + " not scaled up");
        else
            assertTrue(true);
    }
    
    
    @Test (priority = 50)
    public void scaleDown() 
    {
        logger.info("### TEST > Openshift 2 > scaleDown()");

        eu.atos.paas.Module m = session.getModule(APP_NAME);
        session.scaleUpDown(m, ScaleUpDownCommand.SCALE_DOWN_INSTANCES);
        
        if (!checkResult(m, "scaling application", "instances", 1, 5))
            fail(APP_NAME + " not scaled down");
        else
            assertTrue(true);
    }
    
    
    @Test (priority = 60)
    public void bindToService() 
    {
        logger.info("### TEST > Openshift 2 > bindToService()");

        eu.atos.paas.Module m = session.getModule(APP_NAME);
        session.bindToService(m, new ServiceApp(SERV_NAME));
        
        m = session.getModule(APP_NAME);
        assertEquals(1, m.getServices().size());
        
        /*logger.info("### TEST > Openshift 2 > bindToService() > environment values...");
        for (Map.Entry<String, Object> entry : m.getmEnv().entrySet()) {
            logger.info("### TEST > Openshift 2 > bindToService() > " + entry.getKey() + " / " + entry.getValue().toString());
        }*/
    }

    
    @Test (priority = 70)
    public void unbindFromService() 
    {
        logger.info("### TEST > Openshift 2 > unbindFromService()");

        eu.atos.paas.Module m = session.getModule(APP_NAME);
        session.unbindFromService(m, new ServiceApp(SERV_NAME));
        
        m = session.getModule(APP_NAME);
        assertEquals(0, m.getServices().size());
    }
    
    
    @Test (priority = 80)
    public void undeploy() 
    {
        logger.info("### TEST > Openshift 2 > undeploy()");

        session.undeploy(APP_NAME);
        
        eu.atos.paas.Module m = session.getModule(APP_NAME);
        if (m != null) {
            logger.warn("### TEST > Openshift 2 > undeploy() FAILED: "+ m.getName());
            fail(APP_NAME + " still exists");
        }
    }

    
}
