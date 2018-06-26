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
