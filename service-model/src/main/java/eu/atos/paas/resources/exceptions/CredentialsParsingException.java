package eu.atos.paas.resources.exceptions;

import javax.ws.rs.core.Response.Status;

import eu.atos.paas.resources.ErrorEntity;

public class CredentialsParsingException extends ResourceException {
    private static final long serialVersionUID = 1L;

    public CredentialsParsingException(String message) {
        super(new ErrorEntity(Status.UNAUTHORIZED, "Could not parse credentials. " + message));
    }
}
