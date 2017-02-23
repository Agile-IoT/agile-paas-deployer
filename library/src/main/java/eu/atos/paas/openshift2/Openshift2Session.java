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
package eu.atos.paas.openshift2;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openshift.client.IApplication;
import com.openshift.client.InvalidCredentialsOpenShiftException;
import com.openshift.client.OpenShiftException;

import eu.atos.paas.AlreadyExistsException;
import eu.atos.paas.Module;
import eu.atos.paas.NotFoundException;
import eu.atos.paas.PaasException;
import eu.atos.paas.PaasProviderException;
import eu.atos.paas.PaasSession;
import eu.atos.paas.ServiceApp;


/**
 * 
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
        Objects.nonNull(moduleName);
        Objects.nonNull(params);
        
        try {
            
            if (getModule(moduleName) != null) {
                throw new AlreadyExistsException(moduleName);
            }
            eu.atos.paas.openshift2.DeployParameters p = (eu.atos.paas.openshift2.DeployParameters) params;
            IApplication app = connector.deployAppFromGit(moduleName, p.getGitUrl().toString(), p.getCartridge());
            
            return new ModuleImpl(app);

        } catch (OpenShiftException e) {
            
            throw handle(moduleName, e);
        }
    }
    
    
    @Override
    public Module updateApplication(String moduleName, DeployParameters params)
            throws NotFoundException, PaasProviderException {

        Objects.nonNull(moduleName);
        Objects.nonNull(params);
        
        if (getModule(moduleName) == null) {
            throw new NotFoundException(moduleName);
        }
        
        /*
         * TODO
         */
        
        return getModule(moduleName);
    }
    
    
    @Override
    public Module deploy(String moduleName, DeployParameters params) throws PaasException
    {
        Objects.nonNull(moduleName);
        Objects.nonNull(params);
        
        logger.info("DEPLOY({})", moduleName);
        
        try {
            String url = params.getGitUrl().toString();
            IApplication app = connector.deployAppFromGit(moduleName, url, params.getCartridge());
            return new ModuleImpl(app);
        
        } catch (OpenShiftException e) {
            
            throw handle(moduleName, e);
        }
    }

    
    @Override
    public void undeploy(String moduleName) throws PaasException
    {
        Objects.nonNull(moduleName);

        logger.info("UNDEPLOY({})", moduleName);
        if (getModule(moduleName) == null) {
            throw new NotFoundException(moduleName);
        }
        
        try {
            
            connector.deleteApp(moduleName);
        
        } catch (OpenShiftException e) {
            
            throw handle(moduleName, e);
        }
    }
    

    @Override
    public void startStop(Module module, StartStopCommand command) throws PaasException, UnsupportedOperationException
    {
        Objects.nonNull(module);
        Objects.nonNull(command);
        
        logger.info(command.name() + "({})", module.getName());
        
        try {
            if (getModule(module.getName()) == null) {
                throw new NotFoundException(module.getName());
            }
            
            switch (command)
            {
                case START:
                    connector.startApp(module.getName());
                    break;
                    
                case STOP:
                    connector.stopApp(module.getName());
                    break;
                    
                default:
                    throw new UnsupportedOperationException(command.name() + " command not supported (OpenShift2)");
            }
        } catch (OpenShiftException e) {
            
            throw handle(module.getName(), e);
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
        
        try {
            
            IApplication app = connector.getAppFromDomains(moduleName);
            
            if (app == null) {
                return null;
            }
    
            return new eu.atos.paas.openshift2.ModuleImpl(app);

        } catch (InvalidCredentialsOpenShiftException e) {
            /*
             * This should only happen when called from Client.getSession().
             * Let the exception bubble up
             */
            throw e;
            
        } catch (OpenShiftException e) {
            
            throw handle(moduleName, e);
        }
    }
    
    private PaasException handle(String moduleName, OpenShiftException e) {
        
        return new PaasProviderException(e.getMessage(), e);
    }
}
