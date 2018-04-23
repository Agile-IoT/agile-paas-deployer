/**
 * Copyright 2018 Atos
 * Contact: Atos <elena.garrido@atos.net>
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
package eu.atos.deployer.faas.openwhisk;

import java.net.URL;
import java.util.Map;

import eu.atos.paas.DeployParametersImpl;
import eu.atos.paas.PaasSession;


/**
 * 
 *
 * @author ATOS
 * @date 21/3/2016-14:06:34
 */
public class DeployParameters extends DeployParametersImpl implements PaasSession.DeployParameters
{

    public DeployParameters(String path, URL gitUrl, Map<String, String> properties) {
        super(path, gitUrl, properties);
    }

}
