package eu.atos.paas.data;

import java.net.URL;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Application {

    @JsonProperty
    private String name;
    
    @JsonProperty
    private URL url;

    public Application() {
        this("", null);
    }
    
    public Application(String name) {
        this(name, null);
    }
    
    public Application(String name, URL url) {
        this.name = name;
        this.url = url;
    }
    
    public String getName() {
        return name;
    }

    public URL getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return String.format("Application [name=%s, url=%s]", name, url);
    }
}
