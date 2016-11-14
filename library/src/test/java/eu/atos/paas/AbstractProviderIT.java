package eu.atos.paas;

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
    protected static final String APP_NAME = "paastest";
    protected static final String APP_NAME_NOT_EXISTS = "notexists";
    protected int waitSecondsAfterMethod = 0;
    
    protected void initialize() {
        this.initialized = true;
    }
    
    @Test(priority = 0, groups = "default")
    public void getNonExistentApplication() {
        if (!initialized) { return; }

        System.out.println("Running test with session " + session.getClass().getName());
        Module m = session.getModule(APP_NAME);
        assertNull(m);
    }
    
    @Test(priority = 10, groups = "default")
    public void createApplication() {
        if (!initialized) { return; }
        
        Module m = session.createApplication(APP_NAME, params);
        assertNotNull(m);
    }
    
    @Test(priority = 13, groups = "default")
    public void createExistentApplicationShouldFail() {
        if (!initialized) { return; }

        try {
            session.createApplication(APP_NAME, params);
            fail("Should have thrown AlreadyExistsException");
        } catch (AlreadyExistsException e) {
            return;
        }
    }
    
    @Test(priority = 16, groups = "default")
    public void getCreatedApplication() {
        if (!initialized) { return; }
        
        Module m = session.getModule(APP_NAME);
        assertNotNull(m);
        assertEquals(APP_NAME, m.getName());
        assertEquals(State.UNDEPLOYED, m.getState());
    }
    
    @Test(priority = 20, groups = "default")
    public void uploadNonExistentApplicationShouldFail() {
        if (!initialized) { return; }
        
        try {
            session.updateApplication(APP_NAME_NOT_EXISTS, params);
            fail("Should have thrown NotFoundException");
        } catch (NotFoundException e) {
            return;
        }
    }
    
    @Test(priority = 25, groups = "default")
    public void uploadApplication() {
        if (!initialized) { return; }
        
        Module m = session.updateApplication(APP_NAME, params);
        assertNotNull(m);
        assertEquals(APP_NAME, m.getName());
        assertEquals(State.STARTED, m.getState());
    }
    
    @Test(priority = 28, groups = "default")
    public void getUploadedApplication() {
        if (!initialized) { return; }

        Module m = session.getModule(APP_NAME);
        assertNotNull(m);
        assertEquals(APP_NAME, m.getName());
        assertEquals(State.STARTED, m.getState());
    }
    
    @Test(priority = 30, groups = "default")
    public void stopApplication() {
        if (!initialized) { return; }

        Module m = session.getModule(APP_NAME);
        session.startStop(m, StartStopCommand.STOP);
    }
    
    @Test(priority = 35, groups = "default")
    public void getStoppedApplication() {
        if (!initialized) { return; }

        Module m = session.getModule(APP_NAME);
        assertNotNull(m);
        assertEquals(APP_NAME, m.getName());
        assertEquals(State.STOPPED, m.getState());
    }
    
    @Test(priority = 37, groups = "default")
    public void stopNonExistentApplicationShouldFail() {
        if (!initialized) { return; }

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
        if (!initialized) { return; }

        Module m = session.getModule(APP_NAME);
        session.startStop(m, StartStopCommand.START);
    }
    
    @Test(priority = 43, groups = "default")
    public void getStartedApplication() {
        if (!initialized) { return; }

        Module m = session.getModule(APP_NAME);
        assertNotNull(m);
        assertEquals(APP_NAME, m.getName());
        assertEquals(State.STARTED, m.getState());
    }
    
    @Test(priority = 45, groups = "default")
    public void startNonExistentApplicationShouldFail() {
        if (!initialized) { return; }
        
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
        if (!initialized) { return; }

        Module m = session.createApplication(APP_NAME_NOT_EXISTS, params);
        try {
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
        if (!initialized) { return; }

        session.undeploy(APP_NAME);
    }
    
    @Test(priority = 52, groups = "default")
    public void getDeletedApplication() {
        if (!initialized) { return; }

        Module m = session.getModule(APP_NAME);
        assertNull(m);
    }
    
    @Test(priority = 55, groups = "default")
    public void deleteNonExistentApplicationShouldFail() {
        if (!initialized) { return; }

        try {
            session.undeploy(APP_NAME);
            fail("Should have thrown NotFoundException");
        } catch (NotFoundException e) {
            return;
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
