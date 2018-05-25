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
import java.util.List;

public class ActionInformation implements Serializable {
    private static final long serialVersionUID = 1L;
    private String response;
    private String url;
    private Integer status;
    private String reason;
    private String name;
    private Boolean publish;
    private String version;
    private List<LinkedHashMap<String, Object>> annotations;
    private Exec exec;
    //private List<String> parameters;
    private List<LinkedHashMap<String, Object>> parameters;

    private LinkedHashMap<String, String> limits;
    private String namespace;
    private String activationId;
    private String error;
    private String code;




    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getActivationId() {
        return activationId;
    }

    public void setActivationId(String activationId) {
        this.activationId = activationId;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public LinkedHashMap<String, String> getLimits() {
        return limits;
    }

    public void setLimits(LinkedHashMap<String, String> limits) {
        this.limits = limits;
    }

    public List<LinkedHashMap<String, Object>> getParameters() {
        return parameters;
    }

    public void setParameters(List<LinkedHashMap<String, Object>> parameters) {
        if (parameters !=null && parameters.size()>0){
            System.out.println("-3-------------->"+parameters.get(0).getClass().toGenericString());
        }

        this.parameters = parameters;
    }

    public Exec getExec() {
        return exec;
    }

    public void setExec(Exec exec) {
        this.exec = exec;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<LinkedHashMap<String, Object>> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<LinkedHashMap<String, Object>> annotations) {
        this.annotations = annotations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Boolean getPublish() {
        return publish;
    }

    public void setPublish(Boolean publish) {
        this.publish = publish;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }


}