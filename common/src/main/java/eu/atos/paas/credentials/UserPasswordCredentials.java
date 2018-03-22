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

/**
 * User / Password Credentials
 *
 */
public class UserPasswordCredentials extends AbstractCredentials implements Credentials {
    public static final String USER = "user";
    public static final String PASSWORD = "password";
    
    private String user;
    private String password;

    public UserPasswordCredentials(String user, String password) throws IllegalArgumentException {
        checkNull(user, USER);
        checkNull(password, PASSWORD);
        this.user = user;
        this.password = password;
    }
    
    public UserPasswordCredentials(Map<String, String> map) throws IllegalArgumentException {
        this(map.get(USER), map.get(PASSWORD));
    }
    
    public String getUser() {
        return user;
    }
    
    public String getPassword() {
        return password;
    }
    
    @Override
    public String toString() {
        return String.format("UserPasswordCredentials [user=%s, password=%s]", user, password);
    }
}