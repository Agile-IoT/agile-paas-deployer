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
package eu.atos.faas;

import java.util.Map;

public interface FaasSession {

    /**
     * Execute a method
     * @param moduleName  specify the module to be executed.  
     * @param paramparamseters  invocation parameters
     * @return result
     */
    public Object execute(String moduleName, Map<String, String> params, Map<String, Object> extraParameters);

    public interface ExecutionParameters {
        public static class Properties {
            public static final String SYNCHRONOUS = "synchronous";
            public static final String RESULT = "result";
            public static final String TIMEOUT = "timeout";
        }
    }

}
