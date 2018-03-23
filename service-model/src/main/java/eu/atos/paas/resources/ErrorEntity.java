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