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
    
}
