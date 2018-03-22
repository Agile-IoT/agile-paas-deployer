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
package eu.atos.paas;

import java.net.MalformedURLException;

import java.net.URL;

public class TestConstants {
    public static final String SERVER_URL_STR = "http://localhost:8002/api";
    public static final URL SERVER_URL;
    
    static {
        try {
            
            SERVER_URL = new URL(SERVER_URL_STR);
            
        } catch (MalformedURLException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
