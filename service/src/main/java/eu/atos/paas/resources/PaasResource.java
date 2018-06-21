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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.atos.paas.Module;
import eu.atos.paas.PaasClient;
import eu.atos.paas.PaasSession;
import eu.atos.paas.PaasSession.DeployParameters;
import eu.atos.paas.PaasSession.ScaleCommand;
import eu.atos.paas.PaasSession.ScaleUpDownCommand;
import eu.atos.paas.PaasSession.StartStopCommand;
import eu.atos.paas.credentials.Credentials;
import eu.atos.paas.credentials.CredentialsUtils;
import eu.atos.paas.data.Application;
import eu.atos.paas.data.ApplicationToCreate;
import eu.atos.paas.data.CredentialsMap;
import eu.atos.paas.data.CredentialsMap.Base64Transformer;
import eu.atos.paas.data.CredentialsMap.PlainTransformer;
import eu.atos.paas.data.Provider;
import eu.atos.paas.resources.exceptions.AuthenticationException;
import eu.atos.paas.resources.exceptions.CredentialsParsingException;
import eu.atos.paas.resources.exceptions.EntityNotFoundException;
import eu.atos.paas.resources.exceptions.ValidationException;
import io.swagger.annotations.ApiOperation;


/**
 * Parent of Paas resources.
 * 
 * Exception handling from library:
 * <li>PaasProviderException : 502
 * <li>AuthenticationException : 401
 * <li>NotFoundException : 404
 * <li>AlreadyExistsException : 409
 * <li>UnsupportedOperationException : 501
 * <li>Any other runtime exception : 500
 * 
 * Wrong input should be detected in validation and return 400.
 *
 */
public abstract class PaasResource
{
    private static Logger log = LoggerFactory.getLogger(PaasResource.class);
    private static PlainTransformer plainTransformer = new PlainTransformer();
    private static Base64Transformer base64Transformer = new Base64Transformer(plainTransformer);
    private final ClientMap clientMap;
    protected final Provider provider;
    private ParametersTranslator parametersTranslator;
    
    public enum OperationResult
    {
        OK,
        WARNING,
        ERROR
    }
    
    
    /**
     * 
     * @param client
     */
    public PaasResource(Provider provider, ClientMap clientMap, ParametersTranslator parametersTranslator)
    {
        this.clientMap = Objects.requireNonNull(clientMap);
        this.provider = Objects.requireNonNull(provider);
        this.parametersTranslator = Objects.requireNonNull(parametersTranslator);
        
        /*
         * Check provider.defaultVersion has a client
         */
        if (clientMap.get(provider.getDefaultVersion()) == null) {
            throw new IllegalArgumentException(
                    "Trying to construct a PaasResource where provider default version [" + 
                    provider.getDefaultVersion() + "] do not have a client in clientMap");
            
        }
    }

    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Provider getProvider()
    {
        return provider;
    }


    /**
     * 
     * Examples:
     *  HEROKU: 
     *      curl http://localhost:8080/heroku/applications -X POST -F file=@"<FILE>" -F model={\"name\":\"<APP_NAME>\"} -H"Content-Type: multipart/form-data" -H"apikey:<API_KEY>"
     *  CLOUD FOUNDRY:
     *      curl http://localhost:8080/pivotal/applications -X POST -F file=@"<FILE>" -F model={\"name\":\"<APP_NAME>\"} -H"Content-Type: multipart/form-data"  -H"credentials:<API_URL>" -H"credentials:<USER>" -H"credentials:<PASSWORD>" -H"credentials:<ORG>" -H"credentials:<SPACE>"  -H"credentials:<TRUE_FALSE>"
     *  
     * @param headers
     * @param form
     * @return
     */
    @POST
    @Path("/applications")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="Creates a new application. Credentials are set in 'credential' headers")
    public Application createApplication(@Context HttpHeaders headers, FormDataMultiPart form)
    {
        PaasSession session = getSession(headers);
        
        FormDataBodyPart filePart = form.getField(Constants.MultiPartFields.FILE);
        FormDataBodyPart modelPart = form.getField(Constants.MultiPartFields.MODEL);

        if (modelPart == null) {
            throw new ValidationException("ApplicationToCreate model not found");
        }
        modelPart.setMediaType(MediaType.APPLICATION_JSON_TYPE);
        
        File uploadedFile = null;
        try {
        
            /*
             * Parse multiparts into a (ApplicationToCreate, File)
             */
            ApplicationToCreate application = modelPart.getValueAs(ApplicationToCreate.class);
            if (filePart != null) {
                /*
                 * setArtifact() just for input validation
                 */
                application.setArtifact(filePart.getEntityAs(InputStream.class));
                uploadedFile = saveToFile(application.getArtifact());
            }
            application.validate();
            
            log.info("createApplication({})", application.getName());
            return createApplicationImpl(session, application, uploadedFile);
        
        } catch (IOException e)
        {
            throw new WebApplicationException(e);
        }
        finally
        {
            if (uploadedFile != null) {
                uploadedFile.delete();
            }
        }
    }

    /**
     * Created for testing purposes
     * 
     * @see http://stackoverflow.com/questions/14456547/how-to-unit-test-handling-of-incoming-jersey-multipart-requests
     */
    public Application createApplication(HttpHeaders headers, ApplicationToCreate application) {
        PaasSession session = getSession(headers);

        application.validate();
        
        File uploadedFile = null;
        try {
            if (application.getArtifact() != null) {
                
                uploadedFile = saveToFile(application.getArtifact());
            }
            return createApplicationImpl(session, application, uploadedFile);
        } catch (IOException e)
        {
            throw new WebApplicationException(e);
        }
        finally
        {
            if (uploadedFile != null) {
                uploadedFile.delete();
            }
        }
    }
    
    /*
     * At this point, ApplicationToCreate is valid.
     */
    private Application createApplicationImpl(
            PaasSession session, ApplicationToCreate application, File uploadedFile) {
        
        Application result;

        DeployParameters params = parametersTranslator.translate(application, uploadedFile);
            

        Module m = session.deploy(application.getName(), params);
        
        result = new Application(m.getName(), m.getUrl());
        
        return result;
    }


    @GET
    @Path("applications")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="Return the existent applications")
    public List<Application> getApplications(@Context HttpHeaders headers) {
        log.info("getApplications()");
        @SuppressWarnings("unused")
        PaasSession session = getSession(headers);

        /*
         * TODO
         */
        return Collections.<Application>emptyList();
    }
    
    @GET
    @Path("applications/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="Return the status of an application")
    public Application getApplication(@PathParam("name") String name, @Context HttpHeaders headers) {
        log.info("getApplication({})", name);
        PaasSession session = getSession(headers);
        
        Module m = session.getModule(name);

        if (m == null) {
            throw new EntityNotFoundException("applications/" + name);
        }
        return new Application(m.getName(), m.getUrl());
    }


    @DELETE
    @Path("/applications/{name}")
    @ApiOperation(value="Removes an application in the provider")
    public Response deleteApplication(@PathParam("name") String name, @Context HttpHeaders headers)
    {
        log.info("deleteApplication({})", name);
        PaasSession session = getSession(headers);

        session.undeploy(name);

        // Response
        return generateJSONResponse(Response.Status.OK, OperationResult.OK,
                                    "DELETE /applications/" + name,
                                    "application " + name + " deleted");
    }


    @PUT
    @Path("/applications/{name}/start")
    @ApiOperation(value="Starts an application")
    public Response startApplication(@PathParam("name") String name, @Context HttpHeaders headers)
    {
        log.info("startApplication({})", name);
        PaasSession session = getSession(headers);

        Module m = session.getModule(name);
        session.startStop(m, StartStopCommand.START);

        // Response
        return generateJSONResponse(Response.Status.OK, OperationResult.OK,
                                    "PUT /applications/" + name + "/start",
                                    "application " + name + " started: " + m.getUrl());
    }


    @PUT
    @Path("/applications/{name}/stop")
    @ApiOperation(value="Stops an application")
    public Response stopApplication(@PathParam("name") String name, @Context HttpHeaders headers)
    {
        log.info("stopApplication({})", name);
        PaasSession session = getSession(headers);

        Module m = session.getModule(name);
        session.startStop(m, StartStopCommand.STOP);

        // Response
        return generateJSONResponse(Response.Status.OK, OperationResult.OK,
                                    "PUT /applications/" + name + "/stop",
                                    "application " + name + " stopped");
    }
    
    
    /**
     * @param updown Must have value in {"up", "down"}
     */
    @PUT
    @Path("/applications/{name}/scale/{updown}")
    @ApiOperation(value="Scales up/down an application adding or removing an instance")
    public Response scaleUpDownApplication(@PathParam("name") String name, @PathParam("updown") String updown, @Context HttpHeaders headers)
    {
        log.info("scaleUpDownApplication({}, {})", name, updown);
        PaasSession session = getSession(headers);

        Module m = session.getModule(name);
        if ("up".equalsIgnoreCase(updown)) {
            session.scaleUpDown(m, ScaleUpDownCommand.SCALE_UP_INSTANCES);
        }
        else if ("down".equalsIgnoreCase(updown)) {
            session.scaleUpDown(m, ScaleUpDownCommand.SCALE_DOWN_INSTANCES);
        }
        else {
            // Response
            return generateJSONResponse(Response.Status.OK, OperationResult.ERROR,
                                        "PUT /applications/" + name + "/scale/" + updown, 
                                        "application " + name + " NOT scaled");
        }

        // Response
        return generateJSONResponse(Response.Status.OK, OperationResult.OK,
                                    "PUT /applications/" + name + "/scale/" + updown, 
                                    "application " + name + " scaled instances: " + updown);
    }
    
    
    @PUT
    @Path("/applications/{name}/scale/{type}/{value}")
    @ApiOperation(value="Scales the resource 'type' an application with by 'value' units")
    public Response scaleApplication(@PathParam("name") String name, @PathParam("type") String type, 
            @PathParam("value") String value, @Context HttpHeaders headers)
    {
        log.info("scaleApplication({}, {}, {})", name, type, value);
        PaasSession session = getSession(headers);

        Module m = session.getModule(name);
        if ("instances".equalsIgnoreCase(type)) {
            session.scale(m, ScaleCommand.SCALE_INSTANCES, Integer.parseInt(value));
        }
        else if ("memory".equalsIgnoreCase(type)) {
            session.scale(m, ScaleCommand.SCALE_MEMORY, Integer.parseInt(value));
        }
        else if ("disk".equalsIgnoreCase(type)) {
            session.scale(m, ScaleCommand.SCALE_DISK, Integer.parseInt(value));
        }
        else {
            // Response
            return generateJSONResponse(Response.Status.OK, OperationResult.ERROR,
                                        "PUT /applications/" + name + "/scale/" + type + "/" + value, 
                                        "application " + name + " NOT scaled");
        }

        // Response
        return generateJSONResponse(Response.Status.OK, OperationResult.OK,
                                    "PUT /applications/" + name + "/scale/" + type + "/" + value, 
                                    "application " + name + " scaled: " + type + " - " + value);
    }
    
    
    /**
     * 
     * Examples for service parameter:
     *     HEROKU: cleardb:ignite
     *     CLOUD FOUNDRY:cleardb:mycleardb:spark
     * @param name
     * @param service
     * @param headers
     * @return
     */
    public abstract Response bindApplication(@PathParam("name") String name, @PathParam("service") String service, @Context HttpHeaders headers);
    
    
    /**
     * 
     * @param name
     * @param service
     * @param headers
     * @return
     */
    public abstract Response unbindApplication(@PathParam("name") String name, @PathParam("service") String service, @Context HttpHeaders headers);

    /**
     * Build a eu.atos.paas.credentials.Credentials instance from a CredentialsMap.
     * 
     * @param credentialsMap CredentialsMap created from the request headers.
     * @return A instance of Credentials to be used in paas-library classes.
     * @throws IllegalArgumentException
     */
    protected abstract Credentials buildCredentialsFromFieldsMap(CredentialsMap credentialsMap)
        throws IllegalArgumentException;
    
    final protected Credentials extractCredentials(HttpHeaders headers)
    {
        CredentialsMap credentialsMap = null;
        Credentials credentials;
        
        log.debug("Checking credentials...");

        List<String> crs = headers.getRequestHeader(Constants.Headers.CREDENTIALS);
        
        if (crs != null && !crs.isEmpty() && crs.size() == 1) {
            String header = crs.get(0);
            log.debug("Credentials header: {}", CredentialsUtils.shadowJsonCredentials(header));
            try {
                if (header.trim().startsWith("{")) {
                    credentialsMap = plainTransformer.deserialize(header);
                }
                else {
                    credentialsMap = base64Transformer.deserialize(header);
                }
                credentials = buildCredentialsFromFieldsMap(credentialsMap);
                
                if (credentials == null) {
                    log.error("Credentials cannot be null. Check {}.buildCredentialsFromFieldsMap()", 
                            provider.getName());
                    throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
                }
                return credentials;
            } catch (IllegalArgumentException e) {
                throw new CredentialsParsingException(e.getMessage());
            }
        }
        else {
            throw new CredentialsParsingException("Credentials header not found or duplicated");
        }
    }
    
    protected File saveToFile(InputStream is) throws FileNotFoundException, IOException {
        File file = File.createTempFile("up-tmp-file", ".tmp");
        saveToFile(is, file);
        return file;
    }
    
    protected void saveToFile(InputStream is, File file) throws FileNotFoundException, IOException
    {
        try (OutputStream os = new FileOutputStream(file)) {
            int read = 0;

            byte[] bytes = new byte[1024];

            while ((read = is.read(bytes)) != -1)
            {
                os.write(bytes, 0, read);
            }
            os.flush();
        }
        finally {
            is.close();
        }
    }


    /**
     * 
     * @param status
     * @param res
     * @param path
     * @param message
     * @return
     */
    protected Response generateJSONResponse(Response.Status status, OperationResult res, String path, String message)
    {
        log.debug(res.name() + " / " + message);
        
        String json = "{\"result\": \"" + res.name() + "\", " +
                      "\"path\": \"" + path + "\", " +
                      "\"message\": \"" + message + "\"}";
        
        return Response.status(status).type(MediaType.APPLICATION_JSON).entity(json).build();
    }
    
    
    /**
     * Credentials error Response
     * @param path
     * @return
     */
    protected Response generateCredentialsErrorJSONResponse(String path)
    {
        log.debug("ERROR / Error extracting credentials");
        
        String json = "{\"result\": \"" + OperationResult.ERROR.name() + "\", " +
                      "\"path\": \"" + path + "\", " +
                      "\"message\": \"Error extracting credentials\"}";
        
        return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(json).build();
    }
    

    /**
     * Gets session from the client using the credentials or reuse an existing session
     */
    protected PaasSessionProxy getSession(HttpHeaders headers) {
        Credentials credentials = extractCredentials(headers);
        String version = getVersionOrDefault(headers, provider);
        PaasSessionProxy sessionProxy;
        try {
            if (!clientMap.containsKey(version)) {
                throw new ValidationException(
                        "Version '" + version + "' is not valid for provider '" + provider.getName() +
                        "'. Valid versions are " + Arrays.toString(provider.getVersions().toArray()));
            }
            PaasClient client = clientMap.get(version);
            PaasSession session = client.getSession(credentials);
            sessionProxy = new PaasSessionProxy(session);
        } catch (eu.atos.paas.AuthenticationException e) {
            throw new AuthenticationException();
        } catch (UnsupportedOperationException e) {
            throw new AuthenticationException(e.getMessage());
        }
        return sessionProxy;
    }

    /** 
     * Extract from headers the ProviderVersion if specified; if not, the default version for the provider. 
     * In case of multiple ProviderVersion headers, the first one is picked. 
     * If the header is empty, the default version is returned.
     */
    private String getVersionOrDefault(HttpHeaders headers, Provider provider) {
        List<String> versions = headers.getRequestHeader(Constants.Headers.PROVIDER_VERSION);
        if (versions == null || versions.size() == 0 || versions.get(0).isEmpty()) {
            return provider.getDefaultVersion();
        }
        return versions.get(0);
    }
}
