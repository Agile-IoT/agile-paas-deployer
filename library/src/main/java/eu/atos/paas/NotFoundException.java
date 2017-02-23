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
 * This exceptions indicates an operation on a resource that do not exist. 
 * 
 * For example, you are trying to delete an non-existent application.
 */
public class NotFoundException extends PaasException {
    private static final long serialVersionUID = 1L;

    private String resourceName;
    
    public NotFoundException(String resourceName) {
        super(resourceName + " not found");
        this.resourceName = Objects.requireNonNull(resourceName);
    }

    public NotFoundException(String resourceName, Throwable cause) {
        super(resourceName + " not found", cause);
        this.resourceName = Objects.requireNonNull(resourceName);
    }

    public String getResourceName() {
        return resourceName;
    }
}
