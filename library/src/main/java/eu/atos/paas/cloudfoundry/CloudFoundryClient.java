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
package eu.atos.paas.cloudfoundry;

import org.apache.http.conn.HttpHostConnectException;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.springframework.web.client.ResourceAccessException;

import eu.atos.paas.AuthenticationException;
import eu.atos.paas.PaasClient;
import eu.atos.paas.PaasSession;
import eu.atos.paas.credentials.ApiUserPasswordOrgSpaceCredentials;
import eu.atos.paas.credentials.Credentials;


/**
 * 
 * @author
 *
 */
public class CloudFoundryClient implements PaasClient
{
    public static String VERSION = "v2";

    @Override
    public PaasSession getSession(Credentials credentials)
    {
        PaasSession session = null;
        if (credentials instanceof ApiUserPasswordOrgSpaceCredentials)
        {
            session = getSession((ApiUserPasswordOrgSpaceCredentials) credentials);
        }
        else
        {
            throw new UnsupportedOperationException("Credentials of class " + credentials.getClass().getName() + " not supported (Cloud Foundry)");
        }
        
        return session;
    }


    private PaasSession getSession(ApiUserPasswordOrgSpaceCredentials credentials)
    {
        try {
            CloudFoundryConnector connector = new CloudFoundryConnector(
                    credentials.getApi(), credentials.getUser(), credentials.getPassword(),
                    credentials.getOrg(), credentials.getSpace(), true);
            PaasSession session = new CloudFoundrySession(connector);
    
            return session;
        
        } catch (CloudFoundryException e) {
            switch (e.getStatusCode()) {
            case UNAUTHORIZED:
            case FORBIDDEN:
                throw new AuthenticationException();
            default:
                throw e;
            }
        } catch (ResourceAccessException e) {
            
            if (e.getCause() != null && e.getCause() instanceof HttpHostConnectException) {
                throw new AuthenticationException(e.getCause().getMessage());
            }
            throw e;
        }
    }

    @Override
    public String getVersion() {
        return VERSION;
    }
}
