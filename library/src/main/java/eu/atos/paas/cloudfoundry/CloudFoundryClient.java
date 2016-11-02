package eu.atos.paas.cloudfoundry;

import org.apache.http.conn.HttpHostConnectException;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.springframework.web.client.ResourceAccessException;

import eu.atos.paas.AuthenticationException;
import eu.atos.paas.PaasClient;
import eu.atos.paas.PaasSession;
import eu.atos.paas.credentials.ApiUserPasswordOrgSpaceCredentials;
import eu.atos.paas.credentials.Credentials;


/**
 * 
 * @author
 *
 */
public class CloudFoundryClient implements PaasClient
{


    @Override
    public PaasSession getSession(Credentials credentials)
    {
        PaasSession session = null;
        if (credentials instanceof ApiUserPasswordOrgSpaceCredentials)
        {
            session = getSession((ApiUserPasswordOrgSpaceCredentials) credentials);
        }
        else
        {
            throw new UnsupportedOperationException("Credentials of class " + credentials.getClass().getName() + " not supported (Cloud Foundry)");
        }
        
        return session;
    }


    private PaasSession getSession(ApiUserPasswordOrgSpaceCredentials credentials)
    {
        try {
            CloudFoundryConnector connector = new CloudFoundryConnector(
                    credentials.getApi(), credentials.getUser(), credentials.getPassword(),
                    credentials.getOrg(), credentials.getSpace(), true);
            PaasSession session = new CloudFoundrySession(connector);
    
            return session;
        
        } catch (CloudFoundryException e) {
            switch (e.getStatusCode()) {
            case UNAUTHORIZED:
            case FORBIDDEN:
                throw new AuthenticationException();
            default:
                throw e;
            }
        } catch (ResourceAccessException e) {
            
            if (e.getCause() != null && e.getCause() instanceof HttpHostConnectException) {
                throw new AuthenticationException(e.getCause().getMessage());
            }
            throw e;
        }
    }


}
