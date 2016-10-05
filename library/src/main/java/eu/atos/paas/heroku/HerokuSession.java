package eu.atos.paas.heroku;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.heroku.api.Addon;
import com.heroku.api.App;

import eu.atos.paas.Module;
import eu.atos.paas.PaasException;
import eu.atos.paas.PaasSession;
import eu.atos.paas.ServiceApp;


public class HerokuSession implements PaasSession {
    
    
    private static Logger logger = LoggerFactory.getLogger(HerokuSession.class);
    // paas connector
    private HerokuConnector connector;
    
    
    /**
     * 
     * Constructor
     * @param connector
     */
    public HerokuSession(HerokuConnector connector) {
        this.connector = connector;
    }
    

    @Override
    public Module deploy(String moduleName, PaasSession.DeployParameters params) throws PaasException {
        logger.info("DEPLOY({})", moduleName);
        
        App app = connector.createJavaWebApp(moduleName);
        
        if (app == null) {
            throw new PaasException("Application not created");
        }
        
        boolean deployed = connector.deployJavaWebApp(moduleName, params.getPath());
        
        if (!deployed) {
            throw new PaasException("Application not deployed");
        }
        return getModule(moduleName);
    }

    
    @Override
    public void undeploy(String moduleName) throws PaasException {
        logger.info("UNDEPLOY({})", moduleName);
        connector.deleteApp(moduleName);
    }

    
    @Override
    public void startStop(Module module, PaasSession.StartStopCommand command) throws PaasException, UnsupportedOperationException
    {
        logger.info(command.name() + "({})", module.getName());
        switch (command)
        {
            case START:
                connector.getHerokuAPIClient().scaleProcess(module.getName(), module.getAppType(), 1);
                break;
                
            case STOP:
                connector.getHerokuAPIClient().scaleProcess(module.getName(), module.getAppType(), 0);
                break;
                
            default:
                throw new UnsupportedOperationException(command.name() + " command not supported (Heroku)");
        }
    }


    @Override
    public void scaleUpDown(Module module, ScaleUpDownCommand command) throws PaasException, UnsupportedOperationException
    {
        logger.info(command.name() + "({})", module.getName());
        switch (command)
        {
            case SCALE_UP_INSTANCES:
                connector.getHerokuAPIClient().scaleProcess(module.getName(), module.getAppType(), module.getRunningInstances() + 1);
                break;
                
            case SCALE_DOWN_INSTANCES:
                if (module.getRunningInstances() > 1) {
                    connector.getHerokuAPIClient().scaleProcess(module.getName(), module.getAppType(), module.getRunningInstances() - 1);
                }
                break;
                
            case SCALE_UP_MEMORY:
            case SCALE_DOWN_MEMORY:
            default:
                throw new UnsupportedOperationException(command.name() + " command not supported (Heroku)");
        }
    }
    
    
    @Override
    public void scale(Module module, ScaleCommand command, int scale_value) throws PaasException, UnsupportedOperationException
    {
        logger.info(command.name() + "({})", module.getName());
        switch (command)
        {
            case SCALE_INSTANCES:
                connector.getHerokuAPIClient().scaleProcess(module.getName(), module.getAppType(), scale_value);
                break;
                
            case SCALE_MEMORY:
            case SCALE_DISK:
            default:
                throw new UnsupportedOperationException(command.name() + " command not supported (Heroku)");
        }
    }


    @Override
    public void bindToService(Module module, ServiceApp service) throws PaasException
    {
        //AddonChange change = connector.getHerokuAPIClient().addAddon(module.getName(), service.getServiceName());
        connector.getHerokuAPIClient().addAddon(module.getName(), service.getServiceName());
    }


    @Override
    public void unbindFromService(Module module, ServiceApp service) throws PaasException
    {
        //AddonChange change = connector.getHerokuAPIClient().removeAddon(module.getName(), service.getServiceName());
        connector.getHerokuAPIClient().removeAddon(module.getName(), service.getServiceName());
    }
    

    @Override
    public Module getModule(String moduleName) throws PaasException
    {
        logger.debug("getModule({})", moduleName);
        
        App app = connector.getHerokuAPIClient().getApp(moduleName);
        
        if (app == null) {
            throw new PaasException("Application " + moduleName + " is NULL");
        }
        
        try
        {
            List<Addon> l = connector.getHerokuAPIClient().listAppAddons(moduleName);
            Map<String, String> m = connector.getHerokuAPIClient().listConfig(moduleName);
            
            return new eu.atos.paas.heroku.Module(app, l, m);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new eu.atos.paas.heroku.Module(app, null, null);
        }
    }
    

}
