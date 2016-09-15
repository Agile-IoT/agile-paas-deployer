package eu.seaclouds.paas;


public interface PaasSession {
    
    
    public interface DeployParameters {
        String getPath();
        String getBuildpackUrl();
        String getCartridge();
    }

    
    public enum StartStopCommand {
        START,
        STOP
    }
    
    
    public enum ScaleUpDownCommand {
        SCALE_UP_INSTANCES,
        SCALE_DOWN_INSTANCES,
        SCALE_UP_MEMORY,
        SCALE_DOWN_MEMORY
    }
    
    
    public enum ScaleCommand {
        SCALE_INSTANCES,
        SCALE_MEMORY,
        SCALE_DISK
    }

    
    /**
     * 
     * @param moduleName
     * @param params
     * @return
     * @throws PaasException
     */
    Module deploy(String moduleName, PaasSession.DeployParameters params) throws PaasException;
    
    
    /**
     * 
     * @param moduleName
     * @throws PaasException
     */
    void undeploy(String moduleName) throws PaasException;
    
    
    /**
     * 
     * @param module
     * @param command
     * @throws PaasException
     */
    void startStop(Module module, PaasSession.StartStopCommand command) throws PaasException, UnsupportedOperationException;
    
    
    /**
     * scale instances / memory of applications
     * 1. SCALE_UP_INSTANCES adds one more instance to app
     * 2. SCALE_DOWN_INSTANCES removes one instance if running instances > 1
     * 3. SCALE_UP_MEMORY
     * 4. SCALE_DOWN_MEMORY
     * 
     * @param module
     * @param command
     * @throws PaasException
     */
    void scaleUpDown(Module module, PaasSession.ScaleUpDownCommand command) throws PaasException, UnsupportedOperationException;
    
    
    /**
     * scale instances / memory of applications
     * 1. SCALE_INSTANCES sets the number of instances to 'scale_value'
     * 2. SCALE_MEMORY sets the RAM of app to 'scale_value' (in MB)
     * 3. SCALE_DISK sets the disk space value to 'scale_value' (in MB)
     * 
     * @param module
     * @param command
     * @param scale_value
     * @throws PaasException
     */
    void scale(Module module, PaasSession.ScaleCommand command, int scale_value) throws PaasException, UnsupportedOperationException;
    
    
    /**
     * 
     * @param module
     * @param service
     * @throws PaasException
     */
    void bindToService(Module module, ServiceApp service) throws PaasException;
    
    
    /**
     * 
     * @param module
     * @param service
     * @throws PaasException
     */
    void unbindFromService(Module module, ServiceApp service) throws PaasException;
    
    
    /**
     * 
     * @param moduleName
     * @return
     * @throws PaasException
     */
    Module getModule(String moduleName) throws PaasException;

    
}