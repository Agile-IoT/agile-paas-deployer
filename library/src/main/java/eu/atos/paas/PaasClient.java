package eu.atos.paas;

import eu.atos.paas.credentials.Credentials;

public interface PaasClient {

    PaasSession getSession(Credentials credentials) throws PaasException;
    
}
