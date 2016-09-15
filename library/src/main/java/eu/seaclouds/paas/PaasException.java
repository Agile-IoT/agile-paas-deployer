package eu.seaclouds.paas;

public class PaasException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public PaasException() {
    }
    
    public PaasException(String msg) {
        super(msg);
    }
    
    public PaasException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
