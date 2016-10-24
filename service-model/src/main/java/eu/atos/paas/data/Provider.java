package eu.atos.paas.data;

import java.net.MalformedURLException;
import java.net.URL;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Provider {

    private String name;
    private URL url;
    
    @JsonCreator
    public Provider(@JsonProperty("name") String name, @JsonProperty("url") String url) {
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

    @Override
    public String toString() {
        return String.format("Provider [name=%s, url=%s]", name, url);
    }
}
