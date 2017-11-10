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
package eu.atos.paas.openshift3;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.openshift.restclient.model.IDeploymentConfig;
import com.openshift.restclient.model.IEnvironmentVariable;
import com.openshift.restclient.model.IService;
import com.openshift.restclient.model.route.IRoute;

import eu.atos.paas.Module;

public class ModuleImpl implements Module
{
    public static String LABEL_APP = "app";
    
    private IService service;
    private IDeploymentConfig dc;
    private IRoute route;
    
    public ModuleImpl(IService service, IDeploymentConfig dc, IRoute route) {
        this.service = Objects.requireNonNull(service);
        this.dc = Objects.requireNonNull(dc);
        this.route = route;
    }

    @Override
    public String getName() {
        return String.format("%s/%s", service.getNamespace(), service.getName());
    }

    @Override
    public URI getUrl() {
        try {
            if (route != null) {
                return new URI(route.getURL());
            }
            else {
                return new URI(null, null, service.getClusterIP(), service.getPort(), null, null, null);
            }
        } catch (URISyntaxException e) {
            /*
             * this should not happen
             */
            throw new IllegalArgumentException("Error in URL=" + route.getURL() + " from provider ", e);
        }
    }

    @Override
    public State getState() {
        
        return getRunningInstances() > 0? State.STARTED : State.STOPPED;
    }

    @Override
    public String getAppType() {
        return route != null? "web" : "service";
    }

    @Override
    public int getRunningInstances() {
        
        return dc.getCurrentReplicaCount();
    }

    @Override
    public List<String> getServices() {
        return Collections.emptyList();
    }

    @Override
    public Map<String, String> getEnv() {
        Map<String, String> m = new HashMap<>();
        for (IEnvironmentVariable v : dc.getEnvironmentVariables()) {
            m.put(v.getName(), v.getValue());
        }
        return m;
    }

    @Override
    public String toString() {
        return String.format(
                "ModuleImpl[name=%s, url=%s, state()=%s, type=%s, instances=%s, env=%s]",
                getName(), getUrl(), getState(), getAppType(), getRunningInstances(), getEnv());
    }

    
}
