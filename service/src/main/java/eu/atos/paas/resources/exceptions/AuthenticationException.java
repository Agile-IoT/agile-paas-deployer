package eu.atos.paas.resources.exceptions;

import javax.ws.rs.core.Response.Status;

import eu.atos.paas.resources.ErrorEntity;

public class AuthenticationException extends ResourceException {
    private static final long serialVersionUID = 1L;

    public AuthenticationException() {
        super(new ErrorEntity(Status.UNAUTHORIZED, "Authentication error"));
    }
}
