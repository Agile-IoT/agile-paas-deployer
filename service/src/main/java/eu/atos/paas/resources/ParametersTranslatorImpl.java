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

import eu.atos.paas.DeployParametersImpl;
import eu.atos.paas.PaasSession.DeployParameters;
import eu.atos.paas.data.ApplicationToCreate;

public class ParametersTranslatorImpl implements ParametersTranslator {

    @Override
    public DeployParameters translate(ApplicationToCreate application, File uploadedFile) {
        
        DeployParametersImpl.Builder b = getBuilder(application, uploadedFile);

        DeployParametersImpl result = b.build();
        
        return result;
    }

    public static DeployParametersImpl.Builder getBuilder(ApplicationToCreate application, File uploadedFile) {
        String path = uploadedFile != null? uploadedFile.getAbsolutePath() : null;
        
        DeployParametersImpl.Builder builder;
        if (path != null) {
            builder = DeployParametersImpl.Builder.fromPath(path);
            
        } else if (application.getGitUrl() != null) {
            builder = DeployParametersImpl.Builder.fromGitUrl(application.getGitUrl());
            
        } else if (application.getImageName() != null) {
            builder = DeployParametersImpl.Builder.fromImageName(application.getImageName());
        
        } else {
            throw new IllegalStateException(String.format("Not valid (%s, %s)", application, uploadedFile));
        }

        builder
            .envs(application.getEnvs())
            .properties(application.getProperties());
        return builder;
    }

}
