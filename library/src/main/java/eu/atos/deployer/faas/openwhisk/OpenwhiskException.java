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

import eu.atos.deployer.faas.openwhisk.model.ErrorResult;

public class OpenwhiskException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public OpenwhiskException(ErrorResult error) {
        super(error.getError());
    }

    public OpenwhiskException(String message) {
        super(message);
    }

    public OpenwhiskException(Throwable cause) {
        super(cause);
    }

    public OpenwhiskException(Exception cause) {
        super(cause);
    }
}
