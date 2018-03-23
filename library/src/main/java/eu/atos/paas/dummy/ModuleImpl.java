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
package eu.atos.paas.dummy;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleImpl implements eu.atos.paas.Module {

    private String name;
    private URL url;
    private String type;
    private List<String> services;
    private Map<String, String> env;
    private int instances;
    private State state;
    
    public ModuleImpl(String name, URL url, String type, int instances, boolean started) {
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
    public URL getUrl() {
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
