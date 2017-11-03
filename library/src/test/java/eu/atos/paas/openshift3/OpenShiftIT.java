/**
 * Copyright 2017 Atos
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
package eu.atos.paas.openshift3;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import eu.atos.paas.AbstractProviderIT;
import eu.atos.paas.DeployParametersImpl;
import eu.atos.paas.Groups;
import eu.atos.paas.PaasClient;
import eu.atos.paas.PaasClientFactory;
import eu.atos.paas.TestConfigProperties;
import eu.atos.paas.credentials.ApiUserPasswordCredentials;

@Test(groups = Groups.OPENSHIFT3)
public class OpenShiftIT extends AbstractProviderIT {

    @BeforeClass
    protected void initialize() {
        System.out.println("---" + this.getClass().getName() + "---");
        super.initialize();
        
        PaasClient client = new PaasClientFactory().getClient("openshift3");
        
        session = client.getSession(new ApiUserPasswordCredentials(
                TestConfigProperties.getInstance().getOp3_api(), 
                TestConfigProperties.getInstance().getOp3_user(),
                TestConfigProperties.getInstance().getOp3_password()));

        this.params = DeployParametersImpl.Builder.fromImageName("centos/mongodb-26-centos7")
            .env("MONGODB_ADMIN_PASSWORD", "a-super-password")
            .env("MONGODB_USER", "a-user")
            .env("MONGODB_PASSWORD", "a-password")
            .env("MONGODB_DATABASE", "a-db")
            .build();
    }
    
}
