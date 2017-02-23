/**
 * Copyright 2016 Atos
 * Contact: Atos <roman.sosa@atos.net>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
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
import eu.atos.paas.credentials.UserPasswordCredentials;

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
        
        session = client.getSession(new UserPasswordCredentials(
                TestConfigProperties.getInstance().getHeroku_user(),
                TestConfigProperties.getInstance().getHeroku_password()));
        
        URL url;
        try {
            url = new URL("https://github.com/heroku/java-getting-started.git");
            //url = new URL("https://github.com/efsavage/hello-world-war");
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
        this.params = new DeployParameters(url);
    }
}
