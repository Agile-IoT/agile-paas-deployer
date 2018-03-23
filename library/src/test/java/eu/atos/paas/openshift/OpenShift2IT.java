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
