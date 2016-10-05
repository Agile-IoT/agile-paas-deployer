package eu.atos.paas.resources;

import javax.ws.rs.Path;
import eu.atos.paas.PaasClient;


@Path("/pivotal")
public class PivotalResource extends CFBasedResource
{

    
    public PivotalResource(PaasClient client)
    {
        super(client);
    }

    
}
