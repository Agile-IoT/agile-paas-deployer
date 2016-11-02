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
        
        Module m = new Module(app, Collections.<String, Object>emptyMap());
        
        assertEquals("http://app.example.com", m.getUrl().toString());
    }

    @Test
    public void testRightURIFromCloudFoundry() {
        
        List<String> uris = Collections.singletonList("http://app.example.com");
        CloudApplication app = new CloudApplication("name", "command", null, 0, 0, uris, null, null);
        
        Module m = new Module(app, Collections.<String, Object>emptyMap());
        
        assertEquals("http://app.example.com", m.getUrl().toString());
    }
}
