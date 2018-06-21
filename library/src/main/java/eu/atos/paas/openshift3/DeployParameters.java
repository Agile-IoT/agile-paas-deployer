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
package eu.atos.paas.openshift3;

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
