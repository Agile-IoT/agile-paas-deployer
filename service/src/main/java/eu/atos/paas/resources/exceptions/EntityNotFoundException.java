package eu.atos.paas.resources.exceptions;

import javax.ws.rs.core.Response.Status;

import eu.atos.paas.resources.ErrorEntity;

public class EntityNotFoundException extends ResourceException {
    private static final long serialVersionUID = 1L;

    public EntityNotFoundException(String path404) {
        super(new ErrorEntity(Status.NOT_FOUND, String.format("%s not found", path404)));
    }

}
