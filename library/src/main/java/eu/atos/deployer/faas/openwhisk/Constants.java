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
