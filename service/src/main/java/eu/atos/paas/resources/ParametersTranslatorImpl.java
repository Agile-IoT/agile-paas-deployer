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
package eu.atos.paas.resources;

import java.io.File;

import eu.atos.paas.DeployParametersImpl;
import eu.atos.paas.PaasSession.DeployParameters;
import eu.atos.paas.data.ApplicationToCreate;

public class ParametersTranslatorImpl implements ParametersTranslator {

    @Override
    public DeployParameters translate(ApplicationToCreate application, File uploadedFile) {
        
        String path = uploadedFile != null? uploadedFile.getAbsolutePath() : null;
        
        DeployParametersImpl result = new DeployParametersImpl(
                path,
                application.getGitUrl(), 
                application.getProperties());
        return result;
    }

}
