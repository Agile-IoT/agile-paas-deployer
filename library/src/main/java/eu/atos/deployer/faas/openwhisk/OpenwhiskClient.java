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
