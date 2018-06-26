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
package eu.atos.paas.openshift2;

import java.net.URL;
import java.util.Collections;

import eu.atos.paas.DeployParametersImpl;
import eu.atos.paas.PaasSession;


/**
 * 
 *
 * @author ATOS
 * @date 21/3/2016-13:49:55
 */
public class DeployParameters extends DeployParametersImpl implements PaasSession.DeployParameters {

    public DeployParameters(String path, String cartridge) {
        super(path, null, Collections.singletonMap(Properties.CARTRIDGE, cartridge));
    }

    public DeployParameters(URL gitUrl, String cartridge) {
        super(null, gitUrl, Collections.singletonMap(Properties.CARTRIDGE, cartridge));
    }

    public String getCartridge()
    {
        return getProperty(Properties.CARTRIDGE, null);
    }

}
