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
package eu.atos.paas;

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
