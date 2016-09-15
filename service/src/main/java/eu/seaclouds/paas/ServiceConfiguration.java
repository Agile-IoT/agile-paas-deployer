package eu.seaclouds.paas;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;

public class ServiceConfiguration extends Configuration {
    
    @NotEmpty
    private String value = "";
    
    @JsonProperty
    private String getValue() {
        return value;
    }
    
    @JsonProperty
    private void setValue(String value) {
        this.value = value;
    }

}
