/**
 * Copyright 2016 Atos
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
package eu.atos.paas.resources;

import javax.ws.rs.core.Response.Status;

/**
 * JSON entity to be sent on failed requests. 
 * The schema matches the default entity sent by Jersey on exceptions, so the same deserialization could be used in 
 * client.
 */
public class ErrorEntity {
    
    private int code;
    private String message;
    
    public ErrorEntity() {
        this.code = Status.INTERNAL_SERVER_ERROR.getStatusCode();
        this.message = "Internal server error";
    }
    public ErrorEntity(Status status, String message) {
        this.code = status.getStatusCode();
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}