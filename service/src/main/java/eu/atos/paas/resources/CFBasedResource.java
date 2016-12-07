package eu.atos.paas.resources;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.atos.paas.Module;
import eu.atos.paas.PaasSession;
import eu.atos.paas.ServiceApp;
import eu.atos.paas.credentials.ApiUserPasswordOrgSpaceCredentials;
import eu.atos.paas.credentials.Credentials;
import eu.atos.paas.data.CredentialsMap;
import eu.atos.paas.data.Provider;


public class CFBasedResource extends PaasResource
{
    private static Logger log = LoggerFactory.getLogger(CFBasedResource.class);

    /**
     * 
     * @param client
     */
    public CFBasedResource(Provider provider, ClientMap clientMap)
    {
        super(provider, clientMap);
    }

    @PUT
    @Path("/applications/{name}/bind/{service}")
    @Override
    public Response bindApplication(@PathParam("name") String name, @PathParam("service") String service, @Context HttpHeaders headers)
    {
        log.info("bindApplication({}, {})", name, service);
        PaasSession session = getSession(headers);
        
        Module m = session.getModule(name);
        
        // cloud foundry ... cleardb:mycleardb:spark
        String[] servValues = service.split(":");
        
        if (servValues.length == 3)
        {
            ServiceApp serviceapp = new ServiceApp(servValues[0]);
            serviceapp.setServiceInstanceName(servValues[1]);
            serviceapp.setServicePlan(servValues[2]);
            
            session.bindToService(m, serviceapp);
        }
        else
        {
            // Response
            return generateJSONResponse(Response.Status.BAD_REQUEST, OperationResult.ERROR,
                                        "PUT /applications/" + name + "/bind/" + service, 
                                        "Credentials not found in request headers / lenght != 3");
        }

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
        
        // cloud foundry ... cleardb:mycleardb:spark
        String[] servValues = service.split(":");
        
        if (servValues.length == 3)
        {
            ServiceApp serviceapp = new ServiceApp(servValues[0]);
            serviceapp.setServiceInstanceName(servValues[1]);
            serviceapp.setServicePlan(servValues[2]);
            
            session.unbindFromService(m, serviceapp);
        }
        
        // Response
        return generateJSONResponse(Response.Status.OK, OperationResult.OK,
                                    "PUT /applications/" + name + "/unbind/" + service, 
                                    "service " + service + " unbinded from app: " + name);
    }

    @Override
    protected Credentials buildCredentialsFromFieldsMap(CredentialsMap credentialsMap) 
        throws IllegalArgumentException {
        
        /*
         * Set API field to provider URL in case using a specific CloudFoundry provider
         */
        if (!this.getProvider().getName().equals(Constants.Providers.CLOUDFOUNDRY)) {
            credentialsMap.put(ApiUserPasswordOrgSpaceCredentials.API, provider.getUrl().toString());
        }
        
        return new ApiUserPasswordOrgSpaceCredentials(credentialsMap);
    }
}
