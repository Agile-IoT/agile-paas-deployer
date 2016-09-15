package eu.seaclouds.paas.cloudfoundry;

import java.util.Map;
import org.cloudfoundry.client.lib.domain.CloudApplication;
import org.cloudfoundry.client.lib.domain.CloudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.seaclouds.paas.Module;
import eu.seaclouds.paas.PaasException;
import eu.seaclouds.paas.PaasSession;
import eu.seaclouds.paas.ServiceApp;


/**
 * 
 * @author 
 *
 */
public class CloudFoundrySession implements PaasSession {

    
    private static Logger logger = LoggerFactory.getLogger(CloudFoundrySession.class);
    // paas connector
    private CloudFoundryConnector connector;
    
    
    /**
     * 
     * Constructor
     * @param connector
     */
    public CloudFoundrySession(CloudFoundryConnector connector) {
        this.connector = connector;
    }

    
    @Override
    public Module deploy(String moduleName, DeployParameters params) throws PaasException
    {
        logger.info("DEPLOY({})", moduleName);
        
        if (!connector.deployApp(moduleName, params.getPath(), params.getBuildpackUrl()))
        {
            throw new PaasException("Application not deployed");
        }

        return getModule(moduleName);
    }

    
    @Override
    public void undeploy(String moduleName) throws PaasException
    {
        logger.info("UNDEPLOY({})", moduleName);
        connector.deleteApp(moduleName);
    }

    
    @Override
    public void startStop(Module module, StartStopCommand command) throws PaasException, UnsupportedOperationException
    {
        logger.info(command.name() + "({})", module.getName());
        switch (command)
        {
            case START:
                connector.getConnectedClient().startApplication(module.getName());
                break;
                
            case STOP:
                connector.getConnectedClient().stopApplication(module.getName());
                break;
                
            default:
                throw new UnsupportedOperationException(command.name() + " command not supported (Cloud Foundry)");
        }
    }


    @Override
    public void scaleUpDown(Module module, ScaleUpDownCommand command) throws PaasException, UnsupportedOperationException
    {
        logger.info(command.name() + "({})", module.getName());
        switch (command)
        {
            case SCALE_UP_INSTANCES:
                connector.getConnectedClient().updateApplicationInstances(module.getName(), module.getRunningInstances() + 1);
                break;
                
            case SCALE_DOWN_INSTANCES:
                if (module.getRunningInstances() > 1) {
                    connector.getConnectedClient().updateApplicationInstances(module.getName(), module.getRunningInstances() - 1);
                }
                break;
                
            case SCALE_UP_MEMORY:
                break;
                
            case SCALE_DOWN_MEMORY:
                break;
                
            default:
                throw new UnsupportedOperationException(command.name() + " command not supported (Cloud Foundry)");
        }
    }
    
    
    @Override
    public void scale(Module module, ScaleCommand command, int scale_value) throws PaasException, UnsupportedOperationException
    {
        logger.info(command.name() + "({})", module.getName());
        switch (command)
        {
            case SCALE_INSTANCES:
                connector.getConnectedClient().updateApplicationInstances(module.getName(), scale_value);
                break;
                
            case SCALE_MEMORY:
                connector.getConnectedClient().updateApplicationMemory(module.getName(), scale_value);
                break;
                
            case SCALE_DISK:
                connector.getConnectedClient().updateApplicationDiskQuota(module.getName(), scale_value);
                break;
                
            default:
                throw new UnsupportedOperationException(command.name() + " command not supported (Heroku)");
        }
    }


    @Override
    public void bindToService(Module module, ServiceApp service) throws PaasException
    {
        CloudService cs = connector.createService(service.getServiceName(), service.getServiceInstanceName(), service.getServicePlan());
        if (cs != null)
        {
            // bind to service
            logger.info(">> Binding application to service [" + service.getServiceInstanceName() + "] ... ");
            connector.getConnectedClient().bindService(module.getName(), service.getServiceInstanceName());
        }
    }
    
    
    @Override
    public void unbindFromService(Module module, ServiceApp service)
    {
        connector.getConnectedClient().unbindService(module.getName(), service.getServiceInstanceName());
        connector.getConnectedClient().deleteService(service.getServiceInstanceName());
    }
    
    
    
    public void resetAccount()
    {
        connector.getConnectedClient().deleteAllServices();
        connector.getConnectedClient().deleteAllApplications();
    }


    @Override
    public Module getModule(String moduleName) throws PaasException
    {
        logger.debug("getModule({})", moduleName);
        
        CloudApplication app = connector.getConnectedClient().getApplication(moduleName);
        
        if (app == null) {
            throw new PaasException("Application " + moduleName + " is NULL");
        }
        
        Map<String, Object> m = connector.getConnectedClient().getApplicationEnvironment(moduleName);
        
        return new eu.seaclouds.paas.cloudfoundry.Module(app, m);
    }


}
