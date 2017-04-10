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
package eu.atos.paas.openshift;

import java.net.MalformedURLException;
import java.net.URL;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.openshift.client.cartridge.IStandaloneCartridge;

import eu.atos.paas.AbstractProviderIT;
import eu.atos.paas.Groups;
import eu.atos.paas.PaasClient;
import eu.atos.paas.TestConfigProperties;
import eu.atos.paas.credentials.ApiUserPasswordCredentials;
import eu.atos.paas.openshift2.DeployParameters;
import eu.atos.paas.openshift2.Openshift2Client;

@Test(groups = Groups.OPENSHIFT2)
public class OpenShift2IT extends AbstractProviderIT {

    private static final String GIT_APP_URL = "https://github.com/OpenMEAP/openshift-openmeap-quickstart";

    private URL gitAppUrl;
    
    public OpenShift2IT() throws MalformedURLException {
        
        gitAppUrl = new URL(GIT_APP_URL);
    }
    
    @BeforeClass
    protected void initialize() {
        System.out.println("---" + this.getClass().getName() + "---");
        super.initialize();
        
        PaasClient client = new Openshift2Client();
        
        super.session = client.getSession(new ApiUserPasswordCredentials(
                TestConfigProperties.getInstance().getOp_api(),
                TestConfigProperties.getInstance().getOp_user(),
                TestConfigProperties.getInstance().getOp_password()));
        
        super.params = new DeployParameters(gitAppUrl, IStandaloneCartridge.NAME_JBOSSEWS);
    }
    
}
