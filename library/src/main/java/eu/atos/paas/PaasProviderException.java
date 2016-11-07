package eu.atos.paas;

/**
 * Exception that encapsulates exceptions thrown by provider clients (e.g., CF client) and 
 * indicates an unexpected error from the provider.
 */
public class PaasProviderException extends PaasException {
    private static final long serialVersionUID = 1L;

    public PaasProviderException() {
    }

    public PaasProviderException(String msg) {
        super(msg);
    }

    public PaasProviderException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
