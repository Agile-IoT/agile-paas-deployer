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
package eu.atos.paas;

/**
 * A PaasException encapsulates exceptions raised by provider clients or other expected errors (authentication error,
 *     create an application with an existing name, delete a non existing application...)
 */
public class PaasException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public PaasException() {
    }
    
    public PaasException(String msg) {
        super(msg);
    }
    
    public PaasException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
