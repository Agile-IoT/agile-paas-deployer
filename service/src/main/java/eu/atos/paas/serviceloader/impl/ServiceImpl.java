/**
 * Copyright 2016 Atos
 * Contact: Atos <roman.sosa@atos.net>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package eu.atos.paas.serviceloader.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import eu.atos.paas.cloudfoundry.CloudFoundryClient;
import eu.atos.paas.data.Provider;
import eu.atos.paas.heroku.HerokuClient;
import eu.atos.paas.openshift2.Openshift2Client;
import eu.atos.paas.openshift3.Openshift3Client;
import eu.atos.paas.resources.CFBasedResource;
import eu.atos.paas.resources.ClientMap;
import eu.atos.paas.resources.HerokuResource;
import eu.atos.paas.resources.OpenShiftResource;
import eu.atos.paas.serviceloader.ResourceSet;
import static eu.atos.paas.resources.Constants.Providers;

public class ServiceImpl implements ResourceSet {

    private Set<ResourceDescriptor> set;
    
    public ServiceImpl() {
        set = new HashSet<>();
        
        final HerokuResource heroku = new HerokuResource(
                new Provider(Providers.HEROKU, "https://api.heroku.com/", HerokuClient.VERSION),
                ClientMap.builder()
                    .client(new HerokuClient())
                    .build()
        );
        
        final CFBasedResource cloudfoundry = newCFBasedResource(
                new Provider(Providers.CLOUDFOUNDRY, "https://www.example.com", CloudFoundryClient.VERSION)
        );
        
        final CFBasedResource pivotal = newCFBasedResource(
                new Provider(Providers.PIVOTAL, "https://api.run.pivotal.io", CloudFoundryClient.VERSION)
        );

        final CFBasedResource bluemix = newCFBasedResource(
                new Provider(Providers.BLUEMIX, "https://api.ng.bluemix.net", CloudFoundryClient.VERSION)
        );
        
        final OpenShiftResource openshift = newOpenShiftResource(
                new Provider(
                        Providers.OPENSHIFT, 
                        "http://www.example.com",
                        new String[] { Openshift2Client.VERSION, Openshift3Client.VERSION },
                        Openshift2Client.VERSION
                )
        );
        
        final OpenShiftResource openshiftOnline = newOpenShiftResource(
                new Provider(
                        Providers.OPENSHIFT_ONLINE, 
                        "https://openshift.redhat.com",
                        new String[] { Openshift2Client.VERSION, Openshift3Client.VERSION },
                        Openshift2Client.VERSION
                )
        );
        
        set.add(new ResourceDescriptor("heroku", heroku));
        set.add(new ResourceDescriptor("cloudfoundry", cloudfoundry));
        set.add(new ResourceDescriptor("pivotal", pivotal));
        set.add(new ResourceDescriptor("bluemix", bluemix));
        set.add(new ResourceDescriptor("openshift", openshift));
        set.add(new ResourceDescriptor("openshift.com", openshiftOnline));
    }

    @Override
    public Set<ResourceDescriptor> getResources() {
        return Collections.unmodifiableSet(set);
    }
                
    private OpenShiftResource newOpenShiftResource(Provider provider) {
        return new OpenShiftResource(
                provider,
                ClientMap.builder()
                    .client(new Openshift2Client())
                    .client(new Openshift3Client())
                    .build()
        );
    }

    private CFBasedResource newCFBasedResource(Provider provider) {
        return new CFBasedResource(
                provider,
                ClientMap.builder()
                    .client(new CloudFoundryClient())
                    .build()
        );
    }
}
