package eu.atos.paas.resources;

import static org.testng.AssertJUnit.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import eu.atos.paas.data.Application;
import eu.atos.paas.data.Provider;
import eu.atos.paas.dummy.DummyClient;

/**
 * This test creates a DummyPaasResource and tries to execute the operations inside PaasResource.
 * 
 * Operations with actual cloud providers should be tested in integration tests.
 */
public class PaasResourceTest {

    private static final String APP_NAME = "APP_NAME";
    private DummyResource resource;
    private DummyHttpHeaders headers;
    
    @BeforeMethod
    public void beforeMethod() {
    }

    @AfterMethod
    public void afterMethod() {
    }

    @BeforeClass
    public void beforeClass() {
        resource = new DummyResource(new DummyClient());
        /*
         * credentials are ignored in DummyResource, but they cannot be empty
         */
        Map<String, String> map = new HashMap<>();
        map.put(Constants.Headers.CREDENTIALS, "{}");
        headers = new DummyHttpHeaders(map);
    }

    @AfterClass
    public void afterClass() {
    }

    @Test(priority = 1)
    public void testGetProvider() {
        
        Provider provider = resource.getProvider();
        
        assertEquals(Constants.Providers.DUMMY, provider.getName());
    }

    @Test(priority = 2)
    public void testCreateApplication() throws IOException {
        
        Application app = new Application(APP_NAME, new URL("http://www.example.com"));
        InputStream is = new ByteArrayInputStream(new byte[] {});
            
        Response response = resource.createApplication(headers, app, is);
        
        assertEquals(Status.OK.getStatusCode(), response.getStatus());
    }
    
    @Test(priority = 10) 
    public void testStopApplication() {
        Response response = resource.stopApplication(APP_NAME, headers);
        assertEquals(Status.OK.getStatusCode(), response.getStatus());
    }
    
    @Test(priority = 20)
    public void testStartApplication() {
        Response response = resource.startApplication(APP_NAME, headers);
        assertEquals(Status.OK.getStatusCode(), response.getStatus());
    }
    
    @Test(priority = 30)
    public void testGetApplication() {
        Application app = resource.getApplication(APP_NAME, headers);
        assertEquals(APP_NAME, app.getName());
    }
    
    @Test(priority = 90) 
    public void testDeleteApplication() throws IOException {
        Response response = resource.deleteApplication(APP_NAME, headers);
        assertEquals(Status.OK.getStatusCode(), response.getStatus());
    }
    
    private static final class DummyHttpHeaders implements HttpHeaders {
        
        private MultivaluedMap<String, String> map = new MultivaluedHashMap<>();
        
        public DummyHttpHeaders(Map<String, String> map) {
            
            this.map = new MultivaluedHashMap<String, String>(map);
        }

        @Override
        public MultivaluedMap<String, String> getRequestHeaders() {
            return map;
        }

        @Override
        public List<String> getRequestHeader(String name) {
            return map.get(name);
        }

        @Override
        public MediaType getMediaType() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getLength() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Locale getLanguage() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getHeaderString(String name) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Date getDate() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map<String, Cookie> getCookies() {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<MediaType> getAcceptableMediaTypes() {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Locale> getAcceptableLanguages() {
            throw new UnsupportedOperationException();
        }
    }


}
