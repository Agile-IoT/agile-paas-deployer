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
package eu.atos.paas.dummy;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import eu.atos.paas.AlreadyExistsException;
import eu.atos.paas.Module;
import eu.atos.paas.Module.State;
import eu.atos.paas.NotDeployedException;
import eu.atos.paas.NotFoundException;
import eu.atos.paas.PaasException;
import eu.atos.paas.PaasProviderException;
import eu.atos.paas.PaasSession;
import eu.atos.paas.ServiceApp;

public class DummySession implements PaasSession {

    private static Map<String, ModuleImpl> modules = new HashMap<String, ModuleImpl>();
    
    public DummySession() {
    }

    @Override
    public Module createApplication(String moduleName, DeployParameters params)
            throws PaasProviderException, AlreadyExistsException {
        
        Objects.requireNonNull(moduleName);
        Objects.requireNonNull(params);
        
        if (getModule(moduleName) != null) {
            throw new AlreadyExistsException(moduleName);
        }
        
        ModuleImpl m = new ModuleImpl(moduleName, null, "web", 1, false);
        modules.put(moduleName, m);

        return m;
    }
    
    @Override
    public Module updateApplication(String moduleName, DeployParameters params) 
            throws NotFoundException, PaasProviderException {

        Objects.requireNonNull(moduleName);
        Objects.requireNonNull(params);
        
        ModuleImpl m = modules.get(moduleName);

        if (m == null) {
            throw new NotFoundException(moduleName);
        }
        m.start();
        return m;
    }
    
    @Override
    public Module deploy(String moduleName, DeployParameters params) 
        throws PaasProviderException, AlreadyExistsException {
        
        Objects.requireNonNull(moduleName);
        Objects.requireNonNull(params);
        
        if (getModule(moduleName) != null) {
            throw new AlreadyExistsException(moduleName);
        }

        URL url;
        try {
            url = new URL("http://www.example.com/" + moduleName);
        } catch (MalformedURLException e) {
            throw new PaasException(e.getMessage(), e);
        }
        ModuleImpl m = new ModuleImpl(moduleName, url, "web", 1, true);
        
        /*
         * If this throws a client API exception, should be encapsulated by PaasProviderException
         */
        modules.put(moduleName, m);

        return m;
    }

    @Override
    public void undeploy(String moduleName) throws NotFoundException, PaasProviderException {
        Objects.requireNonNull(moduleName);
        
        if (getModule(moduleName) == null) {
            throw new NotFoundException(moduleName + " do not exist");
        }
        /*
         * If this throws a client API exception, should be encapsulated by PaasProviderException
         */
        modules.remove(moduleName);
    }

    @Override
    public void startStop(Module module, StartStopCommand command) throws NotFoundException, PaasProviderException {
        Objects.requireNonNull(module);
        Objects.requireNonNull(command);
        Objects.requireNonNull(module.getName());
        
        String moduleName = module.getName();
        ModuleImpl actualModule = (ModuleImpl) getModule(moduleName);
        if (actualModule == null) {
            throw new NotFoundException(moduleName + " do not exist");
        }
        switch(command) {
        case START:
            if (actualModule.getState().equals(State.UNDEPLOYED)) {
                throw new NotDeployedException(moduleName);
            }
            actualModule.start();
            break;
        case STOP:
            actualModule.stop();
            break;
        default:
            throw new IllegalArgumentException(command + " not recognized");
        }
    }

    @Override
    public void scaleUpDown(Module module, ScaleUpDownCommand command)
            throws PaasException, UnsupportedOperationException {
        Objects.requireNonNull(module);
        Objects.requireNonNull(command);
        Objects.requireNonNull(module.getName());
        
        ModuleImpl m = modules.get(module.getName());
        switch (command) {
        case SCALE_UP_INSTANCES:
            m.scaleInstances(m.getRunningInstances() + 1);
            break;
        case SCALE_DOWN_INSTANCES:
            m.scaleInstances(m.getRunningInstances() + 1);
            break;
        default:
        }
        return;
    }

    @Override
    public void scale(Module module, ScaleCommand command, int scale_value)
            throws PaasException, UnsupportedOperationException {
        
        ModuleImpl m = modules.get(module.getName());
        switch (command) {
        case SCALE_INSTANCES:
            m.scaleInstances(scale_value);
            break;
        default:
        }
    }

    @Override
    public void bindToService(Module module, ServiceApp service) throws PaasException {
        
        ModuleImpl m = modules.get(module.getName());
        m.addService(service.getServiceName());
    }

    @Override
    public void unbindFromService(Module module, ServiceApp service) throws PaasException {
        ModuleImpl m = modules.get(module.getName());
        m.delService(service.getServiceName());
    }

    @Override
    public Module getModule(String moduleName) throws PaasException {
        Objects.requireNonNull(moduleName);
        
        ModuleImpl m = modules.get(moduleName);
        return m;
    }

}
