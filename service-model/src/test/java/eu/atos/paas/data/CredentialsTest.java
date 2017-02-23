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

import static org.testng.AssertJUnit.*;
import org.testng.annotations.Test;

import eu.atos.paas.data.CredentialsMap.Base64Transformer;
import eu.atos.paas.data.CredentialsMap.PlainTransformer;

public class CredentialsTest {

    String json = "{\"user\":\"a-user\",\"pwd\":\"a-pwd\",\"space\":\"a-space\"}";
    String json64 = "eyJ1c2VyIjoiYS11c2VyIiwicHdkIjoiYS1wd2QiLCJzcGFjZSI6ImEtc3BhY2UifQ==";
    PlainTransformer plain = new PlainTransformer();
    Base64Transformer base64 = new Base64Transformer(plain);
    
    @Test
    public void testPlainSerialize() throws Exception {
        CredentialsMap creds = CredentialsMap.builder()
                .item("user", "a-user")
                .item("pwd", "a-pwd")
                .item("space", "a-space")
                .build();
        assertEquals(json, plain.serialize(creds));
    }
    
    @Test
    public void testPlainDeserialize() throws Exception {
        
        CredentialsMap creds = plain.deserialize(json);
        assertEquals("a-user", creds.get("user"));
        assertEquals("a-pwd", creds.get("pwd"));
        assertEquals("a-space", creds.get("space"));
    }
    
    @Test
    public void testBase64Serialize() throws Exception {
        
        CredentialsMap creds = CredentialsMap.builder()
                .item("user", "a-user")
                .item("pwd", "a-pwd")
                .item("space", "a-space")
                .build();
        assertEquals(json64, base64.serialize(creds));
    }

    @Test
    public void testBase64Deserialize() throws Exception {
        
        CredentialsMap creds = base64.deserialize(json64);
        assertEquals("a-user", creds.get("user"));
        assertEquals("a-pwd", creds.get("pwd"));
        assertEquals("a-space", creds.get("space"));
    }

}
