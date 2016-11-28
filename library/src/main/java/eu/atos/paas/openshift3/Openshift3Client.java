package eu.atos.paas.openshift3;

import eu.atos.paas.PaasClient;
import eu.atos.paas.PaasException;
import eu.atos.paas.PaasSession;
import eu.atos.paas.credentials.Credentials;


/**
 * 
 */
public class Openshift3Client implements PaasClient
{
    public static final String VERSION = "v3";

    @Override
    public PaasSession getSession(Credentials credentials) throws PaasException
    {
        throw new UnsupportedOperationException("Openshift3 client not implemented");
    }

    @Override
    public String getVersion() {
        return VERSION;
    }
}
