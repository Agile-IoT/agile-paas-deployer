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
package eu.atos.paas.openshift3;

import java.net.ConnectException;
import java.util.List;

import com.openshift.restclient.ClientBuilder;
import com.openshift.restclient.IClient;
import com.openshift.restclient.OpenShiftException;
import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.authorization.UnauthorizedException;
import com.openshift.restclient.model.IProject;

import eu.atos.paas.AuthenticationException;
import eu.atos.paas.PaasClient;
import eu.atos.paas.PaasException;
import eu.atos.paas.PaasSession;
import eu.atos.paas.credentials.ApiTokenCredentials;
import eu.atos.paas.credentials.ApiUserPasswordCredentials;
import eu.atos.paas.credentials.Credentials;


/**
 * Library client to get access to OpenShift v3 platforms.
 * 
 * Two credentials types are accepted:
 * <li>Api, user, password
 * <li>Api, token: The token can be a session token or a service account token. See
 * https://docs.openshift.com/container-platform/3.5/rest_api/index.html#rest-api-authentication
 * 
 */
public class OpenShift3Client implements PaasClient
{
    public static final String VERSION = "v3";

    @Override
    public PaasSession getSession(Credentials credentials) throws PaasException
    {
        if (credentials instanceof ApiUserPasswordCredentials) {
            ApiUserPasswordCredentials creds = (ApiUserPasswordCredentials) credentials;
            IClient osClient = 
                    new ClientBuilder(creds.getApi())
                    .withUserName(creds.getUser())
                    .withPassword(creds.getPassword())
                    .build();
            
            return getSession(osClient);
        }
        else if (credentials instanceof ApiTokenCredentials) {
            
            ApiTokenCredentials creds = (ApiTokenCredentials) credentials;
            IClient osClient = 
                    new ClientBuilder(creds.getApi())
                    .build();
            osClient.getAuthorizationContext().setToken(creds.getToken());
            return getSession(osClient);
        }
        throw new UnsupportedOperationException(String.format(
                "Openshift3 client not implemented for %s type", credentials.getClass()));
    }

    private PaasSession getSession(IClient osClient) {
        
        try {
            
            @SuppressWarnings("unused")
            List<IProject> projects = osClient.list(ResourceKind.PROJECT);
        }
        catch (UnauthorizedException e){
            throw new AuthenticationException();
        }
        catch (OpenShiftException e) {
            if (e.getCause() != null && e.getCause() instanceof ConnectException) {
                throw new AuthenticationException(e.getMessage(), e);
            }
            throw e;
        }
        return new OpenShift3Session(osClient);
    }
    
    @Override
    public String getVersion() {
        return VERSION;
    }
}
