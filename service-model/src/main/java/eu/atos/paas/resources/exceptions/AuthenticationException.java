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

import javax.ws.rs.core.Response.Status;

import eu.atos.paas.resources.ErrorEntity;

public class AuthenticationException extends ResourceException {
    private static final long serialVersionUID = 1L;

    public AuthenticationException() {
        super(new ErrorEntity(Status.UNAUTHORIZED, "Authentication error"));
    }
    
    public AuthenticationException(String message) {
        super(new ErrorEntity(Status.UNAUTHORIZED, message));
    }
}
