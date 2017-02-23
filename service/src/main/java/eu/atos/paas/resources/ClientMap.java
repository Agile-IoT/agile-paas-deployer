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
package eu.atos.paas.resources;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import eu.atos.paas.PaasClient;

/**
 * Holds the associations between api versions and respective provider clients for a Paas resource.
 */
public class ClientMap {
    private Map<String, PaasClient> map;
    
    private ClientMap(Map<String, PaasClient> map) {
        this.map = map;
    }
    
    /**
     * Return the PaasClient associated to a key; null if no association.
     */
    public PaasClient get(String key) {
        return map.get(key);
    }
    
    /**
     * Returns true if a version is associated to a client; false otherwise
     */
    public boolean containsKey(String version) {
        return map.containsKey(version);
    }
    
    public static ClientMapBuilder builder() {
        return new ClientMapBuilder();
    }
    public static class ClientMapBuilder {
        private Map<String, PaasClient> map;
        
        private ClientMapBuilder() {
            this.map = new HashMap<>();
        }
        
        public ClientMapBuilder client(PaasClient client) {
            Objects.requireNonNull(client);
            if (client.getVersion().isEmpty()) {
                throw new IllegalArgumentException("PaasClient.getVersion() cannot be empty");
            }
            map.put(client.getVersion(), client);
            return this;
        }
        
        public ClientMap build() {
            if (map.size() == 0) {
                throw new IllegalStateException("Trying to build ClientMap without clients");
            }
            return new ClientMap(map);
        }
    }
}
