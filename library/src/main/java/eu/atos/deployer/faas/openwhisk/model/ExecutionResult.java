/**
 * Copyright 2018 Atos
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