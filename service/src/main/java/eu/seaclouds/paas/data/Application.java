package eu.seaclouds.paas.data;

import java.net.URL;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Optional;

public class Application {

    @JsonProperty
    private String name;
    
    @JsonProperty
    private Optional<URL> url;

    public Application() {
        this.name = "";
        this.url = Optional.absent();
    }
    
    public Application(String name, URL url) {
        super();
        this.name = name;
        this.url = Optional.of(url);
    }

    public String getName() {
        return name;
    }

    public Optional<URL> getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return String.format("Application [name=%s, url=%s]", name, url);
    }
}
