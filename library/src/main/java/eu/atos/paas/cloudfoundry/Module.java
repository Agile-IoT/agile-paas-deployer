package eu.atos.paas.cloudfoundry;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import org.cloudfoundry.client.lib.domain.CloudApplication;


/**
 * Cloud Foundry module
 * @author 
 *
 */
public class Module implements eu.atos.paas.Module {

    
    private CloudApplication app;
    private List<String> lServices;
    private Map<String, Object> mEnv;
    private URL url;
    
    
    /**
     * 
     * Constructor
     * @param app
     * @param m
     */
    public Module(CloudApplication app, Map<String, Object> m) {
        this.app = app;
        this.lServices = app.getServices();
        this.mEnv = m;
        try {
            
            this.url = new URL(app.getUris().get(0));
            
        } catch (MalformedURLException e) {
            
            /*
             * this should not happen
             */
            throw new IllegalArgumentException("Error in URL=" + app.getUris().get(0) + " from provider ", e);
        }
    }
    
    
    @Override
    public String getName()
    {
        return app.getName();
    }

    
    @Override
    public URL getUrl()
    {
        return url;
    }
    

    @Override
    public String getAppType()
    {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public int getRunningInstances()
    {
        return app.getRunningInstances();
    }
    
    
    @Override
    public List<String> getServices()
    {
        return lServices;
    }
    
    
    @Override
    public Map<String, Object> getEnv()
    {
        return mEnv;
    }
    

}
