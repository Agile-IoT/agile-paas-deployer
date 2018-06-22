/**
 * Copyright 2018 Atos
 * Contact: Atos <elena.garrido@atos.net>
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
package eu.atos.deployer.faas.openwhisk;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import eu.atos.deployer.faas.openwhisk.model.ActionInformation;
import eu.atos.deployer.faas.openwhisk.model.ExecutionResponse;
import eu.atos.faas.FaasSession;
import eu.atos.paas.AlreadyExistsException;
import eu.atos.paas.Module;
import eu.atos.paas.NotDeployedException;
import eu.atos.paas.NotFoundException;
import eu.atos.paas.PaasException;
import eu.atos.paas.PaasProviderException;
import eu.atos.paas.PaasSession;
import eu.atos.paas.PaasSession.DeployParameters.Properties;
import eu.atos.paas.ServiceApp;

public class OpenwhiskSession implements PaasSession, FaasSession {

    private OpenwhiskConnector connector;

    public OpenwhiskSession(URL serverUrl, String user, String password) {
        this.connector = new OpenwhiskConnector(serverUrl, user, password);
    }

    @Override
    public Module createApplication(String moduleName, DeployParameters params)
            throws PaasProviderException, AlreadyExistsException {
        // this is something to be checked. The wskadmin command is able to
        // create a new namespace and associate it to a user
        // it is not possible to --debug or --verbose the command. The source
        // code has to be checked in order to understand
        // how a namespace is created
        return deployCodeInOpenwhisk(moduleName, params, false);
    }

    @Override
    public Module updateApplication(String moduleName, DeployParameters params)
            throws NotFoundException, PaasProviderException {
        String[] parts = splitName(moduleName);
        String namespace = parts[0];
        String actionname = parts[1];
        try {
            connector.getActionInformation(namespace, actionname, false);
        } catch (OpenwhiskException ex) {
            throw new NotFoundException(moduleName + "has not been found");
        }
        return deployCodeInOpenwhisk(moduleName, params, true);
    }

    @Override
    public Module deploy(String moduleName, DeployParameters params)
            throws PaasProviderException, AlreadyExistsException {
        return deployCodeInOpenwhisk(moduleName, params, true);
    }

    private Module deployCodeInOpenwhisk(String moduleName, DeployParameters params, boolean override)
            throws PaasProviderException, AlreadyExistsException {
        ModuleImpl module = null;
        String[] parts = splitName(moduleName);
        String namespace = parts[0];
        String actionname = parts[1];
        try {
            ActionInformation actionInformation;
            String language = params.getProperty(Properties.LANGUAGE, Constants.Kind.DEFAULT);
            if (Constants.Kind.JAVA.equals(language)) {
                actionInformation = connector.deployActionFromCode(namespace, actionname, Paths.get(params.getPath()),
                        language, override, params.getProperty(Properties.MAIN, ""));
            } else {
                if (Constants.Kind.NODEJS.equals(language) || Constants.Kind.NODEJS6.equals(language)
                        || Constants.Kind.NODEJS8.equals(language) || Constants.Kind.DEFAULT.equals(language)) {

                    String code;
                    
                    if (!params.getCode().isEmpty()) {
                        code = params.getCode();
                    } else if (!params.getPath().isEmpty()) {
                        code = readFromFile(params.getPath(), params.getProperty("encoding", StandardCharsets.UTF_8.name())); 
                    } else {
                        throw new IllegalArgumentException("Code have not been supplied in DeployParameters"); 
                    }
                    actionInformation = connector.deployActionFromCode(namespace, actionname, code,
                            language, override);
                } else {
                    String languages = String.format("%s, %s, %s, %s, %s", Constants.Kind.JAVA, Constants.Kind.NODEJS,
                            Constants.Kind.NODEJS6, Constants.Kind.NODEJS8, Constants.Kind.DEFAULT);
                    throw new IllegalArgumentException("Only " + languages + " are accepted");
                }
            }
            module = new ModuleImpl(actionInformation);
        } catch (OpenwhiskException ex) {
            if ("resource already exists".equals(ex.getMessage()))
                throw new AlreadyExistsException(moduleName, ex);
            throw new PaasProviderException("Error deploying code", ex);
        }
        return module;
    }
    
    private String readFromFile(String path, String encoding) {
        byte[] content;
        Charset charset = Charset.forName(encoding);
        try {
            content = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read from file " + path, e);
        }
        return new String(content, charset);
    }

    @Override
    public void undeploy(String moduleName) throws NotFoundException, PaasProviderException {
        String[] parts = splitName(moduleName);
        String namespace = parts[0];
        String actionname = parts[1];
        try {
            connector.deleteAction(namespace, actionname);
        } catch (OpenwhiskException ex) {
            if ("The requested resource does not exist.".equals(ex.getMessage()))
                throw new NotFoundException(moduleName + " has not been found and cannot be deleted", ex);
            else
                throw ex;
        }
    }

    @Override
    public void startStop(Module module, StartStopCommand command)
            throws NotFoundException, NotDeployedException, PaasProviderException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void scaleUpDown(Module module, ScaleUpDownCommand command)
            throws PaasException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void scale(Module module, ScaleCommand command, int scale_value)
            throws PaasException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void bindToService(Module module, ServiceApp service) throws PaasException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unbindFromService(Module module, ServiceApp service) throws PaasException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Module getModule(String moduleName) throws PaasException {
        String[] parts = splitName(moduleName);
        String namespace = parts[0];
        String actionname = parts[1];
        Module result = null;
        try {
            ActionInformation actionInformation = connector.getActionInformation(namespace, actionname, false);
            result = new ModuleImpl(actionInformation);
        } catch (OpenwhiskException e) {
            // we have an exception when the action doesn't exist in openwhisk.
        }
        return result;
    }

    @Override
    public ExecutionResponse execute(String moduleName, Map<String, String> params,
            Map<String, Object> extraParameters) {
        String[] parts = splitName(moduleName);
        String namespace = parts[0];
        String actionname = parts[1];

        return connector.invokeAction(namespace, actionname, params,
                (Boolean) extraParameters.get(ExecutionParameters.Properties.SYNCHRONOUS),
                (Boolean) extraParameters.get(ExecutionParameters.Properties.RESULT),
                (Integer) extraParameters.get(ExecutionParameters.Properties.TIMEOUT));
    }

    private String[] splitName(String moduleName) {
        String[] parts = moduleName.split("/");
        if (parts.length > 2) {
            throw new IllegalArgumentException(moduleName + " is not a valid module name");
        } else if (parts.length < 2) {
            String defaultNamespace = connector.getDefaultNamespace();
            parts = new String[] { defaultNamespace, parts[0] };
        }
        return parts;
    }

}
