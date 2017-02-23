/**
 * Copyright 2016 Atos
 * Contact: Atos <roman.sosa@atos.net>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
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
