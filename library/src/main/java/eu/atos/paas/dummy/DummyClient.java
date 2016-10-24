package eu.atos.paas.dummy;

import eu.atos.paas.Credentials;
import eu.atos.paas.PaasClient;
import eu.atos.paas.PaasException;
import eu.atos.paas.PaasSession;

public class DummyClient implements PaasClient {

    public DummyClient() {
    }

    @Override
    public PaasSession getSession(Credentials credentials) throws PaasException {
        return new DummySession(credentials);
    }

}
