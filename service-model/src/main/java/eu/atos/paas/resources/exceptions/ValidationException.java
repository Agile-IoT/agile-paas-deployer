package eu.atos.paas.resources.exceptions;

import javax.ws.rs.core.Response.Status;

import eu.atos.paas.resources.ErrorEntity;

public class ValidationException extends ResourceException {
    private static final long serialVersionUID = 1L;

    public ValidationException(String message) {
        super(new ErrorEntity(Status.BAD_REQUEST, message));
    }
}
