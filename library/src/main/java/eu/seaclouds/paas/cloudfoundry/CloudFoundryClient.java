package eu.seaclouds.paas.cloudfoundry;

import eu.seaclouds.paas.Credentials;
import eu.seaclouds.paas.PaasClient;
import eu.seaclouds.paas.PaasSession;


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
        if (credentials instanceof Credentials.ApiUserPasswordOrgSpaceCredentials)
        {
            session = getSession((Credentials.ApiUserPasswordOrgSpaceCredentials) credentials);
        }
        else
        {
            throw new UnsupportedOperationException("Credentials of class " + credentials.getClass().getName() + " not supported (Cloud Foundry)");
        }
        
        return session;
    }


    private PaasSession getSession(Credentials.ApiUserPasswordOrgSpaceCredentials credentials)
    {
        CloudFoundryConnector connector = new CloudFoundryConnector(credentials.getApi(), credentials.getUser(), credentials.getPassword(),
                credentials.getOrg(), credentials.getSpace(), true);

        PaasSession session = new CloudFoundrySession(connector);

        return session;
    }


}
