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

import java.util.Map;

import eu.atos.paas.DeployParametersImpl;


/**
 * 
 *
 * @author ATOS
 * @date 21/3/2016-14:03:16
 */
public class DeployParameters extends DeployParametersImpl
{

    public DeployParameters(String path, Map<String, String> properties) {
        super(path, null, properties);
    }
    
    public String getBuildpackUrl()
    {
        return properties.get("buildpack_url");
    }
}
