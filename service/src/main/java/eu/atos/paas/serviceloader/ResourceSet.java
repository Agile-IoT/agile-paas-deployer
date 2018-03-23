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
