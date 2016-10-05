package eu.atos.paas;


public interface PaasClient {

    PaasSession getSession(Credentials credentials) throws PaasException;
    
}
