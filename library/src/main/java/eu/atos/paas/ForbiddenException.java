package eu.atos.paas;

/**
 * Exception to indicate lack of permissions on the provider to access a resource
 * (e.g. in Heroku, applications names are shared between all users; trying to access an application
 * owned by other user will throw a 403).
 */
public class ForbiddenException extends PaasException {
    private static final long serialVersionUID = 1L;

    private final String resourceName;
    
    public ForbiddenException(String resourceName, Throwable e) {
        
        this.resourceName = resourceName;
    }
    
    public String getResourceName() {
        return resourceName;
    }
}
