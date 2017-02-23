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
package eu.atos.paas.cloudfoundry;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.lib.domain.CloudApplication;
import org.cloudfoundry.client.lib.domain.CloudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import eu.atos.paas.AlreadyExistsException;
import eu.atos.paas.Module;
import eu.atos.paas.NotDeployedException;
import eu.atos.paas.NotFoundException;
import eu.atos.paas.PaasException;
import eu.atos.paas.PaasProviderException;
import eu.atos.paas.PaasSession;
import eu.atos.paas.ServiceApp;
import eu.atos.paas.Module.State;


/**
 * 
 * @author 
 *
 */
public class CloudFoundrySession implements PaasSession {

    private static Logger logger = LoggerFactory.getLogger(CloudFoundrySession.class);
    
    /**
     * CloudFoundry does not differentiate when a created application does not have an uploaded artifact:
     * it is an stopped application. We use this env var to detect when an application has been deployed.
     */
    static final String DEPLOYED_FLAG = "PUL_DEPLOYED";
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
    public Module createApplication(String moduleName, DeployParameters params)
            throws PaasProviderException, AlreadyExistsException {
        
        Objects.requireNonNull(moduleName);
        Objects.requireNonNull(params);
        
        try {
            
            if (getModule(moduleName) != null) {
                throw new AlreadyExistsException(moduleName);
            }
            CloudApplication app = connector.createApplication(moduleName, "", params.getBuildpackUrl());
            Module m = new ModuleImpl(app);
            
            return m;
            
        } catch (CloudFoundryException e) {
            
            throw new PaasProviderException(e.getMessage(), e);
        }
    }

    
    @Override
    public Module updateApplication(String moduleName, DeployParameters params)
            throws NotFoundException, PaasProviderException {
    
        Objects.requireNonNull(moduleName);
        Objects.requireNonNull(params);

        try {
            
            Module actualModule = getModule(moduleName);
            if (actualModule == null) {
                throw new NotFoundException(moduleName);
            }
            
            if (!params.getPath().isEmpty()) {
                Map<String, String> newEnv = new HashMap<>(actualModule.getEnv());
                newEnv.put(DEPLOYED_FLAG, "1");
                connector.updateEnvironment(moduleName, newEnv);
                connector.uploadApplication(moduleName, params.getPath());
                connector.getConnectedClient().startApplication(moduleName);
            }
            
            /*
             * TODO: Other updates
             */
            
            Module m = getModule(moduleName);
            return m;
            
        } catch (CloudFoundryException e) {
            throw handle(moduleName, e);
        }
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
        try {
            
            connector.deleteApp(moduleName);
            
        }
        catch (CloudFoundryException e)
        {
            throw handle(moduleName, e);
        }
    }


    @Override
    public void startStop(Module module, StartStopCommand command) 
            throws PaasException, NotFoundException, UnsupportedOperationException
    {
        Objects.nonNull(module);
        Objects.nonNull(command);

        try {
            logger.info(command.name() + "({})", module.getName());
            
            Module actualModule = getModule(module.getName());
            if (actualModule == null) {
                throw new NotFoundException(module.getName());
            }
            if (State.UNDEPLOYED.equals(actualModule.getState())) {
                throw new NotDeployedException(module.getName());
            }
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
        } catch (CloudFoundryException e) {
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
        
        try {
            
            CloudApplication app = connector.getConnectedClient().getApplication(moduleName);
            Map<String, Object> m = connector.getConnectedClient().getApplicationEnvironment(moduleName);
            return new eu.atos.paas.cloudfoundry.ModuleImpl(app, m);
            
        } catch (CloudFoundryException e) {
            if (isNotFound(e)) {
                return null;
            }
            throw new PaasProviderException(e.getMessage(), e);
        }
    }


    private PaasException handle(String moduleName, CloudFoundryException e) {
        if (isNotFound(e)) {
            return new NotFoundException(moduleName, e);
        }
        return new PaasProviderException(e.getMessage(), e);
    }


    private boolean isNotFound(CloudFoundryException e) {
        return HttpStatus.NOT_FOUND.equals(e.getStatusCode());
    }
}
