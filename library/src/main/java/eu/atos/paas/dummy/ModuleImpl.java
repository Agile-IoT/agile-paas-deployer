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

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleImpl implements eu.atos.paas.Module {

    private String name;
    private URI url;
    private String type;
    private List<String> services;
    private Map<String, String> env;
    private int instances;
    private State state;
    
    public ModuleImpl(String name, URI url, String type, int instances, boolean started) {
        this.name = name;
        this.url = url;
        this.type = type;
        this.services = new ArrayList<String>();
        this.env = new HashMap<String, String>();
        this.instances = instances;
        this.state = started? State.STARTED : State.UNDEPLOYED;
    }

    public void addServices(List<String> services) {
        this.services.addAll(services);
    }
    
    public void addService(String serviceName) {
        this.services.add(serviceName);
    }
    
    public void delService(String serviceName) {
        this.services.remove(serviceName);
    }

    public void addEnvVars(Map<String, String> envVars) {
        this.env.putAll(envVars);
    }

    public void scaleInstances(int instances) {
        this.instances = instances;
    }
    
    public void start() {
        this.state = State.STARTED;
    }
    
    public void stop() {
        this.state = State.STOPPED;
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public URI getUrl() {
        return url;
    }

    @Override
    public String getAppType() {
        return type;
    }

    @Override
    public int getRunningInstances() {
        return instances;
    }

    @Override
    public List<String> getServices() {
        return Collections.unmodifiableList(services);
    }

    @Override
    public Map<String, String> getEnv() {
        return Collections.unmodifiableMap(env);
    }
    
    @Override
    public State getState() {
        
        return state;
    }

    public boolean isStarted() {
        return State.STARTED.equals(state);
    }
}
