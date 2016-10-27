package eu.atos.paas.openshift2;

import eu.atos.paas.PaasClient;
import eu.atos.paas.PaasSession;
import eu.atos.paas.credentials.Credentials;
import eu.atos.paas.credentials.UserPasswordCredentials;


/**
 * 
 *
 * @author ATOS
 * @date 18/2/2016-14:26:45
 */
public class Openshift2Client implements PaasClient
{

    
    @Override
    public PaasSession getSession(Credentials credentials) {
        PaasSession session = null;
        if (credentials instanceof UserPasswordCredentials) {
            
            session = getSession((UserPasswordCredentials)credentials);
        }
        else {
            
            throw new UnsupportedOperationException("Credentials of class " + credentials.getClass().getName() + " not supported (Openshift2)");
        }
        
        return session;
    }

    
    private PaasSession getSession(UserPasswordCredentials credentials) {
        Openshift2Connector connector = new Openshift2Connector(credentials.getUser(), credentials.getPassword());
        PaasSession session = new Openshift2Session(connector);
        
        return session;
    }
    

}
