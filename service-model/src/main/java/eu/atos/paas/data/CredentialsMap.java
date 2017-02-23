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

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.glassfish.jersey.internal.util.Base64;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CredentialsMap extends LinkedHashMap<String, String> {
    
    private static final long serialVersionUID = 1L;
    private static final TypeReference<Map<String,String>> typeRef = new TypeReference<Map<String,String>>() {};
    
    public CredentialsMap(Map<String, String> credentials) {
        this.putAll(credentials);
    }

    @Override
    public String toString() {
        return String.format("Credentials [" + super.toString() + "]");
    }

    public static CredentialsBuilder builder() {
        return new CredentialsBuilder();
    }
    
    public static class CredentialsBuilder {
        CredentialsMap credentials;

        public CredentialsBuilder() {
            credentials =  new CredentialsMap(Collections.<String, String>emptyMap());
        }
        
        public CredentialsBuilder item(String key, String value) {
            credentials.put(key, value);
            return this;
        }
        
        public CredentialsMap build() {
            return credentials;
        }
    }

    public interface Transformer {
        
        String serialize(CredentialsMap credentials);
        CredentialsMap deserialize(String serialized);
    }
    
    public static class PlainTransformer implements Transformer {
        private static ObjectMapper mapper = new ObjectMapper();

        @Override
        public String serialize(CredentialsMap credentials) {
            String result;
            try {
                result = mapper.writeValueAsString(credentials);
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException(e.getMessage(), e);
            }
            
            return result;
        }
        
        @Override
        public CredentialsMap deserialize(String serialized) {
            
            Map<String, String> map;
            try {
                map = mapper.readValue(serialized, typeRef);
            } catch (IOException e) {
                throw new IllegalArgumentException(e.getMessage(), e);
            }
            
            return new CredentialsMap(map);
        }
    }
    
    public static class Base64Transformer implements Transformer {

        Transformer innerTransformer;
        
        public Base64Transformer(Transformer innerDecorator) {
            this.innerTransformer = innerDecorator;
        }
        
        @Override
        public String serialize(CredentialsMap credentials) {
            String innerText = innerTransformer.serialize(credentials);
            String serialized = Base64.encodeAsString(innerText);
            return serialized;
        }

        @Override
        public CredentialsMap deserialize(String serialized) {
            String plainText = Base64.decodeAsString(serialized);
            CredentialsMap credentials = innerTransformer.deserialize(plainText);
            return credentials;
        }
        
    }
}
