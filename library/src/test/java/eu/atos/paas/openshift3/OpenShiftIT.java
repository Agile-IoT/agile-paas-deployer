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
