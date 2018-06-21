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
package eu.atos.paas;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import eu.atos.paas.PaasSession.DeployParameters;

public class DeployParametersImpl implements DeployParameters {

    private String path = "";
    private URL gitUrl;
    private String code = "";
    private String imageName = "";
    protected Map<String, String> properties = Collections.emptyMap();
    protected Map<String, String> envs = Collections.emptyMap();
    
    private DeployParametersImpl() {
    }
    
    public DeployParametersImpl(String path, URL gitUrl, Map<String, String> properties) {
        this.path = path;
        this.gitUrl = gitUrl;
        this.properties = new HashMap<>(properties);
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public URL getGitUrl() {
        return gitUrl;
    }

    @Override
    public String getImageName() {
        return imageName;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getProperty(String propertyName, String defaultValue) {
        return properties.getOrDefault(propertyName, defaultValue);
    }
    
    @Override
    public int getPropertyAsInt(String propertyName, int defaultValue) {
        String str = properties.getOrDefault(propertyName, Integer.toString(defaultValue));

        int result;
        try {
            result = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            result = defaultValue;
        }
        return result;
    }
    
    @Override
    public Map<String, String> getProperties() {
        return properties;
    }
    
    @Override
    public Map<String, String> getEnvs() {
        return envs;
    }
    
    @Override
    public String getEnv(String envName) {
        return envs.get(envName);
    }

    public static class Builder {
        
        private DeployParametersImpl o;
        
        public static Builder fromPath(String path) {
            Builder b = new Builder();
            b.o.path = path;
            return b;
        }

        public static Builder fromGitUrl(URL gitUrl) {
            Builder b = new Builder();
            b.o.gitUrl = gitUrl;
            return b;
        }

        public static Builder fromCode(String code) {
            Builder b = new Builder();
            b.o.code= code;
            return b;
        }

        public static Builder fromImageName(String imageName) {
            Builder b = new Builder();
            b.o.imageName = imageName;
            return b;
        }
        
        private Builder() {
            o = new DeployParametersImpl();
        }
        
        public Builder envs(Map<String, String> envs) {
            o.envs = new HashMap<String, String>(envs);
            return this;
        }
        
        public Builder env(String key, String value) {
            if (o.envs == Collections.EMPTY_MAP) {
                o.envs = new HashMap<String, String>();
            }
            o.envs.put(key, value);
            return this;
        }
        
        public Builder properties(Map<String, String> properties) {
            o.properties = new HashMap<String, String>(properties);
            return this;
        }
        
        public Builder property(String key, String value) {
            if (o.properties == Collections.EMPTY_MAP) {
                o.properties = new HashMap<String, String>();
            }
            o.properties.put(key, value);
            return this;
        }
        
        public DeployParametersImpl build() {
            
            return o;
        }
        
    }
}
