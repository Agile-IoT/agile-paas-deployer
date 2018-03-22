/**
 *  Copyright (c) 2017 Atos, and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License 2.0
 *  which accompanies this distribution, and is available at
 *  https://www.eclipse.org/legal/epl-2.0/
 *
 *  Contributors:
 *  Atos - initial implementation
 */
package eu.atos.paas.serviceloader.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.atos.paas.cloudfoundry.CloudFoundryClient;
import eu.atos.paas.data.Provider;
import eu.atos.paas.heroku.HerokuClient;
import eu.atos.paas.openshift2.Openshift2Client;
import eu.atos.paas.openshift3.Openshift3Client;
import eu.atos.paas.resources.CFBasedResource;
import eu.atos.paas.resources.HerokuResource;
import eu.atos.paas.resources.OpenShiftResource;
import eu.atos.paas.resources.Constants.Providers;
import eu.atos.paas.serviceloader.ResourceSet.ResourceDescriptor;
import eu.atos.paas.serviceloader.impl.GenericServiceImpl.ResourceParameters;

import static org.testng.AssertJUnit.*;

public class GenericServiceImplTest {

    @Test
    public void serializeParameters() throws JsonGenerationException, JsonMappingException, IOException {
        
        Map<String, ResourceParameters> map = new LinkedHashMap<>();
        
        map.put(
                "heroku", 
                new ResourceParameters(
                        HerokuResource.class.getName(), 
                        new Provider(Providers.HEROKU, "https://api.heroku.com/", HerokuClient.VERSION),
                        new String[] { HerokuClient.class.getName() }
                )
        );
        map.put(
                "cloudfoundry",
                new ResourceParameters(
                        CFBasedResource.class.getName(),
                        new Provider(Providers.CLOUDFOUNDRY, "https://www.example.com", CloudFoundryClient.VERSION),
                        new String[] { CloudFoundryClient.class.getName() }

                )
        );
        map.put(
                "pivotal",
                new ResourceParameters(
                        CFBasedResource.class.getName(),
                        new Provider(Providers.PIVOTAL, "https://api.run.pivotal.io", CloudFoundryClient.VERSION),
                        new String[] { CloudFoundryClient.class.getName() }
                )
        );
        map.put(
                "bluemix",
                new ResourceParameters(
                        CFBasedResource.class.getName(),
                        new Provider(Providers.BLUEMIX, "https://api.ng.bluemix.net", CloudFoundryClient.VERSION),
                        new String[] { CloudFoundryClient.class.getName() }
                )
        );
        map.put(
                "openshift",
                new ResourceParameters(
                        OpenShiftResource.class.getName(),
                        new Provider(
                                Providers.OPENSHIFT, 
                                "http://www.example.com",
                                new String[] { Openshift2Client.VERSION, Openshift3Client.VERSION },
                                Openshift2Client.VERSION
                        ),
                        new String[] { Openshift2Client.class.getName(), Openshift3Client.class.getName() }
                )
        );
        map.put(
                "openshift.com",
                new ResourceParameters(
                        OpenShiftResource.class.getName(),
                        new Provider(
                                Providers.OPENSHIFT_ONLINE, 
                                "https://openshift.redhat.com",
                                new String[] { Openshift2Client.VERSION, Openshift3Client.VERSION },
                                Openshift2Client.VERSION
                        ),
                        new String[] { Openshift2Client.class.getName(), Openshift3Client.class.getName() }
                )
        );
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(System.out, map);
    }
    
    @Test
    public void deserializeConfigFile() throws JsonParseException, JsonMappingException, IOException {
        InputStream is = this.getClass().getResourceAsStream("/resources.conf.json");
        
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<Map<String, ResourceParameters>> ref = new TypeReference<Map<String, ResourceParameters>>() {};

        Map<String, ResourceParameters> map = mapper.readValue(is, ref);
        System.out.println(map);
    }
    
    @Test
    public void getResources() {
        
        InputStream is = this.getClass().getResourceAsStream("/resources.conf.json");
        GenericServiceImpl service = new GenericServiceImpl();
        
        Set<ResourceDescriptor> set = service.getResources(is);
        assertEquals(6, set.size());
    }
    
    @Test
    public void getResourcesShouldFail() {
        
        GenericServiceImpl service = new GenericServiceImpl();
        
        try {
            service.getResources(null);
            fail("Should have failed");
        } catch (RuntimeException e) {
        }
    }

}
