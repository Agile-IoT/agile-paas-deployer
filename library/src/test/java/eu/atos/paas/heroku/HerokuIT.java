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
package eu.atos.paas.heroku;

import java.net.MalformedURLException;
import java.net.URL;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import eu.atos.paas.AbstractProviderIT;
import eu.atos.paas.Groups;
import eu.atos.paas.PaasClient;
import eu.atos.paas.TestConfigProperties;
import eu.atos.paas.credentials.ApiKeyCredentials;

@Test(groups = Groups.HEROKU)
public class HerokuIT extends AbstractProviderIT {

    @BeforeClass
    public void initialize() {
        System.out.println("---" + this.getClass().getName() + "---");
        /*
         * Modify to get an unique APPNAME, because Heroku shares application names between all users.
         */
        AbstractProviderIT.APP_NAME = "paas-unified-library-test";
        AbstractProviderIT.APP_NAME_NOT_EXISTS = "paas-unified-library-notexists";
        super.initialize();
        
        PaasClient client = new HerokuClient();
        
        session = client.getSession(new ApiKeyCredentials(TestConfigProperties.getInstance().getHeroku_apiKey()));
        
        URL url;
        try {
            url = new URL("https://github.com/heroku/java-getting-started.git");
            //url = new URL("https://github.com/efsavage/hello-world-war");
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
        this.params = new DeployParameters(url, null);
    }
}
