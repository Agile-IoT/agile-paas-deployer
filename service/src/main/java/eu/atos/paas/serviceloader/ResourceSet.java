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
package eu.atos.paas.serviceloader;

import java.util.ServiceLoader;
import java.util.Set;

import eu.atos.paas.resources.PaasResource;

/**
 * Defines an interface to allow pluggable PaasResources using {@link ServiceLoader}.
 * 
 * An implementation of ResourceSet returns a set of ResourceDescriptor. 
 * A ResourceDescriptor links a PaasResource with the route to the resource.
 */
public interface ResourceSet {

    Set<ResourceDescriptor> getResources();
    
    public static class ResourceDescriptor {
        private String path;
        private PaasResource resource;
        
        public ResourceDescriptor(String path, PaasResource resource) {
            this.path = path;
            this.resource = resource;
        }
        public String getPath() {
            return path;
        }
        public PaasResource getResource() {
            return resource;
        }
    }
}
