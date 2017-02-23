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
package eu.atos.paas.credentials;

import java.util.Map;

/**
 * API url / User / Password / Organization / Space Credentials
 *
 */
public class ApiUserPasswordOrgSpaceCredentials extends AbstractCredentials implements Credentials {
    public static final String TRUST_SELF_SIGNED_CERTS = "trustSelfSignedCerts";
    public static final String SPACE = "space";
    public static final String ORG = "org";
    public static final String PASSWORD = "password";
    public static final String USER = "user";
    public static final String API = "api";
    
    private String api;
    private String user;
    private String password;
    private String org;
    private String space;
    private boolean trustSelfSignedCerts;

    public ApiUserPasswordOrgSpaceCredentials(String api, String user, String password, String org, String space) 
            throws IllegalArgumentException {
        this(api, user, password, org, space, true);
    }
    
    public ApiUserPasswordOrgSpaceCredentials(
            String api, String user, String password, String org, String space, boolean trustSelfSignedCerts) 
                    throws IllegalArgumentException {
        checkNull(api, API);
        checkNull(user, USER);
        checkNull(password, PASSWORD);
        checkNull(org, ORG);
        checkNull(space, SPACE);
        
        this.api = api;
        this.user = user;
        this.password = password;
        this.org = org;
        this.space = space;
        this.trustSelfSignedCerts = trustSelfSignedCerts;
    }
    
    public ApiUserPasswordOrgSpaceCredentials(Map<String, String> map) throws IllegalArgumentException {
        this(map.get(API), map.get(USER), map.get(PASSWORD), map.get(ORG), map.get(SPACE), 
                "false".equalsIgnoreCase(map.get(TRUST_SELF_SIGNED_CERTS))? false : true);
    }
    
    public String getUser() {
        return user;
    }
    
    public String getPassword() {
        return password;
    }

    public String getApi()
    {
        return api;
    }

    public String getOrg()
    {
        return org;
    }

    public String getSpace()
    {
        return space;
    }
    
    public boolean isTrustSelfSignedCerts()
    {
        return trustSelfSignedCerts;
    }

    @Override
    public String toString() {
        return String.format("ApiUserPasswordOrgSpaceCredentials [api=%s, user=%s, password=%s, org=%s, space=%s]", api, user, password, org, space);
    }
}