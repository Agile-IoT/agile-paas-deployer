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