package eu.seaclouds.paas.heroku;

import eu.seaclouds.paas.Credentials;
import eu.seaclouds.paas.PaasClient;
import eu.seaclouds.paas.PaasSession;


public class HerokuClient implements PaasClient {

    
    @Override
    public PaasSession getSession(Credentials credentials) {
        PaasSession session = null;
        if (credentials instanceof Credentials.UserPasswordCredentials) {
            
            session = getSession((Credentials.UserPasswordCredentials)credentials);
        }
        else if (credentials instanceof Credentials.ApiKeyCredentials) {

            session = getSession((Credentials.ApiKeyCredentials)credentials);
            
        } else {
            
            throw new UnsupportedOperationException("Credentials of class " + credentials.getClass().getName() + " not supported (Heroku)");
        }
        
        return session;
    }

    
    private PaasSession getSession(Credentials.UserPasswordCredentials credentials) {
        HerokuConnector connector = new HerokuConnector(credentials.getUser(), credentials.getPassword());
        PaasSession session = new HerokuSession(connector);
        
        return session;
    }
    
    
    private PaasSession getSession(Credentials.ApiKeyCredentials credentials) {
        HerokuConnector connector = new HerokuConnector(credentials.getApiKey());
        
        PaasSession session = new HerokuSession(connector);
        
        return session;
    }
    
    
}
