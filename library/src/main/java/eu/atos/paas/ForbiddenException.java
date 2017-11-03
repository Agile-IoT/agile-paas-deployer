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

/**
 * Exception to indicate lack of permissions on the provider to access a resource
 * (e.g. in Heroku, applications names are shared between all users; trying to access an application
 * owned by other user will throw a 403).
 */
public class ForbiddenException extends PaasException {
    private static final long serialVersionUID = 1L;

    private final String resourceName;
    
    public ForbiddenException(String resourceName, Throwable e) {
        super("Access forbidden to " + resourceName, e);
        this.resourceName = resourceName;
    }
    
    public String getResourceName() {
        return resourceName;
    }
}
