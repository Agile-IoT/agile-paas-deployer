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
package eu.atos.paas;

import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import eu.atos.paas.Module.State;
import eu.atos.paas.PaasSession.DeployParameters;
import eu.atos.paas.PaasSession.StartStopCommand;

import static org.testng.AssertJUnit.*;

import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Base class for integration tests in library project.
 * 
 * Since tests are grouped, and the methods in this abstract class are not classified into the child group
 * (@see http://stackoverflow.com/questions/7708262/testng-test-inheritance-and-groups), all the methods here
 * belong to group "default" but not executed if initialize() have not been called from child. This
 * is a complex solution and may be changed in the future.
 */
public abstract class AbstractProviderIT {

    private boolean initialized = false;
    protected PaasSession session;
    protected DeployParameters params;
    protected static String APP_NAME = "paastest";
    protected static String APP_NAME_NOT_EXISTS = "notexists";
    protected int waitSecondsAfterMethod = 0;
    
    protected void initialize() {
        this.initialized = true;
    }
    
    @AfterClass
    public void cleanup() {
        if (session.getModule(APP_NAME) != null) {
            session.undeploy(APP_NAME);
        }
        if (session.getModule(APP_NAME_NOT_EXISTS) != null) {
            session.undeploy(APP_NAME_NOT_EXISTS);
        }
    }
    
    @Test(priority = 0, groups = "default")
    public void checkCleanState() {
        beforeMethod();
        
        if (session.getModule(APP_NAME) != null) {
            this.initialized = false;
            fail(String.format("Cannot run test: app %s exists", APP_NAME));
        }
        if (session.getModule(APP_NAME_NOT_EXISTS) != null) {
            this.initialized = false;
            fail(String.format("Cannot run test: app %s exists", APP_NAME_NOT_EXISTS));
        }
    }
    
    @Test(priority = 5, groups = "default")
    public void getNonExistentApplication() {
        beforeMethod();

        System.out.println("Running test with session " + session.getClass().getName());
        Module m = session.getModule(APP_NAME);
        assertNull(m);
    }
    
    @Test(priority = 10, groups = "default")
    public void createApplication() {
        beforeMethod();
        
        Module m = session.createApplication(APP_NAME, params);
        assertNotNull(m);
    }
    
    @Test(priority = 13, groups = "default")
    public void createExistentApplicationShouldFail() {
        beforeMethod();

        try {
            session.createApplication(APP_NAME, params);
            fail("Should have thrown AlreadyExistsException");
        } catch (AlreadyExistsException e) {
            return;
        }
    }
    
    @Test(priority = 16, groups = "default")
    public void getCreatedApplication() {
        beforeMethod();
        
        Module m = session.getModule(APP_NAME);
        assertNotNull(m);
        assertEquals(APP_NAME, m.getName());
        assertTrue(State.UNDEPLOYED.equals(m.getState()) || State.STARTED.equals(m.getState()));
    }
    
    @Test(priority = 20, groups = "default")
    public void uploadNonExistentApplicationShouldFail() {
        beforeMethod();
        
        try {
            session.updateApplication(APP_NAME_NOT_EXISTS, params);
            fail("Should have thrown NotFoundException");
        } catch (NotFoundException e) {
            return;
        }
    }
    
    @Test(priority = 25, groups = "default")
    public void uploadApplication() {
        beforeMethod();
        
        Module m = session.updateApplication(APP_NAME, params);
        assertNotNull(m);
        assertEquals(APP_NAME, m.getName());
        assertEquals(State.STARTED, m.getState());
    }
    
    @Test(priority = 28, groups = "default")
    public void getUploadedApplication() {
        beforeMethod();

        Module m = session.getModule(APP_NAME);
        assertNotNull(m);
        assertEquals(APP_NAME, m.getName());
        assertEquals(State.STARTED, m.getState());
    }
    
    @Test(priority = 30, groups = "default")
    public void stopApplication() {
        beforeMethod();

        Module m = session.getModule(APP_NAME);
        session.startStop(m, StartStopCommand.STOP);
    }
    
    @Test(priority = 35, groups = "default")
    public void getStoppedApplication() {
        beforeMethod();

        Module m = session.getModule(APP_NAME);
        assertNotNull(m);
        assertEquals(APP_NAME, m.getName());
        assertEquals(State.STOPPED, m.getState());
    }
    
    @Test(priority = 37, groups = "default")
    public void stopNonExistentApplicationShouldFail() {
        beforeMethod();

        Module m = new NonExistentModule();
        try {
            session.startStop(m, StartStopCommand.STOP);
            fail("Should have thrown NotFoundException");
        }
        catch (NotFoundException e) {
            return;
        }
    }
    
    @Test(priority = 40, groups = "default")
    public void startApplication() {
        beforeMethod();

        Module m = session.getModule(APP_NAME);
        session.startStop(m, StartStopCommand.START);
    }
    
    @Test(priority = 43, groups = "default")
    public void getStartedApplication() {
        beforeMethod();

        Module m = session.getModule(APP_NAME);
        assertNotNull(m);
        assertEquals(APP_NAME, m.getName());
        assertEquals(State.STARTED, m.getState());
    }
    
    @Test(priority = 45, groups = "default")
    public void startNonExistentApplicationShouldFail() {
        beforeMethod();
        
        Module m = new NonExistentModule();
        try {
            session.startStop(m, StartStopCommand.START);
            fail("Should have thrown NotFoundException");
        }
        catch (NotFoundException e) {
            return;
        }
    }
    
    @Test(priority = 45, groups = "default")
    public void startNonDeployedApplicationShouldFail() {
        beforeMethod();

        Module m = session.createApplication(APP_NAME_NOT_EXISTS, params);
        try {
            if (State.STARTED.equals(m.getState())) {
                /* nothing to test here */
                return;
            }
            session.startStop(m, StartStopCommand.START);
            fail("Should have thrown NotDeployedException");
        }
        catch (NotDeployedException e) {
            return;
        }
        finally {
            session.undeploy(APP_NAME_NOT_EXISTS);
        }
    }
    
    @Test(priority = 50, groups = "default")
    public void deleteApplication() {
        beforeMethod();

        session.undeploy(APP_NAME);
    }
    
    @Test(priority = 52, groups = "default")
    public void getDeletedApplication() {
        beforeMethod();

        Module m = session.getModule(APP_NAME);
        assertNull(m);
    }
    
    @Test(priority = 55, groups = "default")
    public void deleteNonExistentApplicationShouldFail() {
        beforeMethod();

        try {
            session.undeploy(APP_NAME);
            fail("Should have thrown NotFoundException");
        } catch (NotFoundException e) {
            return;
        }
    }

    /*
     * Manually called by each test method
     * Tried to use @BeforeMethod, but it brings lots of problems
     */
    private void beforeMethod() {
        if (!initialized) { 
            throw new SkipException("Not initialized");
        }
    }
    
    private static final class NonExistentModule implements Module {
        @Override
        public URL getUrl() {
            return null;
        }

        @Override
        public State getState() {
            return null;
        }

        @Override
        public List<String> getServices() {
            return null;
        }

        @Override
        public int getRunningInstances() {
            return 0;
        }

        @Override
        public String getName() {
            return APP_NAME_NOT_EXISTS;
        }

        @Override
        public Map<String, String> getEnv() {
            return null;
        }

        @Override
        public String getAppType() {
            return null;
        }
    }

}
