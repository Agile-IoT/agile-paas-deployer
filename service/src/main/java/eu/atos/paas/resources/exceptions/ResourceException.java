package eu.atos.paas.resources.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import eu.atos.paas.resources.ErrorEntity;

public class ResourceException extends WebApplicationException {
    private static final long serialVersionUID = 1L;

    public ResourceException(ErrorEntity error) {
        super(buildResponse(error));
    }

    private static Response buildResponse(ErrorEntity error) {
        Response response = Response
                .status(error.getCode())
                .entity(error)
                .build();
        
        return response;
    }
}
