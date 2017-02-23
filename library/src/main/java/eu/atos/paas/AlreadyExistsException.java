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
package eu.atos.paas;

import java.util.Objects;

/**
 * This exceptions indicates a conflict between a creation operation and an existing resource. 
 * 
 * For example, the name of the application you are trying to create already exists.
 */
public class AlreadyExistsException extends PaasException {
    private static final long serialVersionUID = 1L;

    private String resourceName;
    
    public AlreadyExistsException(String resourceName) {
        super(resourceName + " already exists");
        this.resourceName = Objects.requireNonNull(resourceName);
    }

    public AlreadyExistsException(String resourceName, Throwable cause) {
        super(resourceName + " already exists", cause);
        this.resourceName = Objects.requireNonNull(resourceName);
    }

    public String getResourceName() {
        return resourceName;
    }
}
