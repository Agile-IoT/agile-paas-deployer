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
