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
package eu.atos.paas;

import eu.atos.paas.cloudfoundry.CloudFoundryClient;
import eu.atos.paas.dummy.DummyClient;
import eu.atos.paas.heroku.HerokuClient;
import eu.atos.paas.openshift2.Openshift2Client;


/**
 * 
 *
 * @author ATOS
 * @date 21/3/2016-13:30:16
 */
public class PaasClientFactory {

    public PaasClient getClient(String provider) {
        switch (provider) {
            case "heroku":
                return new HerokuClient();
            case "cloudfoundry":
            case "bluemix":
            case "pivotal":
                return new CloudFoundryClient();
            case "openshift2":    
                return new Openshift2Client();
            case "dummy":
                return new DummyClient();
            case "openshift3":
            default:
                throw new IllegalArgumentException("Provider " + provider + " not supported");
        }
    }
    
    
}
