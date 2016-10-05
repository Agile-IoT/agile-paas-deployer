package eu.atos.paas;


public interface Credentials {

    
    /**
     * User / Password Credentials
     *
     */
    public class UserPasswordCredentials implements Credentials {
        private String user;
        private String password;
    
        public UserPasswordCredentials(String user, String password) {
            this.user = user;
            this.password = password;
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
    
    
    /**
     * API url / User / Password / Organization / Space Credentials
     *
     */
    public class ApiUserPasswordOrgSpaceCredentials implements Credentials {
        private String api;
        private String user;
        private String password;
        private String org;
        private String space;
        private boolean trustSelfSignedCerts;
    
        public ApiUserPasswordOrgSpaceCredentials(String api, String user, String password, String org, String space) {
            this.api = api;
            this.user = user;
            this.password = password;
            this.org = org;
            this.space = space;
            this.trustSelfSignedCerts = true;
        }
        
        public ApiUserPasswordOrgSpaceCredentials(String api, String user, String password, String org, String space, boolean trustSelfSignedCerts) {
            this.api = api;
            this.user = user;
            this.password = password;
            this.org = org;
            this.space = space;
            this.trustSelfSignedCerts = trustSelfSignedCerts;
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

    
    /**
     * ApiKey Credentials
     *
     */
    public class ApiKeyCredentials implements Credentials {
        String apiKey;

        public ApiKeyCredentials(String apiKey) {
            super();
            this.apiKey = apiKey;
        }

        public String getApiKey() {
            return apiKey;
        }

        @Override
        public String toString() {
            return String.format("ApiKeyCredentials [apiKey=%s]", apiKey);
        }
    }
    
}