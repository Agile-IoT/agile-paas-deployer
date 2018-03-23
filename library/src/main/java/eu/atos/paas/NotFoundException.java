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
