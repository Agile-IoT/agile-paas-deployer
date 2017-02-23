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