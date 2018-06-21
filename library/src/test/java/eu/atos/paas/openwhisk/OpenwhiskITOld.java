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

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertTrue;
import static org.testng.AssertJUnit.fail;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import eu.atos.deployer.faas.openwhisk.Constants;
import eu.atos.deployer.faas.openwhisk.OpenwhiskConnector;
import eu.atos.deployer.faas.openwhisk.OpenwhiskException;
import eu.atos.deployer.faas.openwhisk.model.ActionInformation;
import eu.atos.deployer.faas.openwhisk.model.ExecutionResponse;
import eu.atos.deployer.faas.openwhisk.model.ExecutionResult;


@Test(groups = "XXX", enabled = false)
public class OpenwhiskITOld  {

    private static final String OPENWHISK_APP_URL = "https://127.0.0.1:4433";
    private static final String OPENWHISK_DEFAULT_USER = "23bc46b1-71f6-4ed5-8c54-816aa4f8c502";
    private static final String OPENWHISK_DEFAULT_PASSWORD = "123zO3xZCLrMN6v2BKK1dXYFpXlPkccOFqm12CdAsMgRU4VrNZ9lyGVCGuMDGIwP";
    private static final String OPENWHISK_TEST_CODE_SIMPLE = "function main() {console.log('Test aaa'); return { hello: 'test' };}";
    private static final String OPENWHISK_TEST_CODE_WITH_PARAM = "function main(params) { return {payload:\"Hello \"+params.name}}";
    
    private static final String ACTION_NAME = "myAction";
    

    OpenwhiskConnector conn;
    
    
    @BeforeClass
    protected void initialize() {
        URL url;
        try {
            url = new URL(OPENWHISK_APP_URL);
            conn = new OpenwhiskConnector(url, OPENWHISK_DEFAULT_USER, OPENWHISK_DEFAULT_PASSWORD);
            
        } catch (MalformedURLException e) {
            fail("URL is incorrect, make sure you're using the right URL for testing");
        }        
    }
    


    @Test(enabled=false)
    public void testDeployCodeInJavaAndExecution() {
        String actionName = "JAVA_TEST_ACTION_NAME";
           Path currentRelativePath = Paths.get("src/test/resources/demo-function.jar");
         String main = "com.example.FunctionApp";
        conn.deployActionFromCode(actionName, currentRelativePath, Constants.Kind.JAVA, true, main);
        ExecutionResponse result = conn.invokeAction(actionName, null, true, true, null);
        String expectedOutput = "{\"greetings\":\"Hello! Welcome to OpenWhisk\"}";
        assertNotNull(result);
        assertNotNull(result.getResult());        
        assertEquals(result.getResult().toString(), expectedOutput);
    }


    @Test(priority = 10)
    public void testDeployCodeSimple() {
        ActionInformation response =conn.deployActionFromCode(ACTION_NAME, OPENWHISK_TEST_CODE_SIMPLE, Constants.Kind.DEFAULT, true);
        assertNull(response.getError());
    }

    @Test(priority = 15)
    public void testDeployCodeBinary() {
        ActionInformation response =conn.deployActionFromCode(ACTION_NAME, OPENWHISK_TEST_CODE_SIMPLE, Constants.Kind.DEFAULT, true);
        assertNull(response.getError());
    }

    @Test(priority = 20)
    public void testGetActionList() {
        List<ActionInformation> response =conn.getActionList(null, null);
        assertTrue(response.size() > 0);
    }

    @Test(priority = 25)
    public void testGetAction() {
        ActionInformation response =conn.getActionInformation(ACTION_NAME, true);
        assertEquals(ACTION_NAME,response.getName()); 
    }


    @Test(priority = 30)
    public void testInvokeCodeSimple() {
        ExecutionResponse er = conn.invokeAction(ACTION_NAME, null, true, true, null);
        assertTrue(er.getResult().equals("{\"hello\":\"test\"}"));
        er = conn.invokeAction(ACTION_NAME, null, true, false, null);
        assertTrue((er.getActivationId()!=null && er.getDuration()>0));
        er = conn.invokeAction(ACTION_NAME, null, false, true, null);
        assertTrue(er.getActivationId()!=null && er.getDuration()==0);
        er = conn.invokeAction(ACTION_NAME, null, false, false, null);
        assertTrue(er.getActivationId()!=null && er.getDuration()==0);
        
    }
    
    @Test(priority = 35)
    public void testRemoveCodeSimple() {
        ActionInformation response = conn.deleteAction(ACTION_NAME);
        assertNull(response.getError());
    }
    
    @Test(priority = 40)
    public void testDeployCodeWithParameter() {
        ActionInformation response =conn.deployActionFromCode(ACTION_NAME, OPENWHISK_TEST_CODE_WITH_PARAM, Constants.Kind.DEFAULT, true);
        assertNull(response.getError());
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("name", "John");
        ExecutionResponse er = conn.invokeAction(ACTION_NAME, params, true, true, null);
        assertEquals(er.getResult(),"{\"payload\":\"Hello John\"}" );
        er = conn.invokeAction(ACTION_NAME, null, true, true, null);
        assertEquals(er.getResult(), "{\"payload\":\"Hello undefined\"}");
        response = conn.deleteAction(ACTION_NAME);
        assertNull(response.getError());
    }

    @Test(priority = 45)
    public void testActivation() {
         String codeTwoLogs = "function main() {console.log('Test aaa'); console.log('Test bbb'); return { hello: 'test' };}";
         ActionInformation response= null;
         try{
             response =conn.deployActionFromCode(ACTION_NAME+"TwoLogs", codeTwoLogs, Constants.Kind.DEFAULT, true);
         }catch(OpenwhiskException e){
             System.out.println(e.getMessage());
         }
         assertNull(response.getError());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            //waiting that the action can be executed
        }
        ExecutionResponse er = conn.invokeAction(ACTION_NAME+"TwoLogs", null, true, false, null);
        List<String> logs= conn.getActivationLogs(er.getActivationId());
        assertTrue(logs.size() == 2);
        
        
         String codeTwoLogsWithError = "function main( {console.log('Test aaa'); console.log('Test bbb'); return { hello: 'test' };}";
        response =conn.deployActionFromCode(ACTION_NAME+"TwoLogs", codeTwoLogsWithError, Constants.Kind.DEFAULT, true);
        assertNull(response.getError());
        er = conn.invokeAction(ACTION_NAME+"TwoLogs", null, false, true, null);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            //waiting that the action can be executed
        }
        ExecutionResult result = conn.getActivationResult(er.getActivationId());
        assertEquals("action developer error", result.getStatus());
        
        response = conn.deleteAction(ACTION_NAME+"TwoLogs");
        assertNull(response.getError());
    }
    
    @AfterClass
    public void cleanup() {
        try{
            conn.getActionInformation(ACTION_NAME+"TwoLogs", false);
            conn.deleteAction(ACTION_NAME+"TwoLogs");
        }catch(OpenwhiskException ex){
            
        }
        try{
            conn.getActionInformation(ACTION_NAME, false);
            conn.deleteAction(ACTION_NAME);
        }catch(OpenwhiskException ex){
            
        }
        try{
            conn.getActionInformation("JAVA_TEST_ACTION_NAME", false);
            conn.deleteAction("JAVA_TEST_ACTION_NAME");
        }catch(OpenwhiskException ex){
            
        }
    }    
}
