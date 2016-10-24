package eu.atos.paas.resources;

import javax.ws.rs.Path;
import eu.atos.paas.PaasClient;
import eu.atos.paas.data.Provider;


@Path("/pivotal")
public class PivotalResource extends CFBasedResource
{

    
    public PivotalResource(PaasClient client)
    {
        super(client, new Provider("Pivotal", "https://api.run.pivotal.io"));
    }

    
}
