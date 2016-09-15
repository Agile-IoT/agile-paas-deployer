package eu.seaclouds.paas.resources;

import javax.ws.rs.Path;
import eu.seaclouds.paas.PaasClient;


@Path("/bluemix")
public class BluemixResource extends CFBasedResource
{

    
    public BluemixResource(PaasClient client)
    {
        super(client);
    }

    
}
