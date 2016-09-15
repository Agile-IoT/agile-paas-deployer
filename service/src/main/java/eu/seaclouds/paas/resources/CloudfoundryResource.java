package eu.seaclouds.paas.resources;

import javax.ws.rs.Path;
import eu.seaclouds.paas.PaasClient;


@Path("/cloudfoundry")
public class CloudfoundryResource extends CFBasedResource
{

    
    public CloudfoundryResource(PaasClient client)
    {
        super(client);
    }
    

}
