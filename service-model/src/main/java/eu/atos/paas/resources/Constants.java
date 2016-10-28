package eu.atos.paas.resources;

public class Constants {

    public static final class Headers {
        public static final String CREDENTIALS = "X-PaaS-Credentials";
    }
    
    public static final class Providers {
        public static final String DUMMY = "dummy";
        public static final String CLOUDFOUNDRY = "CloudFoundry";
        public static final String HEROKU = "Heroku";
        public static final String OPENSHIFT2 = "OpenShift2";
        public static final String OPENSHIFT3 = "OpenShift3";
        public static final String PIVOTAL = "Pivotal";
        public static final String BLUEMIX = "Bluemix";
    }
    
    public static final class MultiPartFields {
        public static final String FILE = "file";
        public static final String MODEL = "model";
    }
}
