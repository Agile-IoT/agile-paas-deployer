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
        SOURCE, TARGET
    }
    
    private String name;
    private URL gitUrl;
    
    @JsonIgnore
    private InputStream artifact;
    private ArtifactType artifactType = ArtifactType.TARGET;
    
    private String programmingLanguage = "";
    
    private Map<String, String> properties = Collections.emptyMap();

    /**
     * Just for deserialization 
     */
    public ApplicationToCreate() {
    }
    
    public ApplicationToCreate(String name, InputStream artifact, String programmingLanguage, 
            Map<String, String> additionalProperties) {
        
        this.name = Objects.requireNonNull(name);
        this.artifact = Objects.requireNonNull(artifact);
        this.programmingLanguage = Objects.requireNonNull(programmingLanguage);
        this.properties = new HashMap<String, String>(additionalProperties);
    }
    
    public ApplicationToCreate(String name, InputStream artifact, ArtifactType artifactType, String programmingLanguage,
            Map<String, String> additionalProperties) {
        
        this.name = Objects.requireNonNull(name);
        this.artifact = Objects.requireNonNull(artifact);
        this.artifactType = Objects.requireNonNull(artifactType);
        this.programmingLanguage = Objects.requireNonNull(programmingLanguage);
        this.properties = new HashMap<String, String>(additionalProperties);
    }
    
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
    
    public void validate() {
        
        if (name == null || name.isEmpty()) {
            throw new ValidationException("Application name must be specified");
        }
        if ((artifact == null && gitUrl == null) || (artifact != null && gitUrl != null)) {
            throw new ValidationException("Either artifact or git url must be specified");
        }
    }

    @Override
    public String toString() {
        return String.format("ApplicationToCreate [name=%s, gitUrl=%s, artifact=%s, artifactType=%s]", 
                name, gitUrl, artifact != null, artifactType);
    }
    
    
}
