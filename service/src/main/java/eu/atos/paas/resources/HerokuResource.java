package eu.atos.paas.resources;

import java.util.List;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.atos.paas.Credentials;
import eu.atos.paas.Module;
import eu.atos.paas.PaasClient;
import eu.atos.paas.PaasSession;
import eu.atos.paas.ServiceApp;


@Path("/heroku")
public class HerokuResource extends PaaSResource
{


    private static Logger log = LoggerFactory.getLogger(HerokuResource.class);


    /**
     * 
     * @param client
     */
    public HerokuResource(PaasClient client)
    {
        super(client);
    }
    
    
    @PUT
    @Path("/applications/{name}/bind/{service}")
    @Override
    public Response bindApplication(@PathParam("name") String name, @PathParam("service") String service, @Context HttpHeaders headers)
    {
        log.info("bindApplication({}, {})", name, service);
        Credentials credentials = extractCredentials(headers);
        if (credentials == null) {
            // Error Response
            return generateCredentialsErrorJSONResponse("PUT /applications/" + name + "/bind/" + service);
        }
        
        PaasSession session = client.getSession(credentials);
        
        Module m = session.getModule(name);
        // heroku ... cleardb:ignite
        ServiceApp serviceapp = new ServiceApp(service);
        
        session.bindToService(m, serviceapp);

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
        Credentials credentials = extractCredentials(headers);
        if (credentials == null) {
            // Error Response
            return generateCredentialsErrorJSONResponse("PUT /applications/" + name + "/unbind/" + service);
        }
        
        PaasSession session = client.getSession(credentials);
        
        Module m = session.getModule(name);
        // heroku ... cleardb
        ServiceApp serviceapp = new ServiceApp(service);
        
        session.unbindFromService(m, serviceapp);

        // Response
        return generateJSONResponse(Response.Status.OK, OperationResult.OK,
                                    "PUT /applications/" + name + "/unbind/" + service, 
                                    "service " + service + " unbinded from app: " + name);
    }


    @Override
    protected Credentials extractCredentials(HttpHeaders headers)
    {
        Credentials credentials = null;

        List<String> apikeys = headers.getRequestHeader("apikey");
        if (apikeys != null && !apikeys.isEmpty())
        {
            credentials = new Credentials.ApiKeyCredentials(apikeys.get(0));
        }
        return credentials;
    }

    
}
