package eu.seaclouds.paas;


public interface PaasClient {

    PaasSession getSession(Credentials credentials) throws PaasException;
    
}
