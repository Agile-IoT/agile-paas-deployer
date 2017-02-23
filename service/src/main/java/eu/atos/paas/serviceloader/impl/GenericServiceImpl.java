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
package eu.atos.paas.serviceloader.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.atos.paas.PaasClient;
import eu.atos.paas.data.Provider;
import eu.atos.paas.resources.ClientMap;
import eu.atos.paas.resources.PaasResource;
import eu.atos.paas.resources.ClientMap.ClientMapBuilder;
import eu.atos.paas.serviceloader.ResourceSet;

/**
 * ResourceSet implementation that reads the description of the providers from a config file.
 * 
 * The class uses by default the resource file given by <code>DEFAULT_CONF_PATH</code>. This default value can
 * be overriden with the value of the system variable <code>CONF_PATH_VAR</code>.
 */
public class GenericServiceImpl implements ResourceSet {

    /**
     *  name of system variable
     */
    private static final String CONF_PATH_VAR = "RESOURCE_CONF_PATH";
    /**
     * path of default config file
     */
    private static final String DEFAULT_CONF_PATH = "/resources.conf.json";
    
    @Override
    public Set<ResourceDescriptor> getResources() {

        String envConfPath = System.getenv(CONF_PATH_VAR);
        String defaultConfPath = DEFAULT_CONF_PATH;
        
        InputStream is = readConfigFileOrDefault(envConfPath, defaultConfPath);
        return getResources(is);
    }

    /**
     * Build the set of ResourceDescriptor given the InputStream where to read the config file.
     */
    public Set<ResourceDescriptor> getResources(InputStream is) {
        Set<ResourceDescriptor> set = new HashSet<>();
        Map<String, ResourceParameters> resourceDescriptionMap = getResourceMapping(is);
        
        for (Map.Entry<String, ResourceParameters> entry : resourceDescriptionMap.entrySet()) {
            String subpath = entry.getKey();
            ResourceParameters desc = entry.getValue();
            
            try {
                ClientMap clientMap = desc.buildClientMap();
                Constructor<?> cons = desc.resourceClass.getConstructor(Provider.class, ClientMap.class);
                
                PaasResource resource = (PaasResource)cons.newInstance(desc.provider, clientMap);
                
                set.add(new ResourceDescriptor(subpath, resource));
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException e) {
                throw new IllegalArgumentException(e.getMessage(), e);
            }
        }
        return set;
    }
    
    /**
     * Returns the InputStream of the config file.
     * @param envConfPath Optional (not used if null) path of the config file. Intended to overwrite the default value.
     * @param defaultConfPath Default path of the config file; used if envConfPath is null.
     */
    private InputStream readConfigFileOrDefault(String envConfPath, String defaultConfPath) {
        InputStream result;
        String confPath;
        
        if (envConfPath != null) {
            confPath = envConfPath;
            try {
                File configFile = new File(envConfPath);
                if (!configFile.exists() || !configFile.isFile()) {
                    throw new RuntimeException(
                            String.format("Config file %2s not found or is not readable", envConfPath));
                }
                result = new FileInputStream(configFile);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(
                        String.format("Config file %2s not found or is not readable", envConfPath));
            }
        }
        else {
            confPath = "resources:" + defaultConfPath;
            result = this.getClass().getResourceAsStream(defaultConfPath);
        }
        if (result == null) {
            throw new IllegalArgumentException("Error obtaining stream from " + confPath);
        }
        return result;
    }

    private Map<String, ResourceParameters> getResourceMapping(InputStream is) {
        
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<Map<String, ResourceParameters>> ref = new TypeReference<Map<String, ResourceParameters>>() {};

        Map<String, ResourceParameters> map;
        try {
            
            map = mapper.readValue(is, ref);
            
        } catch (IOException e) {
            
            throw new RuntimeException("Error reading JSON file");
        }
        
        return map;
    }

    public static class ResourceParameters {

        @JsonProperty("resourceClass")
        private String resourceClassName;
        
        @JsonProperty
        private Provider provider;

        @JsonProperty
        private String[] clients;

        @JsonIgnore
        private Class<? extends PaasResource> resourceClass;
        @JsonIgnore
        private List<Class<?>> clientClasses;
        
        @SuppressWarnings("unchecked")
        @JsonCreator
        public ResourceParameters(
                @JsonProperty("resourceClass") String resourceClassName, 
                @JsonProperty("provider") Provider provider, 
                @JsonProperty("clients") String[] clients) {
            
            this.resourceClassName = resourceClassName;
            this.provider = provider;
            this.clients = clients;

            try {
                this.resourceClass = (Class<? extends PaasResource>) Class.forName(this.resourceClassName);
                this.clientClasses = new ArrayList<>();
                
                for (String client : this.clients) {
                        this.clientClasses.add(Class.forName(client));
                }
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException(e.getMessage(), e);
            }
        }
        
        private ClientMap buildClientMap() throws InstantiationException, IllegalAccessException {
            ClientMapBuilder result = ClientMap.builder();
            
            for (Class<?> clientClass : clientClasses) {
                PaasClient client = (PaasClient) clientClass.newInstance();
                result.client(client);
            }
            return result.build();
        }
        
        @Override
        public String toString() {
            return String.format(
                    "ResourceParameters [resourceClassName=%s, provider=%s, clients=%s, "
                    + "resourceClass=%s, clientClasses=%s]",
                    resourceClassName, provider, Arrays.toString(clients), resourceClass, clientClasses);
        }

    }
}
