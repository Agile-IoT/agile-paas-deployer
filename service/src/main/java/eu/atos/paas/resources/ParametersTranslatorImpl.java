/*******************************************************************************
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
 *******************************************************************************/
package eu.atos.paas.resources;

import java.io.File;
import java.util.Collections;

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
                Collections.<String, String>emptyMap());
        return result;
    }

}
