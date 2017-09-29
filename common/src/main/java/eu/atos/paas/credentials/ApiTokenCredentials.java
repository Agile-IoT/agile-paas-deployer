package eu.atos.paas.credentials;

import java.util.Map;

public class ApiTokenCredentials extends AbstractCredentials implements Credentials {

    public static final String API = "api";
    public static final String TOKEN = "token";
    
    private String api;
    private String token;
    
    public ApiTokenCredentials(String api, String token) throws IllegalArgumentException {
        checkNull(api, API);
        checkNull(token, TOKEN);
        this.api = api;
        this.token = token;
    }
    
    public ApiTokenCredentials(Map<String, String> map) throws IllegalArgumentException {
        this(map.get(API), map.get(TOKEN));
    }
    
    public String getApi() {
        return api;
    }
    
    public String getToken() {
        return token;
    }
}
