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

import eu.atos.paas.PaasSession;


/**
 * 
 *
 * @author ATOS
 * @date 21/3/2016-13:49:55
 */
public class DeployParameters implements PaasSession.DeployParameters {

    
    private String path;
    private String cartridge;
    private URL gitUrl;

    
    public DeployParameters(URL gitUrl, String cartridge) {
        this.cartridge = cartridge;
        this.gitUrl = gitUrl;
    }
    
    
    @Override
    public String getPath() {
        return path;
    }


    @Override
    public String getCartridge()
    {
        return cartridge;
    }


    @Override
    public String getBuildpackUrl()
    {
        return null;
    }
    
    @Override
    public URL getGitUrl() {
        return gitUrl;
    }
}
