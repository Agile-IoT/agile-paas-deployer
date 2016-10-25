package eu.atos.paas.serviceloader.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import eu.atos.paas.cloudfoundry.CloudFoundryClient;
import eu.atos.paas.data.Provider;
import eu.atos.paas.heroku.HerokuClient;
import eu.atos.paas.openshift2.Openshift2Client;
import eu.atos.paas.resources.CFBasedResource;
import eu.atos.paas.resources.HerokuResource;
import eu.atos.paas.resources.Openshift2Resource;
import eu.atos.paas.serviceloader.ResourceSet;

public class ServiceImpl implements ResourceSet {

    private Set<ResourceDescriptor> set;
    
    public ServiceImpl() {
        set = new HashSet<>();
        
        final HerokuResource heroku = new HerokuResource(new HerokuClient());
        final CFBasedResource cloudfoundry = new CFBasedResource(new CloudFoundryClient(), 
                new Provider("CloudFoundry", "https://www.example.com"));
        final CFBasedResource pivotal = new CFBasedResource (new CloudFoundryClient(),
                new Provider("Pivotal", "https://api.run.pivotal.io"));
        final CFBasedResource bluemix = new CFBasedResource (new CloudFoundryClient(),
                new Provider("Bluemix", "https://api.ng.bluemix.net"));
        final Openshift2Resource openshift2 = new Openshift2Resource(new Openshift2Client());
        
        set.add(new ResourceDescriptor("heroku", heroku));
        set.add(new ResourceDescriptor("cloudfoundry", cloudfoundry));
        set.add(new ResourceDescriptor("pivotal", pivotal));
        set.add(new ResourceDescriptor("bluemix", bluemix));
        set.add(new ResourceDescriptor("openshift", openshift2));
    }

    @Override
    public Set<ResourceDescriptor> getResources() {
        return Collections.unmodifiableSet(set);
    }

}
