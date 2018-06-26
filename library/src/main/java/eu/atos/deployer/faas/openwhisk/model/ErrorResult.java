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
package eu.atos.deployer.faas.openwhisk.model;

import java.io.Serializable;

public class ErrorResult implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 2739069098400374155L;
    /**
     * 
     */
    private String error;
    private String code;

    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }


}