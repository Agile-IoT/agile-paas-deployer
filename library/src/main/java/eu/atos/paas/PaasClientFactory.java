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
