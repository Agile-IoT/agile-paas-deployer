package eu.atos.paas;

import java.util.Objects;

/**
 * This exceptions indicates a conflict between a creation operation and an existing resource. 
 * 
 * For example, the name of the application you are trying to create already exists.
 */
public class AlreadyExistsException extends PaasException {
    private static final long serialVersionUID = 1L;

    private String resourceName;
    
    public AlreadyExistsException(String resourceName) {
        super(resourceName + " already exists");
        this.resourceName = Objects.requireNonNull(resourceName);
    }

    public AlreadyExistsException(String resourceName, Throwable cause) {
        super(resourceName + " already exists", cause);
        this.resourceName = Objects.requireNonNull(resourceName);
    }

    public String getResourceName() {
        return resourceName;
    }
}
