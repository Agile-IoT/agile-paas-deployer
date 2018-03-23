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

import java.io.IOException;
import java.util.Properties;

/**
 * 
 *
 * @author ATOS
 * @date 23/2/2016-15:53:33
 */
public class TestConfigProperties
{

    
    private static final String CONFIG_FILE = "/tests.config.properties";

    // TestConfigProperties instance
    private static TestConfigProperties _instance;
        
    // properties:
    // APP NAME
    private String app_name = "";
    // HEROKU
    private String heroku_apiKey = "";
    private String heroku_user = "";
    private String heroku_password = "";
    // CLOUD FOUNDRY
    private String cf_user = "";
    private String cf_password = "";
    private String cf_api = "";
    private String cf_org = "";
    private String cf_space = "";
    private boolean cf_trustSelfSignedCerts = true;
    // OPENSHIFT
    private String op_api = "";
    private String op_user = "";
    private String op_password = "";
    
    
    /**
     * 
     * Constructor
     */
    private TestConfigProperties() 
    { 
        Properties props = new Properties();
        try {
            props.load(TestConfigProperties.class.getResourceAsStream(CONFIG_FILE));
        } catch (IOException e) {
            throw new IllegalStateException("Properties not valid: " + CONFIG_FILE);
        }
        if ((props == null) || (props.isEmpty())) {
            throw new IllegalStateException("Properties not valid: " + CONFIG_FILE);
        }
        
        app_name = props.getProperty("app_name", "");
        
        heroku_apiKey = props.getProperty("heroku_apikey", "");
        heroku_user = props.getProperty("heroku_user", "");
        heroku_password = props.getProperty("heroku_password", "");
        
        cf_user = props.getProperty("cf_user", "");
        cf_password = props.getProperty("cf_password", "");
        cf_org = props.getProperty("cf_org", "");
        cf_space = props.getProperty("cf_space", "");
        cf_api = props.getProperty("cf_api", "");
        
        op_api = props.getProperty("op_api", "");
        op_user = props.getProperty("op_user", "");
        op_password = props.getProperty("op_password", "");
        
        cf_trustSelfSignedCerts = true;
    }
    
    
    /**
     * 
     * @return
     */
    public static TestConfigProperties getInstance()
    {
        if (_instance == null)
            _instance = new TestConfigProperties();
        
        return _instance;
    }
    
    
    /**
     * @return the app_name
     */
    public String getApp_name()
    {
        return app_name;
    }


    /**
     * @return the heroku_apiKey
     */
    public String getHeroku_apiKey()
    {
        return heroku_apiKey;
    }

    
    /**
     * @return the heroku_user
     */
    public String getHeroku_user()
    {
        return heroku_user;
    }

    
    /**
     * @return the heroku_password
     */
    public String getHeroku_password()
    {
        return heroku_password;
    }

    
    /**
     * @return the cf_user
     */
    public String getCf_user()
    {
        return cf_user;
    }

    
    /**
     * @return the cf_password
     */
    public String getCf_password()
    {
        return cf_password;
    }

    
    /**
     * @return the cf_api
     */
    public String getCf_api()
    {
        return cf_api;
    }

    
    /**
     * @return the cf_org
     */
    public String getCf_org()
    {
        return cf_org;
    }

    
    /**
     * @return the cf_space
     */
    public String getCf_space()
    {
        return cf_space;
    }


    /**
     * @return the cf_trustSelfSignedCerts
     */
    public boolean isCf_trustSelfSignedCerts()
    {
        return cf_trustSelfSignedCerts;
    }
    
    public String getOp_api()
    {
        return op_api;
    }
    /**
     * @return the op_user
     */
    public String getOp_user()
    {
        return op_user;
    }
    
    
    /**
     * @return the op_password
     */
    public String getOp_password()
    {
        return op_password;
    }
    
    
}
