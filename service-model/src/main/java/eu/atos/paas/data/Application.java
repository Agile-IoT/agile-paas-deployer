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
package eu.atos.paas.data;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Application {

    @JsonProperty
    private String name;
    
    @JsonProperty
    private URI url;

    public Application() {
        this("", null);
    }
    
    public Application(String name) {
        this(name, null);
    }
    
    public Application(String name, URI url) {
        this.name = name;
        this.url = url;
    }
    
    public String getName() {
        return name;
    }

    public URI getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return String.format("Application [name=%s, url=%s]", name, url);
    }
}
