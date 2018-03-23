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
package eu.atos.paas.credentials;

import java.util.Map;

/**
 * ApiKey Credentials
 *
 */
public class ApiKeyCredentials extends AbstractCredentials implements Credentials {
    public static final String API_KEY = "api-key";
    private String apiKey;

    public ApiKeyCredentials(String apiKey) throws IllegalArgumentException {
        checkNull(apiKey, API_KEY);
        this.apiKey = apiKey;
    }
    
    public ApiKeyCredentials(Map<String, String> map) throws IllegalArgumentException {
        this(map.get(API_KEY));
    }

    public String getApiKey() {
        return apiKey;
    }

    @Override
    public String toString() {
        return String.format("ApiKeyCredentials [apiKey=%s]", apiKey);
    }
}