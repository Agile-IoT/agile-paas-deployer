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
    private Map<String, Object> env;
    private int instances;
    private boolean started;
    
    public ModuleImpl(String name, URL url, String type, int instances) {
        this.name = name;
        this.url = url;
        this.type = type;
        this.services = new ArrayList<String>();
        this.env = new HashMap<String, Object>();
        this.instances = instances;
        this.started = true;
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

    public void addEnvVars(Map<String, Object> envVars) {
        this.env.putAll(envVars);
    }

    public void scaleInstances(int instances) {
        this.instances = instances;
    }
    
    public void start() {
        this.started = true;
    }
    
    public void stop() {
        this.started = false;
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
    public Map<String, Object> getEnv() {
        return Collections.unmodifiableMap(env);
    }

    public boolean isStarted() {
        return started;
    }
}
