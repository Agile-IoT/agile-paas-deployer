package eu.atos.paas.openshift2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openshift.client.IApplication;

import eu.atos.paas.AlreadyExistsException;
import eu.atos.paas.Module;
import eu.atos.paas.NotFoundException;
import eu.atos.paas.PaasException;
import eu.atos.paas.PaasProviderException;
import eu.atos.paas.PaasSession;
import eu.atos.paas.ServiceApp;


/**
 * 
 *
 * @author ATOS
 * @date 11/3/2016-15:54:03
 */
public class Openshift2Session implements PaasSession
{
    
    
    private static Logger logger = LoggerFactory.getLogger(Openshift2Session.class);
    // paas connector
    private Openshift2Connector connector;
    
    
    /**
     * 
     * Constructor
     * @param connector
     */
    public Openshift2Session(Openshift2Connector connector) {
        this.connector = connector;
    }
    
 
    /**
     * @return the connector
     */
    public Openshift2Connector getConnector()
    {
        return connector;
    }


    @Override
    public Module createApplication(String moduleName, DeployParameters params)
            throws PaasProviderException, AlreadyExistsException {

        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    
    @Override
    public Module updateApplication(String moduleName, DeployParameters params)
            throws NotFoundException, PaasProviderException {

        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    
    @Override
    public Module deploy(String moduleName, DeployParameters params) throws PaasException
    {
        logger.info("DEPLOY({})", moduleName);
        IApplication app = connector.deployAppFromGit(moduleName, params.getPath(), params.getCartridge());
        return getModule(moduleName, app);
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
                connector.startApp(module.getName());
                break;
                
            case STOP:
                connector.stopApp(module.getName());
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
                connector.scaleUp(module.getName());
                break;
                
            case SCALE_DOWN_INSTANCES:
                connector.scaleDown(module.getName());
                break;
                
            case SCALE_UP_MEMORY:
            case SCALE_DOWN_MEMORY:
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
        connector.bindToService(module.getName(), service.getServiceName());
    }

    
    @Override
    public void unbindFromService(Module module, ServiceApp service) throws PaasException
    {
        connector.unbindFromService(module.getName(), service.getServiceName());
    }

    
    @Override
    public Module getModule(String moduleName) throws PaasException
    {
        logger.debug("getModule({})", moduleName);
        
        IApplication app = connector.getAppFromDomains(moduleName);
        
        if (app == null) {
            return null;
        }

        return new eu.atos.paas.openshift2.Module(app);
    }
    
    
    /**
     * 
     * @param moduleName
     * @param app
     * @return
     * @throws PaasException
     */
    private Module getModule(String moduleName, IApplication app) throws PaasException
    {
        logger.debug("getModule({})", moduleName);
        
        if (app == null) {
            throw new PaasException("Application " + moduleName + " is NULL");
        }

        return new eu.atos.paas.openshift2.Module(app);
    }
    

}
