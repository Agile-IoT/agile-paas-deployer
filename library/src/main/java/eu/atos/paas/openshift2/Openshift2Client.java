package eu.atos.paas.openshift2;

import java.net.MalformedURLException;
import java.net.URL;

import com.openshift.client.InvalidCredentialsOpenShiftException;

import eu.atos.paas.AuthenticationException;
import eu.atos.paas.PaasClient;
import eu.atos.paas.PaasSession;
import eu.atos.paas.credentials.ApiUserPasswordCredentials;
import eu.atos.paas.credentials.Credentials;


/**
 * 
 *
 * @author ATOS
 * @date 18/2/2016-14:26:45
 */
public class Openshift2Client implements PaasClient
{
    public static final String VERSION = "v2";
    
    @Override
    public PaasSession getSession(Credentials credentials) {
        PaasSession session = null;
        if (credentials instanceof ApiUserPasswordCredentials) {
            
            session = getSession((ApiUserPasswordCredentials)credentials);
        }
        else {
            
            throw new UnsupportedOperationException("Credentials of class " + credentials.getClass().getName() + 
                    " not supported (Openshift2)");
        }
        
        return session;
    }

    private PaasSession getSession(ApiUserPasswordCredentials credentials) {
        try {
            
            URL serverUrl;
            try {
                serverUrl = new URL(credentials.getApi());
            } catch (MalformedURLException e) {
                throw new AuthenticationException("Malformed API endpoint: " + credentials.getApi());
            }
            Openshift2Connector connector = new Openshift2Connector(
                    serverUrl, credentials.getUser(), credentials.getPassword());
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
    
    @Override
    public String getVersion() {
        return VERSION;
    }
}
