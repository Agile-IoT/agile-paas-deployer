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

import eu.atos.paas.credentials.Credentials;

public interface PaasClient {

    /**
     * Gets a session for using the provider.
     * 
     * @param credentials Credential to connect to the provider.
     * @return A connected session
     * @throws AuthenticationException if the authentication fails.
     * @throws UnsupportedOperationException on wrong credential type.
     * @throws PaasProviderException on any other unexpected error from the provider.
     */
    PaasSession getSession(Credentials credentials) 
            throws AuthenticationException, UnsupportedOperationException, PaasProviderException;
    
    String getVersion();
}
