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
package eu.atos.paas.openshift2;

import java.net.URL;
import java.util.Collections;

import eu.atos.paas.DeployParametersImpl;
import eu.atos.paas.PaasSession;


/**
 * 
 *
 * @author ATOS
 * @date 21/3/2016-13:49:55
 */
public class DeployParameters extends DeployParametersImpl implements PaasSession.DeployParameters {

    public DeployParameters(String path, String cartridge) {
        super(path, null, Collections.singletonMap(Properties.CARTRIDGE, cartridge));
    }

    public DeployParameters(URL gitUrl, String cartridge) {
        super(null, gitUrl, Collections.singletonMap(Properties.CARTRIDGE, cartridge));
    }

    public String getCartridge()
    {
        return getProperty(Properties.CARTRIDGE, null);
    }

}
