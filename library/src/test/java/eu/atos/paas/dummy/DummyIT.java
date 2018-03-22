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
package eu.atos.paas.dummy;

import java.net.URL;

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
            public String getProperty(String propertyName) {
                return null;
            }
        };
    }
}
