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
package eu.atos.paas;

import eu.atos.paas.credentials.Credentials;

public interface PaasClient {

    /**
     * Gets a session for using the provider.
     * 
     * @param credentials Credential to connect to the provider.
     * @return A connected session
     * @throws AuthenticationException if the authentication fails.
     * @throws PaasProviderException on any other unexpected error from the provider.
     */
    PaasSession getSession(Credentials credentials) throws AuthenticationException, PaasProviderException;
    
    String getVersion();
}
