package eu.seaclouds.paas.resources;

import javax.ws.rs.Path;
import eu.seaclouds.paas.PaasClient;


@Path("/pivotal")
public class PivotalResource extends CFBasedResource
{

    
    public PivotalResource(PaasClient client)
    {
        super(client);
    }

    
}
