/**
 * Copyright 2016 Atos
 * Contact: Atos <roman.sosa@atos.net>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
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
