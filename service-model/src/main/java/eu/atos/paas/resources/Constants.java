package eu.atos.paas.resources;

public class Constants {

    public static final class Headers {
        public static final String CREDENTIALS = "X-PaaS-Credentials";
        public static final String PROVIDER_VERSION = "X-PaaS-ProviderVersion";
    }
    
    /**
     * Names of providers
     */
    public static final class Providers {
        public static final String DUMMY = "dummy";
        public static final String CLOUDFOUNDRY = "CloudFoundry";
        public static final String HEROKU = "Heroku";
        public static final String OPENSHIFT = "OpenShift";
        public static final String PIVOTAL = "Pivotal";
        public static final String BLUEMIX = "Bluemix";
        public static final String OPENSHIFT_ONLINE = "OpenShift Online";
    }
    
    public static final class MultiPartFields {
        public static final String FILE = "file";
        public static final String MODEL = "model";
    }
}
