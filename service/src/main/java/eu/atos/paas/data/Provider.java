package eu.atos.paas.data;

import java.net.MalformedURLException;
import java.net.URL;

public class Provider {

    private String name;
    private URL url;
    
    public Provider(String name, String url) {
        this.name = name;
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }
    
    public Provider(String name, URL url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public URL getUrl() {
        return url;
    }
}
