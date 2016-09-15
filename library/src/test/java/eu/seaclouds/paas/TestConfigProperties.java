package eu.seaclouds.paas;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 *
 * @author ATOS
 * @date 23/2/2016-15:53:33
 */
public class TestConfigProperties
{

    
    // log
    private static Logger logger = LoggerFactory.getLogger(TestConfigProperties.class);
    
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
    private String op_user = "";
    private String op_password = "";
    
    
    /**
     * 
     * Constructor
     */
    private TestConfigProperties() 
    { 
        try
        {
            PropertiesConfiguration props = new PropertiesConfiguration("tests.config.properties");
            if ((props == null) || (props.isEmpty())) {
                logger.error("PropertiesConfiguration file not found: tests.config.properties");
            }
            
            app_name = props.getString("app_name", "");
            
            heroku_apiKey = props.getString("heroku_apikey", "");
            heroku_user = props.getString("heroku_user", "");
            heroku_password = props.getString("heroku_password", "");
            
            cf_user = props.getString("cf_user", "");
            cf_password = props.getString("cf_password", "");
            cf_org = props.getString("cf_org", "");
            cf_space = props.getString("cf_space", "");
            cf_api = props.getString("cf_api", "");
            
            op_user = props.getString("op_user", "");
            op_password = props.getString("op_password", "");
        }
        catch (ConfigurationException e)
        {
            e.printStackTrace();
        }
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
