package eu.atos.paas.heroku;

import eu.atos.paas.PaasClient;
import eu.atos.paas.PaasSession;
import eu.atos.paas.credentials.ApiKeyCredentials;
import eu.atos.paas.credentials.Credentials;
import eu.atos.paas.credentials.UserPasswordCredentials;


public class HerokuClient implements PaasClient {
    public static final String VERSION = "v3";
    
    @Override
    public PaasSession getSession(Credentials credentials) {
        PaasSession session = null;
        if (credentials instanceof UserPasswordCredentials) {
            
            session = getSession((UserPasswordCredentials)credentials);
        }
        else if (credentials instanceof ApiKeyCredentials) {

            session = getSession((ApiKeyCredentials)credentials);
            
        } else {
            
            throw new UnsupportedOperationException("Credentials of class " + credentials.getClass().getName() + " not supported (Heroku)");
        }
        
        return session;
    }

    
    private PaasSession getSession(UserPasswordCredentials credentials) {
        HerokuConnector connector = new HerokuConnector(credentials.getUser(), credentials.getPassword());
        PaasSession session = new HerokuSession(connector);
        
        return session;
    }
    
    
    private PaasSession getSession(ApiKeyCredentials credentials) {
        HerokuConnector connector = new HerokuConnector(credentials.getApiKey());
        
        PaasSession session = new HerokuSession(connector);
        
        return session;
    }
    
    @Override
    public  String getVersion() {
        return VERSION;
    }
}
