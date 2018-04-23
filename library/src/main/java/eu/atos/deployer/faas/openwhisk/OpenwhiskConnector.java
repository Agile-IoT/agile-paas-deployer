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
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.atos.deployer.faas.openwhisk.model.ActionInformation;
import eu.atos.deployer.faas.openwhisk.model.ErrorResult;
import eu.atos.deployer.faas.openwhisk.model.Exec;
import eu.atos.deployer.faas.openwhisk.model.ExecutionResponse;
import eu.atos.deployer.faas.openwhisk.model.ExecutionResult;
import eu.atos.deployer.faas.openwhisk.model.Logs;
import eu.atos.deployer.faas.openwhisk.model.PackageInformation;

/**
 * 
 *
 * @author ATOS
 * @date 19/2/2016-16:01:46
 */
public class OpenwhiskConnector {

    // logger
    private static final Logger logger = LoggerFactory.getLogger(OpenwhiskConnector.class);

    // Rest client
    private WebTarget rootTarget;

    /**
     * @param serverUrl
     * @param login
     * @param passwd
     */
    public OpenwhiskConnector(URL serverUrl, String user, String password) {
        logger.info(">> Connecting to Openwhisk ...");
        logger.info(
                ">> Be aware that the trust certificates are disabled. Please reprogram if you put this code in production ...");
        TrustManager[] certs = new TrustManager[] { new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
        } };

        SSLContext ctx = null;
        try {
            ctx = SSLContext.getInstance("TLS");
            ctx.init(null, certs, new SecureRandom());
        } catch (java.security.GeneralSecurityException e) {
            throw new OpenwhiskException(e);
        }

        HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());

        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        try {
            clientBuilder.sslContext(ctx);
            clientBuilder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception e) {
            throw new OpenwhiskException(e);
        }

        Client restClient = clientBuilder
                .withConfig(new ClientConfig())
                .register(ObjectMapperContextResolver.class)
                .register(JacksonFeature.class).register(new Authenticator(user, password))
                .register(MultiPartFeature.class)
                .build();

        this.rootTarget = restClient.target(serverUrl.toString());
    }

    /**
     * Get the default namespace
     * 
     * @return String
     */
    public String getDefaultNamespace() throws OpenwhiskException {
        WebTarget appTarget = rootTarget.path(Constants.Path.ROOT + Constants.Path.NAMESPACES);
        Response response = appTarget.request(MediaType.APPLICATION_JSON).get();
        List<String> namespaces = response.readEntity(new GenericType<List<String>>() {
            {
            }
        });
        String namespace = "";
        if (namespaces != null)
            switch (namespaces.size()) {
            case 0:
                throw new OpenwhiskException("No namespace detected, at least one must exist");
            case 1:
                namespace = namespaces.get(0);
                break;
            default:
                throw new OpenwhiskException(
                        "More than one namespace detected, please use correct methid where you specify the namespace");
            }
        return namespace;
    }

    /**
     * Get the action list form the default namespace
     * 
     * @param limit
     *            Number of entities to include in the result (0-200). The
     *            default limit is 30. A value of 0 sets the limit to the
     *            maximum.
     * @param skip
     *            Number of entities to skip in the result.
     * @return List<ActionInformation>
     */
    public List<ActionInformation> getActionList(Integer limit, Integer skip) throws OpenwhiskException {
        logger.info(">> Getting action list form default namespace ...");
        return getActionList(getDefaultNamespace(), limit, skip);
    }

    /**
     * Get the action list form the indicated namespace
     * 
     * @param namespace
     * @param limit
     *            Number of entities to include in the result (0-200). The
     *            default limit is 30. A value of 0 sets the limit to the
     *            maximum.
     * @param skip
     *            Number of entities to skip in the result.
     * @return List<ActionInformation>
     */
    public List<ActionInformation> getActionList(String namespace, Integer limit, Integer skip)
            throws OpenwhiskException {
        logger.info(">> Getting action list form '" + namespace + "' namespace ...");
        WebTarget appTarget = rootTarget
                .path(Constants.Path.ROOT + Constants.Path.NAMESPACES + "/" + namespace + Constants.Path.ACTIONS + "/");
        if (limit != null)
            appTarget = appTarget.queryParam(Constants.Query.LIMIT, limit);
        if (skip != null)
            appTarget = appTarget.queryParam(Constants.Query.SKIP, skip);
        return getReadingEntity(appTarget, new GenericType<List<ActionInformation>>() {
            {
            }
        });
    }

    /**
     * Get a specific actionfrom the default namespace
     * 
     * @param actionname
     * @param code
     *            Include action code in the result
     * @return ActionInformation
     */
    public ActionInformation getActionInformation(String actionname, Boolean code) throws OpenwhiskException {
        logger.info(">> Getting action information form default namespace ...");
        return getActionInformation(getDefaultNamespace(), actionname, code);
    }

    /**
     * Deploy an action in the indicated namespace
     * 
     * @param namespace
     * @param limit
     *            Number of entities to include in the result (0-200). The
     *            default limit is 30. A value of 0 sets the limit to the
     *            maximum.
     * @param skip
     *            Number of entities to skip in the result.
     * @return ActionInformation
     */
    public ActionInformation getActionInformation(String namespace, String actionname, Boolean code)
            throws OpenwhiskException {
        logger.info(">> Getting action information form '" + namespace + "' namespace ...");
        WebTarget appTarget = rootTarget.path(Constants.Path.ROOT + Constants.Path.NAMESPACES + "/" + namespace
                + Constants.Path.ACTIONS + "/" + actionname);
        if (code != null)
            appTarget = appTarget.queryParam(Constants.Query.CODE, code);
        return getReadingEntity(appTarget, ActionInformation.class);
    }

    /**
     * Deploy an action in the default namespace
     * 
     * @param actionName
     *            name of action
     * @param code
     *            method in source code format
     * @param kind
     *            kind of code, see eu.atos.paas.openwhisk.Constants.Kind
     * @param overwrite
     *            Overwrite item if it exists. Default is false.
     * @return ActionInformation
     */
    public ActionInformation deployActionFromCode(String actionName, String code, String kind, Boolean overwrite)
            throws OpenwhiskException {
        logger.info(">> Creating action '" + actionName + "' in default namespace ...");
        return deployActionFromCode(getDefaultNamespace(), actionName, code, kind, overwrite);
    }

    /**
     * Deploy an action in the indicated namespace
     * 
     * @param namespace
     * @param actionName
     *            name of action
     * @param code
     *            method in source code format
     * @param kind
     *            kind of code, see eu.atos.paas.openwhisk.Constants.Kind
     * @param overwrite
     *            Overwrite item if it exists. Default is false.
     * @return ActionInformation
     */
    public ActionInformation deployActionFromCode(String namespace, String actionName, String code, String kind,
            Boolean overwrite) throws OpenwhiskException {
        logger.info(">> Creating action '" + actionName + "' in '" + namespace + "' namespace ...");
        WebTarget appTarget = rootTarget.path(Constants.Path.ROOT + Constants.Path.NAMESPACES + "/" + namespace
                + Constants.Path.ACTIONS + "/" + actionName);
        if (overwrite != null)
            appTarget = appTarget.queryParam(Constants.Query.OVERWRITE, overwrite);
        ActionInformation action = new ActionInformation();
        action.setName(actionName);
        action.setNamespace(namespace);
        Exec exec = new Exec();
        exec.setKind(kind);
        exec.setCode(code);
        action.setExec(exec);
        return putReadingEntity(appTarget, action, ActionInformation.class);
    }

    /**
     * Deploy an action in the default namespace
     * 
     * @param actionName
     *            name of action
     * @param pathToFile
     *            path to jar or zip file
     * @param kind
     *            kind of code, see eu.atos.paas.openwhisk.Constants.Kind java
     *            or zip
     * @param overwrite
     *            Overwrite item if it exists. Default is false.
     * @return ActionInformation
     */
    public ActionInformation deployActionFromCode(String actionName, Path pathToFile, String kind, boolean overwrite,
            String mainMethod) throws OpenwhiskException {
        logger.info(">> Creating action '" + actionName + "' in default namespace from jar or zip ...");
        return deployActionFromCode(getDefaultNamespace(), actionName, pathToFile, kind, overwrite, mainMethod);
    }

    /**
     * Deploy an action in the default namespace
     * 
     * @param actionName
     *            name of action
     * @param pathToFile
     *            path to jar or zip file
     * @param kind
     *            kind of code, see eu.atos.paas.openwhisk.Constants.Kind java
     *            or zip
     * @param overwrite
     *            Overwrite item if it exists. Default is false.
     * @return ActionInformation
     */
    public ActionInformation deployActionFromCode(String namespace, String actionName, Path pathToFile, String kind,
            Boolean overwrite, String mainMethod) throws OpenwhiskException {
        try {
            logger.info(">> Creating action '" + actionName + "' in '" + namespace + "' namespace from jar or zip ...");
            WebTarget appTarget = rootTarget.path(Constants.Path.ROOT + Constants.Path.NAMESPACES + "/" + namespace
                    + Constants.Path.ACTIONS + "/" + actionName);
            if (overwrite != null)
                appTarget = appTarget.queryParam(Constants.Query.OVERWRITE, overwrite);
            ActionInformation action = new ActionInformation();
            action.setName(actionName);
            action.setNamespace(namespace);
            Exec exec = new Exec();
            exec.setKind(kind);
            exec.setCode(readFileContentConvertWithBase64(pathToFile));
            exec.setBinary(true);
            exec.setMain(mainMethod);
            action.setExec(exec);
            return putReadingEntity(appTarget, action, ActionInformation.class);
        } catch (IOException e) {
            throw new OpenwhiskException(e);
        }
    }

    // the jar or zip file has to be converted to base64. It is how openwhisk
    // has implemented this
    private String readFileContentConvertWithBase64(Path pathToFile) throws IOException {
        byte[] content = Files.readAllBytes(pathToFile);
        return new String(Base64.getEncoder().encode(content));
    }

    /**
     * Invoke the execution of an action from the default namespace
     * 
     * @param actionName
     * @param blocking
     * @param result
     * @return ExecutionResponse
     */
    public ExecutionResponse invokeAction(String actionName, Map<String, String> params, Boolean blocking,
            Boolean result, Integer timeout) {
        logger.info(">> Invoking action '" + actionName + "' in default namespace ...");
        return invokeAction(getDefaultNamespace(), actionName, params, blocking, result, timeout);
    }

    /**
     * Invoke the execution of an action from the default namespace
     * 
     * @param namespace
     * @param actionName
     * @param blocking
     *            Blocking or non-blocking invocation. Default is non-blocking.
     * @param result
     *            Return only the result of a blocking activation. Default is
     *            false.
     * @param timeout
     *            Wait no more than specified duration in milliseconds for a
     *            blocking response. Default value and max allowed timeout are
     *            60000.
     * @return ExecutionResponse
     */
    public ExecutionResponse invokeAction(String namespace, String actionName, Map<String, String> params,
            Boolean blocking, Boolean result, Integer timeout) {
        logger.info(">> Invoking action f'" + actionName + "' in '" + namespace + "' namespace");

        WebTarget appTarget = rootTarget.path(Constants.Path.ROOT + Constants.Path.NAMESPACES + "/" + namespace
                + Constants.Path.ACTIONS + "/" + actionName);
        if (blocking != null)
            appTarget = appTarget.queryParam(Constants.Query.BLOCKING, blocking);
        if (result != null)
            appTarget = appTarget.queryParam(Constants.Query.RESULT, result);
        if (timeout != null)
            appTarget = appTarget.queryParam(Constants.Query.TIMEOUT, timeout);
        Response response = appTarget.request(MediaType.APPLICATION_JSON).post(Entity.json(params));
        ExecutionResponse callresult = null;
        if (!blocking) {
            callresult = response.readEntity(ExecutionResponse.class);
        } else {
            if (result) {
                String resultAsString = response.readEntity(String.class);
                callresult = new ExecutionResponse();
                callresult.setResult(resultAsString);
            } else {
                callresult = response.readEntity(ExecutionResponse.class);
            }
        }
        return callresult;
    }

    /**
     * Remove an action from the default namespace
     * 
     * @param actionName
     * @return ActionInformation
     */
    public ActionInformation deleteAction(String actionName) {
        logger.info(">> Delete action '" + actionName + "' in default namespace ...");
        return deleteAction(getDefaultNamespace(), actionName);
    }

    /**
     * Remove an action from the default namespace
     * 
     * @param namespace
     * @param actionName
     * @return ActionInformation
     */
    public ActionInformation deleteAction(String namespace, String actionName) {
        logger.info(">> Delete action f'" + actionName + "' in '" + namespace + "' namespace");
        WebTarget appTarget = rootTarget.path(Constants.Path.ROOT + Constants.Path.NAMESPACES + "/" + namespace
                + Constants.Path.ACTIONS + "/" + actionName);
        return deleteReadingEntity(appTarget, ActionInformation.class);
    }

    /**
     * Get a specific actionfrom the default namespace
     * 
     * @param actionname
     *            Name of item
     * @param limit
     *            Number of entities to include in the result (0-200). The
     *            default limit is 30. A value of 0 sets the limit to the
     *            maximum.
     * @param skip
     *            Number of entities to skip in the result.
     * @param since
     *            Only include entities later than this timestamp (measured in
     *            milliseconds since Thu, 01 Jan 1970)
     * @param upto
     *            Only include entities earlier than this timestamp (measured in
     *            milliseconds since Thu, 01 Jan 1970)
     * @param docs
     *            Whether to include full entity description.
     * @return List<ExecutionResponse>
     */

    public List<ExecutionResponse> getActivationList(String actionname, Integer limit, Integer skip, Integer since,
            Integer upto, Boolean docs) throws OpenwhiskException {
        logger.info(">> Getting activation list form default namespace ...");
        return getActivationList(getDefaultNamespace(), actionname, limit, skip, since, upto, docs);
    }

    /**
     * Get the action list form the indicated namespace
     * 
     * @param namespace
     * @param actionname
     *            Name of item
     * @param limit
     *            Number of entities to include in the result (0-200). The
     *            default limit is 30. A value of 0 sets the limit to the
     *            maximum.
     * @param skip
     *            Number of entities to skip in the result.
     * @param since
     *            Only include entities later than this timestamp (measured in
     *            milliseconds since Thu, 01 Jan 1970)
     * @param upto
     *            Only include entities earlier than this timestamp (measured in
     *            milliseconds since Thu, 01 Jan 1970)
     * @param docs
     *            Whether to include full entity description.
     * @return List<ExecutionResponse>
     */
    public List<ExecutionResponse> getActivationList(String namespace, String actionname, Integer limit, Integer skip,
            Integer since, Integer upto, Boolean docs) throws OpenwhiskException {
        logger.info(">> Getting activation list form '" + namespace + "' namespace ...");
        WebTarget appTarget = rootTarget.path(
                Constants.Path.ROOT + Constants.Path.NAMESPACES + "/" + namespace + Constants.Path.ACTIVATIONS + "/");
        if (actionname != null)
            appTarget = appTarget.queryParam(Constants.Query.NAME, actionname);
        if (limit != null)
            appTarget = appTarget.queryParam(Constants.Query.LIMIT, limit);
        if (skip != null)
            appTarget = appTarget.queryParam(Constants.Query.SKIP, skip);
        if (since != null)
            appTarget = appTarget.queryParam(Constants.Query.SINCE, since);
        if (upto != null)
            appTarget = appTarget.queryParam(Constants.Query.UPTO, upto);
        if (docs != null)
            appTarget = appTarget.queryParam(Constants.Query.DOCS, docs);
        return getReadingEntity(appTarget, new GenericType<List<ExecutionResponse>>() {
            {
            }
        });
    }

    /**
     * Get a specific activation the default namespace
     * 
     * @param activationid
     * @return ExecutionResponse
     */

    public ExecutionResponse getActivationInformation(String activationid) throws OpenwhiskException {
        logger.info(">> Getting activation information form default namespace ...");
        return getActivationInformation(getDefaultNamespace(), activationid);
    }

    /**
     * Get the activation form the indicated namespace
     * 
     * @param namespace
     * @param activationid
     * @return ExecutionResponse
     */
    public ExecutionResponse getActivationInformation(String namespace, String activationid) throws OpenwhiskException {
        logger.info(">> Getting activation list form '" + namespace + "' namespace ...");
        WebTarget appTarget = rootTarget.path(Constants.Path.ROOT + Constants.Path.NAMESPACES + "/" + namespace
                + Constants.Path.ACTIVATIONS + "/" + activationid);
        return getReadingEntity(appTarget, ExecutionResponse.class);
    }

    /**
     * Get logs from a specific activation in the default namespace
     * 
     * @param activationid
     * @return Logs
     */

    public List<String> getActivationLogs(String activationid) throws OpenwhiskException {
        logger.info(">> Getting logs form activation form default namespace ...");
        return getActivationLogs(getDefaultNamespace(), activationid);
    }

    /**
     * Get logs from the activation form the indicated namespace
     * 
     * @param namespace
     * @param activationid
     * @return Logs
     */
    public List<String> getActivationLogs(String namespace, String activationid) throws OpenwhiskException {
        logger.info(">> Getting logs from activation form '" + namespace + "' namespace ...");
        WebTarget appTarget = rootTarget.path(Constants.Path.ROOT + Constants.Path.NAMESPACES + "/" + namespace
                + Constants.Path.ACTIVATIONS + "/" + activationid + Constants.Path.LOGS);
        Logs logs = getReadingEntity(appTarget, Logs.class);
        return logs.getLogs();
    }

    /**
     * Get result from a specific activation the default namespace
     * 
     * @param activationid
     * @return String
     */

    public ExecutionResult getActivationResult(String activationid) throws OpenwhiskException {
        logger.info(">> Getting logs form activation form default namespace ...");
        return getActivationResult(getDefaultNamespace(), activationid);
    }

    /**
     * Get result from the activation form the indicated namespace
     * 
     * @param namespace
     * @param activationid
     * @return ExecutionResult
     */
    public ExecutionResult getActivationResult(String namespace, String activationid) throws OpenwhiskException {
        logger.info(">> Getting logs from activation form '" + namespace + "' namespace ...");
        WebTarget appTarget = rootTarget.path(Constants.Path.ROOT + Constants.Path.NAMESPACES + "/" + namespace
                + Constants.Path.ACTIVATIONS + "/" + activationid + Constants.Path.RESULT);
        return getReadingEntity(appTarget, ExecutionResult.class);
    }

    /**
     * Get a package list from the default namespace
     * 
     * @param public
     *            Include publicly shared entitles in the result.
     * @param limit
     *            Number of entities to include in the result (0-200). The
     *            default limit is 30. A value of 0 sets the limit to the
     *            maximum.
     * @param skip
     *            Number of entities to skip in the result.
     * @return
     */

    public List<PackageInformation> getPackagesList(Boolean includepublic, Integer limit, Integer skip)
            throws OpenwhiskException {
        logger.info(">> Getting packages list form default namespace ...");
        return getPackagesList(getDefaultNamespace(), includepublic, limit, skip);
    }

    /**
     * Get the package list form the indicated namespace
     * 
     * @param namespace
     * @param actionname
     *            Name of item
     * @param limit
     *            Number of entities to include in the result (0-200). The
     *            default limit is 30. A value of 0 sets the limit to the
     *            maximum.
     * @param skip
     *            Number of entities to skip in the result.
     * @param since
     *            Only include entities later than this timestamp (measured in
     *            milliseconds since Thu, 01 Jan 1970)
     * @param upto
     *            Only include entities earlier than this timestamp (measured in
     *            milliseconds since Thu, 01 Jan 1970)
     * @param docs
     *            Whether to include full entity description.
     * @return List<ExecutionResponse>
     */
    public List<PackageInformation> getPackagesList(String namespace, Boolean includepublic, Integer limit,
            Integer skip) throws OpenwhiskException {
        logger.info(">> Getting packages list form '" + namespace + "' namespace ...");
        WebTarget appTarget = rootTarget.path(
                Constants.Path.ROOT + Constants.Path.NAMESPACES + "/" + namespace + Constants.Path.PACKAGES + "/");
        if (includepublic != null)
            appTarget = appTarget.queryParam(Constants.Query.PUBLIC, includepublic);
        if (limit != null)
            appTarget = appTarget.queryParam(Constants.Query.LIMIT, limit);
        if (skip != null)
            appTarget = appTarget.queryParam(Constants.Query.SKIP, skip);
        return getReadingEntity(appTarget, new GenericType<List<PackageInformation>>() {
            {
            }
        });
    }

    /**
     * Get a package information from the default namespace
     * 
     * @param public
     *            Include publicly shared entitles in the result.
     * @param limit
     *            Number of entities to include in the result (0-200). The
     *            default limit is 30. A value of 0 sets the limit to the
     *            maximum.
     * @param skip
     *            Number of entities to skip in the result.
     * @return
     */

    public PackageInformation getPackageInformation(String name) throws OpenwhiskException {
        logger.info(">> Getting packages list form default namespace ...");
        return getPackageInformation(getDefaultNamespace(), name);
    }

    /**
     * Get the package information form the indicated namespace
     * 
     * @param namespace
     * @param actionname
     *            Name of item
     * @param limit
     *            Number of entities to include in the result (0-200). The
     *            default limit is 30. A value of 0 sets the limit to the
     *            maximum.
     * @param skip
     *            Number of entities to skip in the result.
     * @param since
     *            Only include entities later than this timestamp (measured in
     *            milliseconds since Thu, 01 Jan 1970)
     * @param upto
     *            Only include entities earlier than this timestamp (measured in
     *            milliseconds since Thu, 01 Jan 1970)
     * @param docs
     *            Whether to include full entity description.
     * @return List<ExecutionResponse>
     */
    public PackageInformation getPackageInformation(String namespace, String name) throws OpenwhiskException {
        logger.info(">> Getting packages list form '" + namespace + "' namespace ...");
        WebTarget appTarget = rootTarget.path(Constants.Path.ROOT + Constants.Path.NAMESPACES + "/" + namespace
                + Constants.Path.PACKAGES + "/" + name);
        return getReadingEntity(appTarget, PackageInformation.class);
    }

    /**
     * Create a package in the default namespace
     * 
     * @param name
     *            Name of package
     * @param overwrite
     *            Overwrite item if it exists. Default is false.
     * @return
     */

    public PackageInformation createPackages(String name, Boolean overwrite) throws OpenwhiskException {
        logger.info(">> Create package in the default namespace ...");
        return createPackages(getDefaultNamespace(), name, overwrite);
    }

    /**
     * Create a package in the indicated namespace
     * 
     * @param namespace
     * @param actionname
     *            Name of item
     * @param limit
     *            Number of entities to include in the result (0-200). The
     *            default limit is 30. A value of 0 sets the limit to the
     *            maximum.
     * @param skip
     *            Number of entities to skip in the result.
     * @param since
     *            Only include entities later than this timestamp (measured in
     *            milliseconds since Thu, 01 Jan 1970)
     * @param upto
     *            Only include entities earlier than this timestamp (measured in
     *            milliseconds since Thu, 01 Jan 1970)
     * @param docs
     *            Whether to include full entity description.
     * @return List<ExecutionResponse>
     */
    public PackageInformation createPackages(String namespace, String name, Boolean overwrite)
            throws OpenwhiskException {
        logger.info(">> Create package in the  '" + namespace + "' namespace ...");
        WebTarget appTarget = rootTarget.path(Constants.Path.ROOT + Constants.Path.NAMESPACES + "/" + namespace
                + Constants.Path.PACKAGES + "/" + name);
        if (overwrite != null)
            appTarget = appTarget.queryParam(Constants.Query.OVERWRITE, overwrite);

        PackageInformation apackage = new PackageInformation();
        apackage.setName(name);
        Response response = appTarget.request(MediaType.APPLICATION_JSON).put(Entity.json(apackage));
        return response.readEntity(PackageInformation.class);
    }

    public <T> T getReadingEntity(WebTarget appTarget, final GenericType<T> entityType) throws OpenwhiskException {
        Response response = appTarget.request(MediaType.APPLICATION_JSON).get();
        if (response.getStatus() == 200) {
            /*
             * String result = response.readEntity(String.class);
             * System.out.println(result);
             */
            return response.readEntity(entityType);
        } else
            throw new OpenwhiskException(response.readEntity(ErrorResult.class));
    }

    public <T> T getReadingEntity(WebTarget appTarget, final Class<T> entityType) throws OpenwhiskException {
        Response response = appTarget.request(MediaType.APPLICATION_JSON).get();
        if (response.getStatus() == 200) {
            /*
             * String result = response.readEntity(String.class);
             * System.out.println(result);
             */
            return response.readEntity(entityType);
        } else
            throw new OpenwhiskException(response.readEntity(ErrorResult.class));
    }

    public <T> T putReadingEntity(WebTarget appTarget, Object object, final Class<T> entityType)
            throws OpenwhiskException {
        Response response = appTarget.request(MediaType.APPLICATION_JSON).put(Entity.json(object));
        if (response.getStatus() == 200) {
            return response.readEntity(entityType);
        } else
            throw new OpenwhiskException(response.readEntity(ErrorResult.class));
    }

    public <T> T deleteReadingEntity(WebTarget appTarget, final Class<T> entityType) throws OpenwhiskException {
        Response response = appTarget.request(MediaType.APPLICATION_JSON).delete();
        if (response.getStatus() == 200) {
            /*
             * String result = response.readEntity(String.class);
             * System.out.println(result);
             */
            return response.readEntity(entityType);
        } else
            throw new OpenwhiskException(response.readEntity(ErrorResult.class));
    }
}
