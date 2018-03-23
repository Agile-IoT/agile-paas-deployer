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
