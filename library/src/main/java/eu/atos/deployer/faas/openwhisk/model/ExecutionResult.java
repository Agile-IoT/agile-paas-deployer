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
import java.util.LinkedHashMap;

import eu.atos.deployer.faas.openwhisk.Constants;

public class ExecutionResult implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 3656604905586463530L;
    private LinkedHashMap<String, String> result;
    private String success;
    private String status;

    public LinkedHashMap<String, String> getResult() {
        return result;
    }
    public void setResult(LinkedHashMap<String, String> result) {
        this.result = result;
    }
    public String getSuccess() {
        return success;
    }
    public void setSuccess(String success) {
        this.success = success;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getResultPayload(){
        if (result != null)
            return result.get(Constants.Fields.PAYLOAD);
        else return null;
    }


}