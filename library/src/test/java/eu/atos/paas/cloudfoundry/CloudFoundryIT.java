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
package eu.atos.paas.cloudfoundry;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import eu.atos.paas.AbstractProviderIT;
import eu.atos.paas.Groups;
import eu.atos.paas.PaasClient;
import eu.atos.paas.PaasClientFactory;
import eu.atos.paas.TestConfigProperties;
import eu.atos.paas.credentials.ApiUserPasswordOrgSpaceCredentials;

@Test(groups = Groups.CLOUDFOUNDRY)
public class CloudFoundryIT extends AbstractProviderIT {

    @BeforeClass
    protected void initialize() {
        System.out.println("---" + this.getClass().getName() + "---");
        super.initialize();
        
        PaasClient client = new PaasClientFactory().getClient("cloudfoundry");
        
        session = client.getSession(new ApiUserPasswordOrgSpaceCredentials(
                TestConfigProperties.getInstance().getCf_api(), 
                TestConfigProperties.getInstance().getCf_user(),
                TestConfigProperties.getInstance().getCf_password(), 
                TestConfigProperties.getInstance().getCf_org(), 
                TestConfigProperties.getInstance().getCf_space(), 
                TestConfigProperties.getInstance().isCf_trustSelfSignedCerts()));
        
        String path = this.getClass().getResource("/SampleApp1.war").getFile();
        this.params = new DeployParameters(path);
    }
    
    /*
     * This active wait leaves time for the previous staging to finish.
     * 
     * Priority parameter in annotation must be equal to parent
     */
    @Override
    @Test(priority = 40)
    public void startApplication() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.startApplication();
    }
    
}
