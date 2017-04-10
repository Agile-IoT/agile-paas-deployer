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
package eu.atos.paas.heroku;

import java.net.URL;
import java.util.Collections;

import eu.atos.paas.DeployParametersImpl;
import eu.atos.paas.PaasSession;


/**
 * 
 *
 * @author ATOS
 * @date 21/3/2016-14:03:24
 */
public class DeployParameters extends DeployParametersImpl implements PaasSession.DeployParameters {

    
    public DeployParameters(URL gitUrl, String buildPackUrl) {
        super(null, gitUrl, Collections.singletonMap(Properties.BUILDPACK_URL, buildPackUrl));
    }

    public DeployParameters(String path, String buildPackUrl) {
        super(path, null, Collections.singletonMap(Properties.BUILDPACK_URL, buildPackUrl));
    }

    public String getBuildpackUrl()
    {
        return getProperty(Properties.BUILDPACK_URL);
    }
}
