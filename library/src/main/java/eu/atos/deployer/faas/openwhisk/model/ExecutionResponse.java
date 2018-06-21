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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import eu.atos.deployer.faas.openwhisk.Constants;

public class ExecutionResponse implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 3656604905586463530L;
    private long statusCode;
    private long duration;
    private String name;
    private String subject;
    private String activationId;
    private String publish;
    private List<LinkedHashMap<String, Object>> annotations;
    private String version;
    private LinkedHashMap<String, Object> response;
    private long end;
    private List<String> logs; //al loro, a revisar
    private long start;
    private String namespace;

    public long getDuration() {
        return duration;
    }
    public void setDuration(long duration) {
        this.duration = duration;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getActivationId() {
        return activationId;
    }
    public void setActivationId(String activationId) {
        this.activationId = activationId;
    }
    public String getPublish() {
        return publish;
    }
    public void setPublish(String publish) {
        this.publish = publish;
    }
    public List<LinkedHashMap<String, Object>> getAnnotations() {
        return annotations;
    }
    public void setAnnotations(List<LinkedHashMap<String, Object>> annotations) {
        this.annotations = annotations;
    }

    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public LinkedHashMap<String, Object> getResponse() {
        return response;
    }

    public void setResult(String result){
        response = new LinkedHashMap<String, Object>();
        response.put(Constants.Fields.RESULT, result);
    }

    public Object getResult(){
        if (response==null) return null;
        return response.get(Constants.Fields.RESULT);

    }

    private void writeLinkedHashMap(LinkedHashMap<String, Object> lhm){
        Iterator <String> it = lhm.keySet().iterator();
        while (it.hasNext()){
            String key = it.next();
            Object value = lhm.get(key);
            System.out.println("-----------------k "+key);
            System.out.println("-----------------v "+value.getClass()+" "+value);
        }
    }

    public void setResponse(LinkedHashMap<String, Object> response) {
        this.response = response;
    }
    public long getEnd() {
        return end;
    }
    public void setEnd(long end) {
        this.end = end;
    }
    public List<String> getLogs() {
        return logs;
    }
    public void setLogs(List<String> logs) {
        this.logs = logs;
    }
    public long getStart() {
        return start;
    }
    public void setStart(long start) {
        this.start = start;
    }
    public String getNamespace() {
        return namespace;
    }
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
    public long getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(long statusCode) {
        this.statusCode = statusCode;
    }


}