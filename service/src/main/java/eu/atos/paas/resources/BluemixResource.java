package eu.atos.paas.resources;

import javax.ws.rs.Path;
import eu.atos.paas.PaasClient;
import eu.atos.paas.data.Provider;


@Path("/bluemix")
public class BluemixResource extends CFBasedResource
{

    
    public BluemixResource(PaasClient client)
    {
        super(client, new Provider("Bluemix", "https://api.ng.bluemix.net"));
    }

    
}
