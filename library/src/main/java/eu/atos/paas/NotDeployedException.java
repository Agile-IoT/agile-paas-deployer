package eu.atos.paas;

public class NotDeployedException extends PaasException {
    private static final long serialVersionUID = 1L;
    
    public NotDeployedException(String applicationName) {
        super(applicationName + " is not deployed");
    }
}
