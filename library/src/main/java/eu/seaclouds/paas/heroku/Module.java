package eu.seaclouds.paas.heroku;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.heroku.api.Addon;
import com.heroku.api.App;


/**
 * Heroku module
 * @author
 *
 */
public class Module implements eu.seaclouds.paas.Module {

    
    private App app;
    private List<String> lServices;
    private Map<String, Object> mEnv;
    
    
    /**
     * 
     * @param app
     */
    public Module(App app) {
        this.app = app;
        lServices = new ArrayList<String>(0);
    }
    
    
    /**
     * 
     * Constructor
     * @param app
     * @param l
     * @param m
     */
    public Module(App app, List<Addon> l, Map<String, String> m) {
        this.app = app;
        
        if ((l != null) && (l.size() > 0))
        {
            lServices = new ArrayList<String>(3);
            for (Addon ad : l)
            {
                lServices.add(ad.getName());
            }
        }
        else
        {
            lServices = new ArrayList<String>(0);
        }
        
        this.mEnv = new HashMap<String, Object>(3);
        if ((m != null) && (m.size() > 0))
        {
            // Map<String, String> to Map<String, Object>
            //this.mEnv.putAll(m);
            for (Map.Entry<String, String> entry : m.entrySet()) {
                this.mEnv.put(entry.getKey(), entry.getValue());
            }
        }
    }

    
    @Override
    public String getName() {
        return app.getName();
    }
    
    
    @Override
    public String getUrl() {
        return app.getWebUrl();
    }
    
    
    @Override
    public String getAppType() {
        return "web";
    }


    @Override
    public int getRunningInstances()
    {
        return app.getDynos();
    }

    
    @Override
    public List<String> getServices()
    {
        return lServices;
    }


    @Override
    public Map<String, Object> getmEnv()
    {
        return mEnv;
    }
    
    
}
