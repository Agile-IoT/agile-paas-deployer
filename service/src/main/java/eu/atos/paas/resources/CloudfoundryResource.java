package eu.atos.paas.resources;

import javax.ws.rs.Path;
import eu.atos.paas.PaasClient;


@Path("/cloudfoundry")
public class CloudfoundryResource extends CFBasedResource
{

    
    public CloudfoundryResource(PaasClient client)
    {
        super(client);
    }
    

}
