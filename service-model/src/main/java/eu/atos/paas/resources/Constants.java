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
