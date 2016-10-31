package eu.atos.paas.client;

public class RestClientException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private int status;
    
    public RestClientException() {
    }

    public RestClientException(String message) {
        super(message);
    }

    public RestClientException(Throwable cause) {
        super(cause);
    }

    public RestClientException(String message, Throwable cause, int status) {
        super(message, cause);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
