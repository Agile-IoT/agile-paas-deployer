package eu.atos.paas.data;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "name", "url" })
public class Provider {

    private String name;
    private URL url;
    private Set<String> versions;
    private String defaultVersion;
    
    @JsonCreator
    public Provider(
            @JsonProperty("name") String name, 
            @JsonProperty("url") String url, 
            @JsonProperty("versions") Set<String> versions,
            @JsonProperty("defaultVersion") String defaultVersion) 
    {
        this(name, parseUrl(url), versions, defaultVersion);
    }

    public Provider(String name, String url, String[] versions, String defaultVersion) {
        this(name, parseUrl(url), new HashSet<String>(Arrays.asList(versions)), defaultVersion);
    }
    
    public Provider(String name, String url, String defaultVersion) {
        this(name, parseUrl(url), Collections.singleton(defaultVersion), defaultVersion);
    }
    
    public Provider(String name, URL url, Set<String> versions, String defaultVersion) {
        this.name = name;
        this.url = url;
        this.versions = new HashSet<>(versions);
        this.defaultVersion = defaultVersion;
    }

    public String getName() {
        return name;
    }

    public URL getUrl() {
        return url;
    }
    
    public Set<String> getVersions() {
        return versions;
    }

    public String getDefaultVersion() {
        return defaultVersion;
    }
    
    @Override
    public String toString() {
        return String.format("Provider [name=%s, url=%s, versions=%s]", 
                name, url, Arrays.toString(versions.toArray()));
    }

    private static URL parseUrl(String url) {
        URL url_;
        try {
            url_ = new URL(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
        return url_;
    }
}
