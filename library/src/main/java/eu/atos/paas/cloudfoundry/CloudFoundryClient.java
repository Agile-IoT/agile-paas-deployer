package eu.atos.paas.cloudfoundry;

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
        CloudFoundryConnector connector = new CloudFoundryConnector(credentials.getApi(), credentials.getUser(), credentials.getPassword(),
                credentials.getOrg(), credentials.getSpace(), true);

        PaasSession session = new CloudFoundrySession(connector);

        return session;
    }


}
