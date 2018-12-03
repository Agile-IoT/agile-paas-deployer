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
package eu.atos.paas.heroku;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.heroku.api.Addon;
import com.heroku.api.App;
import com.heroku.api.Formation;
import com.heroku.api.exception.HerokuAPIException;
import com.heroku.api.exception.RequestFailedException;

import eu.atos.paas.AlreadyExistsException;
import eu.atos.paas.ForbiddenException;
import eu.atos.paas.Module;
import eu.atos.paas.NotDeployedException;
import eu.atos.paas.NotFoundException;
import eu.atos.paas.PaasException;
import eu.atos.paas.PaasProviderException;
import eu.atos.paas.PaasSession;
import eu.atos.paas.ServiceApp;
import eu.atos.paas.Module.State;

/*
 * Non-satisfactory support for getting the state of an application:
 * check https://github.com/pbougie/heroku-apps-status/blob/master/lib/commands/apps.rb#L60 
 */
public class HerokuSession implements PaasSession {
    
    
    private static final Logger logger = LoggerFactory.getLogger(HerokuSession.class);
    // paas connector
    private final HerokuConnector connector;
    
    /**
     * Heroku does not differentiate states.
     */
    static final String UNDEPLOYED_FLAG = "PUL_UNDEPLOYED";
    
    /**
     * 
     * Constructor
     * @param connector
     */
    public HerokuSession(HerokuConnector connector) {
        this.connector = connector;
    }
    
    
    @Override
    public Module createApplication(String moduleName, DeployParameters params)
            throws PaasProviderException, AlreadyExistsException {
        Objects.requireNonNull(moduleName);
        Objects.requireNonNull(params);
        
        logger.info("CREATE APPLICATION({})", moduleName);
        
        try {
            
            if (connector.getHerokuAPIClient().appExists(moduleName)) {
                throw new AlreadyExistsException(moduleName);
            }
            connector.createApp(moduleName);
            connector.addConfig(moduleName, UNDEPLOYED_FLAG, "1");
            
            return getModule(moduleName);

        } catch (HerokuAPIException e) {
            
            throw new PaasProviderException(e.getMessage(), e);
        }
    }
    

    @Override
    public Module updateApplication(String moduleName, DeployParameters params)
            throws NotFoundException, PaasProviderException {
        Objects.requireNonNull(moduleName);
        Objects.requireNonNull(params);

        logger.info("UPDATE APPLICATION({})", moduleName);

        try {
            
            boolean deployed;
            
            String undeployed = connector.getAppEnvironmentValue(moduleName, UNDEPLOYED_FLAG);
            Map<String, String> envs = params.getEnvs();
            if (undeployed != null) {
                envs.put(UNDEPLOYED_FLAG, undeployed);
            }
            connector.setConfig(moduleName, envs);
            
            if (params.getGitUrl() != null) {
                
                deployed = connector.deployApp(moduleName, params.getGitUrl());
            }
            else if (params.getPath() != null && !params.getPath().isEmpty()) {

                deployed = connector.deployApp(moduleName, params.getPath());
            }
            else {
                throw new UnsupportedOperationException("Not implemented yet");
            }
            if (!deployed) {
                throw new PaasException("Application not deployed");
            }
            connector.removeConfig(moduleName, UNDEPLOYED_FLAG);
            return getModule(moduleName);
            
        } catch (HerokuAPIException e) {
            throw handle(moduleName, e);
        }
    }
    
    
    @Override
    public Module deploy(String moduleName, PaasSession.DeployParameters params) throws PaasException {
        Objects.requireNonNull(moduleName);
        Objects.requireNonNull(params);

        logger.info("DEPLOY({})", moduleName);
        
        try {
            
            App app = connector.createApp(moduleName);
            
            if (app == null) {
                throw new PaasException("Application not created");
            }
            Module result = updateApplication(moduleName, params);
            
            return result;
            
        } catch (HerokuAPIException e) {
            
            throw handle(moduleName, e);
        }
    }

    
    @Override
    public void undeploy(String moduleName) throws PaasException {
        Objects.requireNonNull(moduleName);

        logger.info("UNDEPLOY({})", moduleName);
        try {
            
            connector.getHerokuAPIClient().destroyApp(moduleName);
            
        } catch (HerokuAPIException e) {
            
            throw handle(moduleName, e);
        }
    }

    
    @Override
    public void startStop(Module module, PaasSession.StartStopCommand command) throws PaasException, UnsupportedOperationException
    {
        logger.info("{}({})", command.name(), module.getName());
        
        try {
            
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
                    connector.scaleProcess(module.getName(), module.getAppType(), 1);
                    break;
                    
                case STOP:
                    connector.scaleProcess(module.getName(), module.getAppType(), 0);
                    break;
                    
                default:
                    throw new UnsupportedOperationException(command.name() + " command not supported (Heroku)");
            }
    
        } catch (HerokuAPIException e) {
            
            throw handle(module.getName(), e);
        }
    }


    @Override
    public void scaleUpDown(Module module, ScaleUpDownCommand command) throws PaasException, UnsupportedOperationException
    {
        logger.info("{}({})", command.name(), module.getName());
        switch (command)
        {
            case SCALE_UP_INSTANCES:
                connector.scaleProcess(module.getName(), module.getAppType(), module.getRunningInstances() + 1);
                break;
                
            case SCALE_DOWN_INSTANCES:
                if (module.getRunningInstances() > 1) {
                    connector.scaleProcess(module.getName(), module.getAppType(), module.getRunningInstances() - 1);
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
        logger.info("{}({})", command.name(), module.getName());
        switch (command)
        {
            case SCALE_INSTANCES:
                connector.scaleProcess(module.getName(), module.getAppType(), scale_value);
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
        
        App app;
        
        try {
            
            app = connector.getHerokuAPIClient().getApp(moduleName);
            List<Addon> l = connector.getHerokuAPIClient().listAppAddons(moduleName);
            Map<String, String> m = connector.getHerokuAPIClient().listConfig(moduleName);
            List<Formation> formations = connector.getHerokuAPIClient().listFormation(moduleName);
            return new ModuleImpl(app, l, m, formations);
        
        } catch (RequestFailedException e) {
            
            if (isNotFound(e)) {
                return null;
            }
            if (isForbidden(e)) {
                throw new ForbiddenException(moduleName, e);
            }
            throw new PaasProviderException(e.getMessage(), e);
            
        } catch (HerokuAPIException e) {
            
            throw new PaasProviderException(e.getMessage(), e);
        }
    }
    

    private PaasException handle(String moduleName, HerokuAPIException e) {
        
        if (e instanceof RequestFailedException) {
            RequestFailedException rfe = (RequestFailedException) e;
            
            if (isNotFound(rfe)) {
                return new NotFoundException(moduleName, e);
            }
            else if (isForbidden(rfe)) {
                return new ForbiddenException(moduleName, e);
            }
        }
        return new PaasProviderException(e.getMessage(), e);
    }
    
    private boolean isNotFound(RequestFailedException e) {
        return e.getStatusCode() == HttpStatus.NOT_FOUND.value();
    }
    
    private boolean isForbidden(RequestFailedException e) {
        return e.getStatusCode() == HttpStatus.FORBIDDEN.value();
    }

}
