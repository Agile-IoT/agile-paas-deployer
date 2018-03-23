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
