package eu.atos.paas.openshift3;

import eu.atos.paas.PaasClient;
import eu.atos.paas.PaasException;
import eu.atos.paas.PaasSession;
import eu.atos.paas.credentials.Credentials;


/**
 * 
 *
 * @author ATOS
 * @date 18/2/2016-14:26:32
 */
public class Openshift3Client implements PaasClient
{
    

    @Override
    public PaasSession getSession(Credentials credentials) throws PaasException
    {
        throw new UnsupportedOperationException("Openshift3 client not implemented");
    }

    
}
