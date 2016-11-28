package eu.atos.paas;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.atos.paas.resources.ApiResource;
import eu.atos.paas.resources.PaasResource;
import eu.atos.paas.serviceloader.ResourceSet;
import eu.atos.paas.serviceloader.ResourceSet.ResourceDescriptor;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;


public class ServiceApplication extends Application<ServiceConfiguration>
{
    private static Logger logger = LoggerFactory.getLogger(ServiceApplication.class);
    private static ServiceLoader<ResourceSet> resourceSetLoader = ServiceLoader.load(ResourceSet.class);

    public static void main(String[] args) throws Exception
    {
        new ServiceApplication().run(args);
    }


    public ServiceApplication()
    {
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

        /*
         * key: path; value: resource
         */
        Map<String, PaasResource> resourcesMap = new HashMap<>();

        /*
         * Each ResourceSet declares a collection of REST resources and its subpath.
         * The ResourceSet implementation is defined in META-INF/services/eu.atos.paas.serviceloader.ResourceSet file.
         */
        for (ResourceSet rs : resourceSetLoader) {
            logger.info("Reading ResourceDescriptors from {}", rs.getClass().getName());
            for (ResourceDescriptor descriptor : rs.getResources()) {
                resourcesMap.put(descriptor.getPath(), descriptor.getResource());
            }
        }
        ApiResource api = new ApiResource(resourcesMap);

        environment.jersey().register(MultiPartFeature.class);
        environment.jersey().register(api);
        
        for (Map.Entry<String, PaasResource> item : resourcesMap.entrySet()) {
            logger.info("Added resource /{} ({})", item.getKey(), item.getValue().getClass().getName());
        }

    }

    
    @Override
    public void initialize(Bootstrap<ServiceConfiguration> bootstrap) {
    }

}
