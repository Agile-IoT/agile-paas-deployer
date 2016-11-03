package eu.atos.paas.openshift2;

import com.openshift.client.InvalidCredentialsOpenShiftException;

import eu.atos.paas.AuthenticationException;
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
        try {
            
            Openshift2Connector connector = new Openshift2Connector(credentials.getUser(), credentials.getPassword());
            Openshift2Session session = new Openshift2Session(connector);
            /*
             * Just to trigger the login
             */
            session.getModule("don't care");
            return session;
            
        } catch (InvalidCredentialsOpenShiftException e) {
            throw new AuthenticationException();
        }
    }
    

}
