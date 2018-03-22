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
//    public String getCartridge() {
//        // TODO Auto-generated method stub
//        return null;
//    }

    @Override
    public String getProperty(String propertyName) {
        return properties.get(propertyName);
    }


}
