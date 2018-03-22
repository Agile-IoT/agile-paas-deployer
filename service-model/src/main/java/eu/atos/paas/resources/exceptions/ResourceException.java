/**
 *  Copyright (c) 2017 Atos, and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License 2.0
 *  which accompanies this distribution, and is available at
 *  https://www.eclipse.org/legal/epl-2.0/
 *
 *  Contributors:
 *  Atos - initial implementation
 */
package eu.atos.paas.resources.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import eu.atos.paas.resources.ErrorEntity;

public class ResourceException extends WebApplicationException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a ResourceException to be thrown by the REST service.
     * 
     * @param error Information about the error to be sent to client.
     */
    public ResourceException(ErrorEntity error) {
        super(buildResponse(error));
    }

    /**
     * Constructs a ResourceException to be thrown by the REST service, produced by other exception.
     * 
     * @param error Information about the error to be sent to client.
     * @param cause Cause of the exception. The cause will not be sent to client, but it should be logged
     * in server.
     */
    public ResourceException(ErrorEntity error, Throwable cause) {
        super(cause, buildResponse(error));
    }

    private static Response buildResponse(ErrorEntity error) {
        Response response = Response
                .status(error.getCode())
                .entity(error)
                .build();
        
        return response;
    }
}
