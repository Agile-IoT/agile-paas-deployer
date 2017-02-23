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
package eu.atos.paas.heroku;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;
import static org.testng.AssertJUnit.fail;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import eu.atos.paas.Groups;
import eu.atos.paas.PaasClient;
import eu.atos.paas.PaasClientFactory;
import eu.atos.paas.PaasSession;
import eu.atos.paas.ServiceApp;
import eu.atos.paas.TestConfigProperties;
import eu.atos.paas.credentials.ApiKeyCredentials;
import eu.atos.paas.PaasSession.ScaleUpDownCommand;
import eu.atos.paas.PaasSession.StartStopCommand;
import eu.atos.paas.heroku.DeployParameters;


@Test(groups = "XXX", enabled = false)
public class HerokuOldIT
{
    
    
    // Application
    private static final String APP_NAME = TestConfigProperties.getInstance().getApp_name();

    // session
    private PaasSession session;
    
    // log
    private static Logger logger = LoggerFactory.getLogger(HerokuOldIT.class);

    
    @BeforeTest
    public void initialize()
    {
        logger.info("### INTEGRATION TESTS > Heroku ...");
        // login / connect to PaaS
        PaasClientFactory factory = new PaasClientFactory();
        PaasClient client = factory.getClient("heroku");
        session = client.getSession(new ApiKeyCredentials(TestConfigProperties.getInstance().getHeroku_apiKey()));
    }
    

    /**
     * 
     * @param m
     * @param exeFunc
     * @param operation
     * @param expectedValue
     * @param seconds
     * @return
     */
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
    

    @Test
    public void deploy() 
    {
        logger.info("### TEST > Heroku > deploy()");

        String path = this.getClass().getResource("/SampleApp1.war").getFile();
        eu.atos.paas.Module m = session.deploy(APP_NAME, new DeployParameters(path));

        assertNotNull(m);
        logger.info(">> " + String.format("name='%s',  url='%s'", m.getName(), m.getUrl()));
        assertEquals(APP_NAME, m.getName());
        
        if (!checkResult(m, "deploying / starting application", "instances", 1, 10))
            fail(APP_NAME + " not started");
        else
            assertTrue(true);
    }
    
    
    @Test (dependsOnMethods={"deploy"})
    public void stop() 
    {
        logger.info("### TEST > Heroku > stop()");

        eu.atos.paas.Module m = session.getModule(APP_NAME);
        session.startStop(m, StartStopCommand.STOP);
        
        if (!checkResult(m, "stopping application", "instances", 0, 2))
            fail(APP_NAME + " not stopped");
        else
            assertTrue(true);
    }
    
    
    @Test (dependsOnMethods={"stop"})
    public void start() 
    {
        logger.info("### TEST > Heroku > start()");

         eu.atos.paas.Module m = session.getModule(APP_NAME);
         session.startStop(m, StartStopCommand.START);
         
         if (!checkResult(m, "starting application", "instances", 1, 5))
            fail(APP_NAME + " not started");
         else
            assertTrue(true);
    }
    
   
    @Test (dependsOnMethods={"start"})
    public void scaleUp() 
    {
        logger.info("### TEST > Heroku > scaleUp()");

        eu.atos.paas.Module m = session.getModule(APP_NAME);
        session.scaleUpDown(m, ScaleUpDownCommand.SCALE_UP_INSTANCES);
        
        if (!checkResult(m, "scaling application", "instances", 2, 5))
            fail(APP_NAME + " not scaled up");
        else
            assertTrue(true);
    }
    
    
    @Test (dependsOnMethods={"scaleUp"})
    public void scaleDown() 
    {
        logger.info("### TEST > Heroku > scaleDown()");

        eu.atos.paas.Module m = session.getModule(APP_NAME);
        session.scaleUpDown(m, ScaleUpDownCommand.SCALE_DOWN_INSTANCES);
        
        if (!checkResult(m, "scaling application", "instances", 1, 5))
            fail(APP_NAME + " not scaled down");
        else
            assertTrue(true);
    }
    
    
    @Test (dependsOnMethods={"scaleDown"}) 
    public void bindToService() 
    {
        logger.info("### TEST > Heroku > bindToService()");
        eu.atos.paas.Module m = session.getModule(APP_NAME);
        ServiceApp service = new ServiceApp("cleardb:ignite");
        
        session.bindToService(m, service);
        
        m = session.getModule(APP_NAME);
        assertEquals(1, m.getServices().size());
        
        logger.info("### TEST > Heroku > bindToService() > environment values...");
        for (Map.Entry<String, String> entry : m.getEnv().entrySet()) {
            logger.info("### TEST > Heroku > bindToService() > " + entry.getKey() + " / " + entry.getValue().toString());
        }
    }

    
    @Test (dependsOnMethods={"bindToService"})
    public void unbindFromService() 
    {
        logger.info("### TEST > Heroku > unbindFromService()");
        eu.atos.paas.Module m = session.getModule(APP_NAME);
        ServiceApp service = new ServiceApp("cleardb:ignite");
        
        session.unbindFromService(m, service);
        
        m = session.getModule(APP_NAME);
        assertEquals(0, m.getServices().size());
    }
    
    
    @Test (dependsOnMethods={"unbindFromService"})
    public void undeploy() 
    {
        logger.info("### TEST > Heroku > undeploy()");

        session.undeploy(APP_NAME);
        
        eu.atos.paas.Module m = session.getModule(APP_NAME);
        if (m != null) {
            System.out.println("### TEST > Heroku > undeploy() > " + m.getName());
            fail(APP_NAME + " still exists");
        }
    }

    
}
