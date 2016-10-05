package eu.atos.paas;

import org.glassfish.jersey.media.multipart.MultiPartFeature;

import eu.atos.paas.resources.BluemixResource;
import eu.atos.paas.resources.CloudfoundryResource;
import eu.atos.paas.resources.HerokuResource;
import eu.atos.paas.resources.Openshift2Resource;
import eu.atos.paas.resources.PivotalResource;
import eu.atos.paas.PaasClientFactory;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;


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
        final HerokuResource heroku = new HerokuResource(paasClientFactory.getClient("heroku"));
        final CloudfoundryResource cloudfoundry = new CloudfoundryResource(paasClientFactory.getClient("cloudfoundry"));
        final PivotalResource pivotal = new PivotalResource(paasClientFactory.getClient("pivotal"));
        final BluemixResource bluemix = new BluemixResource(paasClientFactory.getClient("bluemix"));
        final Openshift2Resource openshift2 = new Openshift2Resource(paasClientFactory.getClient("openshift2"));

        environment.jersey().register(MultiPartFeature.class);
        environment.jersey().register(heroku);
        environment.jersey().register(cloudfoundry);
        environment.jersey().register(pivotal);
        environment.jersey().register(bluemix);
        environment.jersey().register(openshift2);
    }
    

}
