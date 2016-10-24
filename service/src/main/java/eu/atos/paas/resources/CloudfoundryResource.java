package eu.atos.paas.resources;

import javax.ws.rs.Path;
import eu.atos.paas.PaasClient;
import eu.atos.paas.data.Provider;
import io.swagger.annotations.Api;


@Api(value="/cloudfoundry")
@Path("/cloudfoundry")
public class CloudfoundryResource extends CFBasedResource
{

    
    public CloudfoundryResource(PaasClient client)
    {
        super(client, new Provider("CloudFoundry", "https://www.example.com"));
    }
    

}
