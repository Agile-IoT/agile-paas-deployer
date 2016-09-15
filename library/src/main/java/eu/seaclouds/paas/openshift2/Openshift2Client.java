package eu.seaclouds.paas.openshift2;

import eu.seaclouds.paas.Credentials;
import eu.seaclouds.paas.PaasClient;
import eu.seaclouds.paas.PaasSession;


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
        if (credentials instanceof Credentials.UserPasswordCredentials) {
            
            session = getSession((Credentials.UserPasswordCredentials)credentials);
        }
        else {
            
            throw new UnsupportedOperationException("Credentials of class " + credentials.getClass().getName() + " not supported (Openshift2)");
        }
        
        return session;
    }

    
    private PaasSession getSession(Credentials.UserPasswordCredentials credentials) {
        Openshift2Connector connector = new Openshift2Connector(credentials.getUser(), credentials.getPassword());
        PaasSession session = new Openshift2Session(connector);
        
        return session;
    }
    

}
