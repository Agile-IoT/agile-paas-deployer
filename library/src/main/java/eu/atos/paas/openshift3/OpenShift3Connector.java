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

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.openshift.restclient.IClient;
import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.images.DockerImageURI;
import com.openshift.restclient.model.IDeploymentConfig;
import com.openshift.restclient.model.IPort;
import com.openshift.restclient.model.IProject;
import com.openshift.restclient.model.IService;
import com.openshift.restclient.model.deploy.DeploymentTriggerType;

public class OpenShift3Connector
{
    
    private IClient client;
    
    public OpenShift3Connector(IClient osClient)
    {
        this.client = osClient;
    }
    
    public IProject getProject(String projectName) {
        IProject project = client.get(ResourceKind.PROJECT, projectName, "");
        return project;
    }
    
    public List<IProject> getProjects() {
        
        List<IProject> projects = client.list(ResourceKind.PROJECT);
        return projects;
    }
    
    public IDeploymentConfig createDcForContainer(
            String projectName, String appName, String dcName, String imageName, Map<String, String> env, 
            int numReplicas) {
        
        IDeploymentConfig dc = client.getResourceFactory().stub(ResourceKind.DEPLOYMENT_CONFIG, dcName, projectName);
        
        /*
         * Mandatory
         */
        HashSet<IPort> ports = new HashSet<IPort>();
        
        dc.addContainer(new DockerImageURI(imageName), ports, Collections.<String, String>emptyMap());
        dc.setReplicaSelector(ModuleImpl.LABEL_APP, appName);
        dc.addLabel(ModuleImpl.LABEL_APP, appName);
        dc.setReplicas(numReplicas);
        dc.addTrigger(DeploymentTriggerType.CONFIG_CHANGE);

        for (String key : env.keySet()) {
            String value = env.get(key);
            dc.setEnvironmentVariable(key, value);
        }
        dc = client.create(dc);
        
        return dc;
    }

    public IService createService(String projectName, String appName, String serviceName, int port) {
        IService service = client.getResourceFactory().stub(ResourceKind.SERVICE, serviceName, projectName);
        service.setPort(port);
        service.setSelector(ModuleImpl.LABEL_APP, appName);
        service = client.create(service);
        
        return service;
    }
}
