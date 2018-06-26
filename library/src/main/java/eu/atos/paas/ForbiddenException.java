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
