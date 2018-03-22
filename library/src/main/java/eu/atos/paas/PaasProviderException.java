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
 * Exception that encapsulates exceptions thrown by provider clients (e.g., CF client) and 
 * indicates an unexpected error from the provider.
 */
public class PaasProviderException extends PaasException {
    private static final long serialVersionUID = 1L;

    public PaasProviderException() {
    }

    public PaasProviderException(String msg) {
        super(msg);
    }

    public PaasProviderException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
