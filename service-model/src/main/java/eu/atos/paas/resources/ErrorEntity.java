package eu.atos.paas.resources;

import javax.ws.rs.core.Response.Status;

/**
 * JSON entity to be sent on failed requests. 
 * The schema matches the default entity sent by Jersey on exceptions, so the same deserialization could be used in 
 * client.
 */
public class ErrorEntity {
    
    private int code;
    private String message;
    
    public ErrorEntity() {
        this.code = Status.INTERNAL_SERVER_ERROR.getStatusCode();
        this.message = "Internal server error";
    }
    public ErrorEntity(Status status, String message) {
        this.code = status.getStatusCode();
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}