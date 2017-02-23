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
package eu.atos.paas.cloudfoundry;

import java.util.Collections;
import java.util.List;

import org.cloudfoundry.client.lib.domain.CloudApplication;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.*;

public class ModuleTest {

    @Test
    public void testMalformedURIFromCloudFoundry() {
        
        List<String> uris = Collections.singletonList("app.example.com");
        CloudApplication app = new CloudApplication("name", "command", null, 0, 0, uris, null, null);
        
        ModuleImpl m = new ModuleImpl(app, Collections.<String, Object>emptyMap());
        
        assertEquals("http://app.example.com", m.getUrl().toString());
    }

    @Test
    public void testRightURIFromCloudFoundry() {
        
        List<String> uris = Collections.singletonList("http://app.example.com");
        CloudApplication app = new CloudApplication("name", "command", null, 0, 0, uris, null, null);
        
        ModuleImpl m = new ModuleImpl(app, Collections.<String, Object>emptyMap());
        
        assertEquals("http://app.example.com", m.getUrl().toString());
    }
}
