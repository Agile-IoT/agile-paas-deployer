package eu.atos.paas.resources;

import javax.ws.rs.Path;
import eu.atos.paas.PaasClient;


@Path("/bluemix")
public class BluemixResource extends CFBasedResource
{

    
    public BluemixResource(PaasClient client)
    {
        super(client);
    }

    
}
