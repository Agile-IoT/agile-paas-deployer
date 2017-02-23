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
package eu.atos.paas.data;

import java.io.InputStream;
import java.net.URL;
import java.util.Objects;


import com.fasterxml.jackson.annotation.JsonIgnore;

import eu.atos.paas.resources.exceptions.ValidationException;

public class ApplicationToCreate {

    private String name;
    private URL gitUrl;
    @JsonIgnore
    private InputStream artifact;

    private String programmingLanguage;

    /**
     * Just for deserialization 
     */
    public ApplicationToCreate() {
    }
    
    public ApplicationToCreate(String name, InputStream artifact, String programmingLanguage) {
        this.name = Objects.requireNonNull(name);
        this.artifact = Objects.requireNonNull(artifact);
        this.programmingLanguage = Objects.requireNonNull(programmingLanguage);
    }
    
    public ApplicationToCreate(String name, URL gitUrl, String programmingLanguage) {
        this.name = Objects.requireNonNull(name);
        this.gitUrl = Objects.requireNonNull(gitUrl);
        this.programmingLanguage = Objects.requireNonNull(programmingLanguage);
    }
    
    /**
     * Constructor to be used to construct an instance from REST parameters. Call {@link #validate()} later.
     */
    public ApplicationToCreate(ApplicationToCreate application, InputStream artifact) {
        this.name = application.getName();
        this.gitUrl = application.getGitUrl();
        this.artifact = artifact;
        this.programmingLanguage = application.getProgrammingLanguage();
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

    public String getProgrammingLanguage() {
        return programmingLanguage;
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
        return String.format("ApplicationToCreate [name=%s, gitUrl=%s, artifact=%s]", name, gitUrl, artifact != null);
    }
    
    
}
