package eu.atos.paas;

import java.util.Objects;

/**
 * This exceptions indicates an operation on a resource that do not exist. 
 * 
 * For example, you are trying to delete an non-existent application.
 */
public class NotFoundException extends PaasException {
    private static final long serialVersionUID = 1L;

    private String resourceName;
    
    public NotFoundException(String resourceName) {
        super(resourceName + " not found");
        this.resourceName = Objects.requireNonNull(resourceName);
    }

    public NotFoundException(String resourceName, Throwable cause) {
        super(resourceName + " not found", cause);
        this.resourceName = Objects.requireNonNull(resourceName);
    }

    public String getResourceName() {
        return resourceName;
    }
}
