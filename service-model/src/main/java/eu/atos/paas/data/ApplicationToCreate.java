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
package eu.atos.paas.data;

import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


import com.fasterxml.jackson.annotation.JsonIgnore;

import eu.atos.paas.resources.exceptions.ValidationException;

public class ApplicationToCreate {

    public enum ArtifactType {
        SOURCE, TARGET, CONTAINER;
    }
    
    private String name;
    private URL gitUrl;

    @JsonIgnore
    private InputStream artifact;
    private String imageName;

    private ArtifactType artifactType = ArtifactType.TARGET;
    
    private String programmingLanguage = "";
    
    private Map<String, String> properties = Collections.emptyMap();
    
    private Map<String, String> envs = Collections.emptyMap();

    /**
     * Just for deserialization 
     */
    public ApplicationToCreate() {
    }
    
    @Deprecated
    public ApplicationToCreate(String name, InputStream artifact, String programmingLanguage, 
            Map<String, String> additionalProperties) {
        
        this.name = Objects.requireNonNull(name);
        this.artifact = Objects.requireNonNull(artifact);
        this.programmingLanguage = Objects.requireNonNull(programmingLanguage);
        this.properties = new HashMap<String, String>(additionalProperties);
    }
    
    @Deprecated
    public ApplicationToCreate(String name, InputStream artifact, ArtifactType artifactType, String programmingLanguage,
            Map<String, String> additionalProperties) {
        
        this.name = Objects.requireNonNull(name);
        this.artifact = Objects.requireNonNull(artifact);
        this.artifactType = Objects.requireNonNull(artifactType);
        this.programmingLanguage = Objects.requireNonNull(programmingLanguage);
        this.properties = new HashMap<String, String>(additionalProperties);
    }
    
    @Deprecated
    public ApplicationToCreate(String name, URL gitUrl, String programmingLanguage, 
            Map<String, String> additionalProperties) {
        
        this.name = Objects.requireNonNull(name);
        this.gitUrl = Objects.requireNonNull(gitUrl);
        this.programmingLanguage = Objects.requireNonNull(programmingLanguage);
        this.properties = new HashMap<String, String>(additionalProperties);
    }
    
    
    public String getName() {
        return name;
    }
    
    public URL getGitUrl() {
        return gitUrl;
    }
    
    public InputStream getArtifact() {
        return artifact;
    }
    
    public void setArtifact(InputStream artifact) {
        this.artifact = artifact;
    }
    
    public String getImageName() {
        return imageName;
    }
    
    public String getProgrammingLanguage() {
        return programmingLanguage;
    }
    
    public ArtifactType getArtifactType() {
        return artifactType;
    }
    
    public Map<String, String> getProperties() {
        return Collections.unmodifiableMap(properties);
    }

    public String getProperty(String propertyName) {
        return properties.get(propertyName);
    }

    public Map<String, String> getEnvs() {
        return Collections.unmodifiableMap(envs);
    }
    
    public String getEnv(String envVar) {
        return envs.get(envVar);
    }
    
    public void validate() {
        
        if (name == null || name.isEmpty()) {
            throw new ValidationException("Application name must be specified");
        }
        int sum = 0;
        if (artifact != null) {
            sum += 1;
        }
        if (gitUrl != null) {
            sum += 1;
        }
        if (imageName != null) {
            sum += 1;
        }
        if (sum != 1) {
            throw new ValidationException("Either artifact or git url or image name must be specified");
        }
    }

    @Override
    public String toString() {
        return String.format("ApplicationToCreate [name=%s, gitUrl=%s, artifact=%s, artifactType=%s]", 
                name, gitUrl, artifact != null, artifactType);
    }
    
    public static class Builder {
        
        private ApplicationToCreate o;

        public Builder(String name, InputStream is, ArtifactType type) {
            o = new ApplicationToCreate();
            o.name = name;
            o.artifact = is;
            o.artifactType = type;
        }
        
        public Builder(String name, URL gitUrl) {
            o = new ApplicationToCreate();
            o.name = name;
            o.gitUrl = gitUrl;
            o.artifactType = ArtifactType.SOURCE;
        }
        
        public Builder(String name, String imageName) {
            o = new ApplicationToCreate();
            o.name = name;
            o.imageName = imageName;
            o.artifactType = ArtifactType.CONTAINER;
        }
        
        public Builder(ApplicationToCreate o) {
            this.o = o;
        }

        public Builder programmingLanguage(String programmingLanguage) {
            this.o.programmingLanguage = programmingLanguage;
            return this;
        }
        
        public Builder envs(Map<String, String> envs) {
            o.envs = new HashMap<String, String>(envs);
            return this;
        }
        
        public Builder env(String key, String value) {
            if (o.envs.isEmpty()) {
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
            if (o.properties.isEmpty()) {
                o.properties = new HashMap<String, String>();
            }
            o.properties.put(key, value);
            return this;
        }
        
        public ApplicationToCreate build() {
            
            o.validate();
            return o;
        }
    }
    
    
}
