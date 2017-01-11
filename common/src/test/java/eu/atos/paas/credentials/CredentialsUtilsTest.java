package eu.atos.paas.credentials;

import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

public class CredentialsUtilsTest {

    @Test
    public void shadowJsonCredentials() {
        checkShadowJsonCredentials("{\"user\":\"this-is-a-user\",\"password\":\"this-is-a-password\"}");
        checkShadowJsonCredentials("{\"user\":\"this-is-a-user\",\"password\":\"this-is-a-password\",\"api\":\"bla\"}");
    }

    private void checkShadowJsonCredentials(String creds) {
        String result = CredentialsUtils.shadowJsonCredentials(creds);
        assertFalse(result.contains("this-is-a-password"));
    }


    @Test
    public void shadowPassword() {
        assertEquals("********", CredentialsUtils.shadowPassword("password"));
    }
}
