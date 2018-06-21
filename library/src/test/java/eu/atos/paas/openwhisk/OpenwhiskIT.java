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
package eu.atos.paas.openwhisk;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import eu.atos.deployer.faas.openwhisk.Constants;
import eu.atos.deployer.faas.openwhisk.model.ExecutionResponse;
import eu.atos.faas.FaasSession;
import eu.atos.faas.FaasSession.ExecutionParameters;
import eu.atos.paas.AbstractProviderIT;
import eu.atos.paas.DeployParametersImpl;
import eu.atos.paas.Groups;
import eu.atos.paas.Module;
import eu.atos.paas.PaasClient;
import eu.atos.paas.PaasClientFactory;
import eu.atos.paas.PaasSession.DeployParameters.Properties;
import eu.atos.paas.PaasSession.StartStopCommand;
import eu.atos.paas.TestConfigProperties;
import eu.atos.paas.credentials.ApiUserPasswordCredentials;
import static org.testng.AssertJUnit.*;

@Test(groups = Groups.OPENWHISK)
public class OpenwhiskIT extends AbstractProviderIT {
    
    private static final String JAVA_TEST_ACTION_NAME = "JAVA_TEST_ACTION_NAME";
    private static final String NODEJS_TEST_ACTION_NAME = "NODEJS_TEST_ACTION_NAME";

    @BeforeClass
    protected void initialize() {
        System.out.println("---" + this.getClass().getName() + "---");
        
        PaasClient client = new PaasClientFactory().getClient("openwhisk");
        
        session = client.getSession(new ApiUserPasswordCredentials(
                TestConfigProperties.getInstance().getOw_api(), 
                TestConfigProperties.getInstance().getOw_user(),
                TestConfigProperties.getInstance().getOw_password()));
        
           Path currentRelativePath = Paths.get("src/test/resources/demo-function.jar");
        params = DeployParametersImpl.Builder.fromPath(currentRelativePath.toString())
                .property(Properties.LANGUAGE, Constants.Kind.JAVA)
                .property(Properties.MAIN, "com.example.FunctionApp")
                .build();
        super.initialize();
        
    }
    @Override
    public void stopApplication() {
        beforeMethod();

        Module m = session.getModule(APP_NAME);
        try{
            session.startStop(m, StartStopCommand.STOP);
            fail("startStop must return an UnsupportedOperationException");
        }catch(UnsupportedOperationException ex){
            
        }
    }
    
    @Override
    @AfterClass
    public void cleanup() {
        super.cleanup();
        if (session.getModule(JAVA_TEST_ACTION_NAME) != null) {
            session.undeploy(JAVA_TEST_ACTION_NAME);
        }
        if (session.getModule(NODEJS_TEST_ACTION_NAME) != null) {
            session.undeploy(NODEJS_TEST_ACTION_NAME);
        }
    }
    
    @Override
    public void getStoppedApplication(){
        //we do not have stopped state in openwhisk, we cannot perform this execution
    }
    
    @Override
    public void stopNonExistentApplicationShouldFail() {
    }
    
    @Override
    public void startApplication() {
    }
    
    @Override
    public void startNonExistentApplicationShouldFail() {
    }

    @Override
    public void startNonDeployedApplicationShouldFail() {
    }
    
    @Test(priority = 70, groups = "default")
    public void executeTestJava() {
        FaasSession faasSession = (FaasSession)session;
           Path currentRelativePath = Paths.get("src/test/resources/demo-function.jar");
        this.params = DeployParametersImpl.Builder.fromPath(currentRelativePath.toString())
                .property(Properties.LANGUAGE, Constants.Kind.JAVA)
                .property(Properties.MAIN, "com.example.FunctionApp")
                .build();
        session.deploy(JAVA_TEST_ACTION_NAME, params);
        HashMap<String, Object> extraParams = new HashMap<String, Object>();
        extraParams.put(ExecutionParameters.Properties.SYNCHRONOUS, true);
        extraParams.put(ExecutionParameters.Properties.RESULT, true);
        ExecutionResponse executionResult = (ExecutionResponse)faasSession.execute(JAVA_TEST_ACTION_NAME, null, extraParams);
        System.out.println(executionResult.getResult()); 
        assertNotNull(executionResult);
        assertNotNull(executionResult.getResult());
        String expectedOutput = "{\"greetings\":\"Hello! Welcome to OpenWhisk\"}";
        assertEquals(executionResult.getResult().toString(), expectedOutput);
    }

    private static final String OPENWHISK_TEST_CODE_WITH_PARAM = "function main(params) { return {payload:\"Hello \"+params.name}}";
    @Test(priority = 71, groups = "default")
    public void executeTestNodeJS() {
        FaasSession faasSession = (FaasSession)session;
           
        this.params = DeployParametersImpl.Builder.fromCode(OPENWHISK_TEST_CODE_WITH_PARAM)
                .property(Properties.LANGUAGE, Constants.Kind.DEFAULT)
                .build();
        session.deploy(NODEJS_TEST_ACTION_NAME, params);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("name", "John");
        HashMap<String, Object> extraParams = new HashMap<String, Object>();
        extraParams.put(ExecutionParameters.Properties.SYNCHRONOUS, true);
        extraParams.put(ExecutionParameters.Properties.RESULT, true);
        ExecutionResponse executionResult = (ExecutionResponse)faasSession.execute(NODEJS_TEST_ACTION_NAME, params, extraParams);
        assertNotNull(executionResult);
        assertNotNull(executionResult.getResult());
        assertEquals(executionResult.getResult(),"{\"payload\":\"Hello John\"}" );
        executionResult = (ExecutionResponse)faasSession.execute(NODEJS_TEST_ACTION_NAME, null, extraParams);
        assertNotNull(executionResult);
        assertNotNull(executionResult.getResult());        
        assertEquals(executionResult.getResult(), "{\"payload\":\"Hello undefined\"}");
    }

    @Test(priority = 72, groups = "default")
    public void executeTestNodeJSFromFile() {
        FaasSession faasSession = (FaasSession)session;
        String actionName = NODEJS_TEST_ACTION_NAME + "_";
        
        String path = this.getClass().getResource("/demo-function.js").getFile();
        /*
         * HACK: The initial "/" in windows avoids finding the file
         */
        if (path.startsWith("/C:")) {
            path = path.substring(1);
        }
        this.params = DeployParametersImpl.Builder.fromPath(path)
                .property(Properties.LANGUAGE, Constants.Kind.DEFAULT)
                .build();
        session.deploy(actionName, params);
        
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("name", "Will");
        HashMap<String, Object> extraParams = new HashMap<String, Object>();
        extraParams.put(ExecutionParameters.Properties.SYNCHRONOUS, true);
        extraParams.put(ExecutionParameters.Properties.RESULT, true);
        ExecutionResponse executionResult = (ExecutionResponse)faasSession.execute(actionName, params, extraParams);
        assertNotNull(executionResult);
        assertNotNull(executionResult.getResult());
        assertEquals(executionResult.getResult(),"{\"payload\":\"Hello Will\"}" );
        session.undeploy(actionName);
    }    
}