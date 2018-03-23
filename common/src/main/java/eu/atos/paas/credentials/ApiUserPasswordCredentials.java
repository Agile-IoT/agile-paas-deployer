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
package eu.atos.paas.credentials;

import java.util.Map;

public class ApiUserPasswordCredentials extends AbstractCredentials implements Credentials {

    public static final String API = "api";
    public static final String USER = "user";
    public static final String PASSWORD = "password";

    private String api;
    private String user;
    private String password;
    
    public ApiUserPasswordCredentials(String api, String user, String password) throws IllegalArgumentException {
        checkNull(api, API);
        checkNull(user, USER);
        checkNull(password, PASSWORD);
        this.api = api;
        this.user = user;
        this.password = password;
    }
    
    public ApiUserPasswordCredentials(Map<String, String> map) throws IllegalArgumentException {
        this(map.get(API), map.get(USER), map.get(PASSWORD));
    }

    public String getApi() {
        return api;
    }
    
    public String getUser() {
        return user;
    }
    
    public String getPassword() {
        return password;
    }
}
