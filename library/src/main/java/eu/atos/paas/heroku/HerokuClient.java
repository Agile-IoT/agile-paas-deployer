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
package eu.atos.paas.heroku;

import eu.atos.paas.PaasClient;
import eu.atos.paas.PaasSession;
import eu.atos.paas.credentials.ApiKeyCredentials;
import eu.atos.paas.credentials.Credentials;


public class HerokuClient implements PaasClient {
    public static final String VERSION = "v3";
    
    @Override
    public PaasSession getSession(Credentials credentials) {
        PaasSession session = null;

        if (credentials instanceof ApiKeyCredentials) {

            session = getSession((ApiKeyCredentials)credentials);
            
        } else {
            
            throw new UnsupportedOperationException("Please, login with apikey");
        }
        
        return session;
    }

    private PaasSession getSession(ApiKeyCredentials credentials) {
        HerokuConnector connector = new HerokuConnector(credentials.getApiKey());
        
        PaasSession session = new HerokuSession(connector);
        
        return session;
    }
    
    @Override
    public  String getVersion() {
        return VERSION;
    }
}
