/**
 * Copyright 2018 Atos
 * Contact: Atos <elena.garrido@atos.net>
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
package eu.atos.deployer.faas.openwhisk;

import java.net.MalformedURLException;
import java.net.URL;

import eu.atos.paas.PaasClient;
import eu.atos.paas.PaasException;
import eu.atos.paas.PaasSession;
import eu.atos.paas.credentials.ApiUserPasswordCredentials;
import eu.atos.paas.credentials.Credentials;


/**
 * 
 */
public class OpenwhiskClient implements PaasClient
{
    public static final String VERSION = "v1";

    @Override
    public PaasSession getSession(Credentials credentials) throws PaasException
    {
        ApiUserPasswordCredentials creds = (ApiUserPasswordCredentials) credentials;
        try {
            return  new OpenwhiskSession(new URL(creds.getApi()), creds.getUser(), creds.getPassword());
        } catch (MalformedURLException e) {
            throw new PaasException("Check the URL specified in the API field, it seems not to be wellformed", e);
        }
    }

    @Override
    public String getVersion() {
        return VERSION;
    }



}
