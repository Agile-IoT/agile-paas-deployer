/*******************************************************************************
 * Copyright 2017 Atos
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
 *******************************************************************************/
package eu.atos.paas.credentials;

import java.util.Map;

public class ApiTokenCredentials extends AbstractCredentials implements Credentials {

    public static final String API = "api";
    public static final String TOKEN = "token";
    
    private String api;
    private String token;
    
    public ApiTokenCredentials(String api, String token) throws IllegalArgumentException {
        checkNull(api, API);
        checkNull(token, TOKEN);
        this.api = api;
        this.token = token;
    }
    
    public ApiTokenCredentials(Map<String, String> map) throws IllegalArgumentException {
        this(map.get(API), map.get(TOKEN));
    }
    
    public String getApi() {
        return api;
    }
    
    public String getToken() {
        return token;
    }
}
