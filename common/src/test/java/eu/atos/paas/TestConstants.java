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
