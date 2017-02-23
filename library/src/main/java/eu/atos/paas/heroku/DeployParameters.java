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

import eu.atos.paas.PaasSession;


/**
 * 
 *
 * @author ATOS
 * @date 21/3/2016-14:03:24
 */
public class DeployParameters implements PaasSession.DeployParameters {

    
    private String path;
    private String buildpack_url;
    private URL gitUrl;
    
    public DeployParameters(String path) {
        this.path = path;
        this.buildpack_url = "";
    }
    
    
    public DeployParameters(String path, String buildpack_url) {
        this.path = path;
        this.buildpack_url = buildpack_url;
    }
    
    public DeployParameters(URL gitUrl) {
        this.gitUrl = gitUrl;
    }
    
    @Override
    public String getPath() {
        return path;
    }
    
    
    @Override
    public String getBuildpackUrl()
    {
        return buildpack_url;
    }


    @Override
    public String getCartridge()
    {
        return null;
    }


    @Override
    public URL getGitUrl() {
        return gitUrl;
    }
    
    
}
