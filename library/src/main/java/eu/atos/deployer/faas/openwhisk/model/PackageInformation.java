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