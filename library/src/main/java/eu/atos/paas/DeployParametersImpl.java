/**
 * Copyright 2017 Atos
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

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import eu.atos.paas.PaasSession.DeployParameters;

public class DeployParametersImpl implements DeployParameters {

    private String path;
    private URL gitUrl;
    protected Map<String, String> properties;
    
    public static DeployParametersImpl fromPath(String path, Map<String, String> properties) {
        return new DeployParametersImpl(path, null, properties);
    }
    
    public static DeployParametersImpl fromGitUrl(URL gitUrl, Map<String, String> properties) {
        return new DeployParametersImpl(null, gitUrl, properties);
    }
    
    public DeployParametersImpl(String path, URL gitUrl, Map<String, String> properties) {
        this.path = path;
        this.gitUrl = gitUrl;
        this.properties = new HashMap<>(properties);
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public URL getGitUrl() {
        return gitUrl;
    }

//    @Override
//    public String getBuildpackUrl() {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    @Override
//    public String getCartridge() {
//        // TODO Auto-generated method stub
//        return null;
//    }

    @Override
    public String getProperty(String propertyName) {
        return properties.get(propertyName);
    }


}
