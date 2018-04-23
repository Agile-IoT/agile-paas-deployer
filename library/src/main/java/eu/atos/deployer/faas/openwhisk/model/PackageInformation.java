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

public class PackageInformation implements Serializable {
    private static final long serialVersionUID = 1L;
    private String namespace;
    private String name;
    private String version;
    private Boolean publish;
    private Boolean binding;
    private List<LinkedHashMap<String, Object>> annotations;
    private Exec exec;
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public Boolean getPublish() {
        return publish;
    }
    public void setPublish(Boolean publish) {
        this.publish = publish;
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

    public String getNamespace() {
        return namespace;
    }
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
    public Exec getExec() {
        return exec;
    }
    public void setExec(Exec exec) {
        this.exec = exec;
    }
    public Boolean getBinding() {
        return binding;
    }
    public void setBinding(Boolean binding) {
        this.binding = binding;
    }

}