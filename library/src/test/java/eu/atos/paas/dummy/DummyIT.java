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
package eu.atos.paas.dummy;

import java.net.URL;
import java.util.Collections;
import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import eu.atos.paas.AbstractProviderIT;
import eu.atos.paas.Groups;
import eu.atos.paas.PaasSession.DeployParameters;
import eu.atos.paas.credentials.Credentials;
import eu.atos.paas.credentials.UserPasswordCredentials;

@Test(groups = Groups.DUMMY)
public class DummyIT extends AbstractProviderIT {

    @BeforeClass
    public void initialize() {
        System.out.println("---" + this.getClass().getName() + "---");
        super.initialize();
        
        Credentials credentials = new UserPasswordCredentials(DummyClient.USER, DummyClient.PASSWORD);
        this.session = new DummyClient().getSession(credentials);
        
        this.params = new DeployParameters() {
            
            @Override
            public String getPath() {
                return "";
            }
            
            @Override
            public URL getGitUrl() {
                return null;
            }
            
            @Override
            public String getImageName() {
                return null;
            }

            @Override
            public String getCode() {
                return null;
            }

            @Override
            public String getProperty(String propertyName, String defaultValue) {
                return null;
            }

            @Override
            public int getPropertyAsInt(String propertyName, int defaultValue) {
                return 0;
            }
            
            @Override
            public Map<String, String> getProperties() {
                return Collections.emptyMap();
            }

            @Override
            public String getEnv(String envName) {
                return null;
            }

            @Override
            public Map<String, String> getEnvs() {
                return Collections.emptyMap();
            }
        };
    }
}
