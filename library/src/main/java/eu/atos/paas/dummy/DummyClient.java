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
package eu.atos.paas.dummy;

import eu.atos.paas.AuthenticationException;
import eu.atos.paas.PaasClient;
import eu.atos.paas.PaasException;
import eu.atos.paas.PaasSession;
import eu.atos.paas.credentials.Credentials;
import eu.atos.paas.credentials.UserPasswordCredentials;

public class DummyClient implements PaasClient {
    
    public static final String VERSION = "v1";
    
    /**
     * Value of accepted user
     */
    public static final String USER = "user";
    /**
     * Value of accepted password
     */
    public static final String PASSWORD = "password";

    public DummyClient() {
    }

    @Override
    public PaasSession getSession(Credentials credentials) throws PaasException, AuthenticationException {
        UserPasswordCredentials creds = (UserPasswordCredentials) credentials;
        if (USER.equals(creds.getUser()) && PASSWORD.equals(creds.getPassword())) {
            return new DummySession();
        }
        else {
            throw new AuthenticationException();
        }
    }

    @Override
    public String getVersion() {
        return VERSION;
    }
}
