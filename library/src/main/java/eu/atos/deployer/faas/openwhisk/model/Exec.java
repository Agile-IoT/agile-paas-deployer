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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties 
public class Exec implements Serializable{
    private static final long serialVersionUID = 1L;
    String kind;
    String code;
    Boolean binary;
    String main;

    public String getKind() {
        return kind;
    }
    public void setKind(String kind) {
        this.kind = kind;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getBinary() {
        return binary;
    }
    public void setBinary(Boolean binary) {
        this.binary = binary;
    }
    public String getMain() {
        return main;
    }
    public void setMain(String main) {
        this.main = main;
    }

}    

