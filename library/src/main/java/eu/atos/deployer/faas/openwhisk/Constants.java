/**
 * Copyright 2018 Atos
 * Contact: Atos <elena.garrido@atos.net>
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
package eu.atos.deployer.faas.openwhisk;

public class Constants {

    public static final class Headers {
        public static final String CREDENTIALS = "X-PaaS-Credentials";
        public static final String PROVIDER_VERSION = "X-PaaS-ProviderVersion";
    }


    public static final class Path {
        public static final String ROOT = "/api/v1";
        public static final String NAMESPACES = "/namespaces";
        public static final String ACTIONS = "/actions";
        public static final String ACTIVATIONS = "/activations";
        public static final String LOGS = "/logs";
        public static final String RESULT = "/result";
        public static final String PACKAGES = "/packages";
    }

    public static final class Query {
        public static final String OVERWRITE = "overwrite";
        public static final String BLOCKING = "blocking";
        public static final String RESULT = "result";
        public static final String LIMIT = "limit";
        public static final String SKIP = "skip";
        public static final String TIMEOUT = "timeout";
        public static final String CODE = "code";
        public static final String NAME = "name";
        public static final String SINCE = "since";
        public static final String UPTO = "upto";
        public static final String DOCS = "docs";
        public static final String PUBLIC = "public";
    }

    public static final class Fields {
        public static final String RESULT = "result";
        public static final String PAYLOAD = "payload";
    }

    public static final class Kind {
        public static final String DEFAULT = "nodejs:default";
        public static final String PHP7 = "php:7.1";
        public static final String NODEJS8 = "nodejs:8";
        public static final String SWIFT3 = "swift:3";
        public static final String NODEJS = "nodejs";
        public static final String BLACKBOX = "blackbox";
        public static final String JAVA = "java";
        public static final String SEQUENCE = "sequence";
        public static final String NODEJS6 = "nodejs:6";
        public static final String PYTHON3 = "python:3";
        public static final String PYTHON = "python";
        public static final String PYTHON2 = "python:2";
        public static final String SWIFT = "swift";
        public static final String SWIFT311 = "swift:3.1.1";
    }



    public static final class MultiPartFields {
        public static final String FILE = "file";
        public static final String MODEL = "model";
    }
}
