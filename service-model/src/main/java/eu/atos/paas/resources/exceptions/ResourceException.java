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
