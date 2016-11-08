package eu.atos.paas.data;

import java.io.InputStream;
import java.net.URL;
import java.util.Objects;


import com.fasterxml.jackson.annotation.JsonIgnore;

import eu.atos.paas.resources.exceptions.ValidationException;

public class ApplicationToCreate {

    private String name;
    private URL gitUrl;
    private String cartridge;
    @JsonIgnore
    private InputStream artifact;

    /**
     * Just for deserialization 
     */
    public ApplicationToCreate() {
    }
    
    public ApplicationToCreate(String name, InputStream artifact, String cartridge) {
        this.name = Objects.requireNonNull(name);
        this.artifact = Objects.requireNonNull(artifact);
        this.cartridge = Objects.requireNonNull(cartridge);
    }
    
    public ApplicationToCreate(String name, URL gitUrl, String cartridge) {
        this.name = Objects.requireNonNull(name);
        this.gitUrl = Objects.requireNonNull(gitUrl);
        this.cartridge = Objects.requireNonNull(cartridge);
    }
    
    /**
     * Constructor to be used to construct an instance from REST parameters. Call {@link #validate()} later.
     */
    public ApplicationToCreate(ApplicationToCreate application, InputStream artifact) {
        this.name = application.getName();
        this.gitUrl = application.getGitUrl();
        this.cartridge = application.getCartridge();
        this.artifact = artifact;
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

    public String getCartridge() {
        return cartridge;
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
