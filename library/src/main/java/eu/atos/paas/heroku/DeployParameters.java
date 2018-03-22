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
