package eu.seaclouds.paas.openshift3;

import eu.seaclouds.paas.Credentials;
import eu.seaclouds.paas.PaasClient;
import eu.seaclouds.paas.PaasException;
import eu.seaclouds.paas.PaasSession;


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
