/**
 *  Copyright (c) 2017 Atos, and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License 2.0
 *  which accompanies this distribution, and is available at
 *  https://www.eclipse.org/legal/epl-2.0/
 *
 *  Contributors:
 *  Atos - initial implementation
 */
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
