package eu.atos.paas.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
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
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openshift.client.cartridge.IStandaloneCartridge;

import eu.atos.paas.data.Application;
import eu.atos.paas.data.CredentialsMap;
import eu.atos.paas.credentials.Credentials;
import eu.atos.paas.credentials.UserPasswordCredentials;
import eu.atos.paas.data.Provider;
import eu.atos.paas.Module;
import eu.atos.paas.PaasClient;
import eu.atos.paas.PaasSession;
import eu.atos.paas.ServiceApp;
import eu.atos.paas.openshift2.DeployParameters;
import eu.atos.paas.resources.Constants.Providers;


public class Openshift2Resource extends PaaSResource
{
    private static Logger log = LoggerFactory.getLogger(Openshift2Resource.class);

    
    /**
     * 
     * Constructor
     * @param client
     */
    public Openshift2Resource(PaasClient client)
    {
        super(client, new Provider(Providers.OPENSHIFT2, "http://api.openshift.com"));
    }
    
    
    @POST
    @Path("/applications")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Application createApplication(@Context HttpHeaders headers, FormDataMultiPart form)
    {
        throw new WebApplicationException("method not implemented", Response.Status.BAD_REQUEST);
    }

    
    @POST
    @Path("/applications/git")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createApplication2(@Context HttpHeaders headers, @FormParam("appName") String appname, 
            @FormParam("appGitUrl") String appGitUrl)
    {
        try
        {
            log.info("createApplication({})", appname);
            
            PaasSession session = getSession(headers);

            Application result;
            Module m = session.deploy(appname, new DeployParameters(appGitUrl, IStandaloneCartridge.NAME_JBOSSEWS));
            result = new Application(m.getName(), m.getUrl());
            
            // Response
            return generateJSONResponse(Response.Status.OK, OperationResult.OK,
                                        "POST /applications/",
                                        "application " + result.getName() + " created / deployed: " + result.getUrl());
        }
        catch (Exception e)
        {
            // Response
            return generateJSONResponse(Response.Status.INTERNAL_SERVER_ERROR, OperationResult.ERROR,
                                        "POST /applications/",
                                        "application not created / deployed: " + e.getMessage());
        }
    }
    
    
    @PUT
    @Path("/applications/{name}/bind/{service}")
    @Override
    public Response bindApplication(@PathParam("name") String name, @PathParam("service") String service, @Context HttpHeaders headers)
    {
        log.info("bindApplication({}, {})", name, service);
        PaasSession session = getSession(headers);
        
        Module m = session.getModule(name);
        // openshift ... mysql-5.5
        session.bindToService(m, new ServiceApp(service));

        // Response
        return generateJSONResponse(Response.Status.OK, OperationResult.OK,
                                    "PUT /applications/" + name + "/bind/" + service, 
                                    "service " + service + " binded to app: " + name);
    }
    

    @PUT
    @Path("/applications/{name}/unbind/{service}")
    @Override
    public Response unbindApplication(@PathParam("name") String name, @PathParam("service") String service, @Context HttpHeaders headers)
    {
        log.info("unbindApplication({}, {})", name, service);
        PaasSession session = getSession(headers);
        
        Module m = session.getModule(name);
        // openshift ... mysql-5.5
        session.unbindFromService(m, new ServiceApp(service));

        // Response
        return generateJSONResponse(Response.Status.OK, OperationResult.OK,
                                    "PUT /applications/" + name + "/unbind/" + service, 
                                    "service " + service + " unbinded from app: " + name);
    }


    @Override
    protected Credentials buildCredentialsFromFieldsMap(CredentialsMap credentialsMap)
            throws IllegalArgumentException {
        
        return new UserPasswordCredentials(credentialsMap);
    }

}
