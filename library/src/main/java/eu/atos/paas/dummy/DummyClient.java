package eu.atos.paas.dummy;

import eu.atos.paas.AuthenticationException;
import eu.atos.paas.PaasClient;
import eu.atos.paas.PaasException;
import eu.atos.paas.PaasSession;
import eu.atos.paas.credentials.Credentials;
import eu.atos.paas.credentials.UserPasswordCredentials;

public class DummyClient implements PaasClient {

    /**
     * Value of accepted user
     */
    public static final String USER = "user";
    /**
     * Value of accepted password
     */
    public static final String PASSWORD = "password";

    public DummyClient() {
    }

    @Override
    public PaasSession getSession(Credentials credentials) throws PaasException, AuthenticationException {
        UserPasswordCredentials creds = (UserPasswordCredentials) credentials;
        if (USER.equals(creds.getUser()) && PASSWORD.equals(creds.getPassword())) {
            return new DummySession();
        }
        else {
            throw new AuthenticationException();
        }
    }

}
