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
