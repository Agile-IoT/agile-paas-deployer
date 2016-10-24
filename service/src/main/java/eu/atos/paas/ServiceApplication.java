package eu.atos.paas;

import java.util.HashMap;
import java.util.Map;

import org.glassfish.jersey.media.multipart.MultiPartFeature;

import eu.atos.paas.resources.ApiResource;
import eu.atos.paas.resources.CFBasedResource;
import eu.atos.paas.resources.HerokuResource;
import eu.atos.paas.resources.Openshift2Resource;
import eu.atos.paas.resources.PaaSResource;
import eu.atos.paas.PaasClientFactory;
import eu.atos.paas.data.Provider;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;


public class ServiceApplication extends Application<ServiceConfiguration>
{
    private PaasClientFactory paasClientFactory;


    public static void main(String[] args) throws Exception
    {
        PaasClientFactory factory = new PaasClientFactory();

        new ServiceApplication(factory).run(args);
    }


    public ServiceApplication(PaasClientFactory paasClientFactory)
    {
        this.paasClientFactory = paasClientFactory;
    }


    @Override
    public String getName()
    {
        return "unified-paas-service";
    }


    @Override
    public void run(ServiceConfiguration configuration, Environment environment) throws Exception
    {
        /*
         * swagger conf
         */
        environment.jersey().register(new ApiListingResource());
        BeanConfig config = new BeanConfig();
        config.setTitle("PaaS Unified Service");
        config.setResourcePackage(this.getClass().getPackage().getName());
        config.setScan(true);

        final HerokuResource heroku = new HerokuResource(paasClientFactory.getClient("heroku"));
        final CFBasedResource cloudfoundry = new CFBasedResource(paasClientFactory.getClient("cloudfoundry"), 
                new Provider("CloudFoundry", "https://www.example.com"));
        final CFBasedResource pivotal = new CFBasedResource (paasClientFactory.getClient("pivotal"),
                new Provider("Pivotal", "https://api.run.pivotal.io"));
        final CFBasedResource bluemix = new CFBasedResource (paasClientFactory.getClient("bluemix"),
                new Provider("Bluemix", "https://api.ng.bluemix.net"));
        final Openshift2Resource openshift2 = new Openshift2Resource(paasClientFactory.getClient("openshift2"));
        
        Map<String, PaaSResource> resourcesMap = new HashMap<>();
        resourcesMap.put("heroku", heroku);
        resourcesMap.put("cloudfoundry", cloudfoundry);
        resourcesMap.put("pivotal", pivotal);
        resourcesMap.put("bluemix", bluemix);
        resourcesMap.put("openshift2", openshift2);
        ApiResource api = new ApiResource(resourcesMap);

        environment.jersey().register(MultiPartFeature.class);
        environment.jersey().register(api);
    }

    
    @Override
    public void initialize(Bootstrap<ServiceConfiguration> bootstrap) {
    }

}
