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
package eu.atos.paas.client;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import eu.atos.paas.resources.ErrorEntity;

public class RestClientException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private ErrorEntity error;
    
    public RestClientException(ErrorEntity error) {
        super(error.getMessage());
        this.error = error;
    }
    
    public RestClientException(Response response) {
        this(extractEntity(response));
    }

    public RestClientException(String message) {
        super(message);
        this.error = new ErrorEntity(Status.INTERNAL_SERVER_ERROR, message);
    }

    public RestClientException(Throwable cause) {
        super(cause);
        this.error = new ErrorEntity(Status.INTERNAL_SERVER_ERROR, cause.getMessage());
    }

    public ErrorEntity getError() {
        return error;
    }
    
    private static ErrorEntity extractEntity(Response response) {
        ErrorEntity error = null;
        
        MediaType mediaType = response.getMediaType();
        if (response.hasEntity() && MediaType.APPLICATION_JSON_TYPE.isCompatible(mediaType)) {
            error = response.readEntity(ErrorEntity.class);
        }
        if (error == null) {
            Status status = Status.fromStatusCode(response.getStatus());
            error = new ErrorEntity(status, 
                    String.format("%d %s", status.getStatusCode(), status.getReasonPhrase()));
        }
        return error;
    }
}
