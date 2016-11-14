package eu.atos.paas.heroku;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
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
public class Module implements eu.atos.paas.Module {

    
    private App app;
    private List<String> lServices;
    private Map<String, String> mEnv;
    private URL url;
    
    
    /**
     * 
     * @param app
     */
    public Module(App app) {
        this(app, Collections.<Addon>emptyList(), Collections.<String, String>emptyMap());
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
        try {
            this.url = new URL(app.getWebUrl());
        } catch (MalformedURLException e) {
            /*
             * this should not happen
             */
            throw new IllegalArgumentException("Error in URL=" + app.getWebUrl() + " from provider ", e);
            
        }
        
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
        
        this.mEnv = new HashMap<String, String>(3);
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
    public URL getUrl() {
        return url;
    }
    
    
    @Override
    public State getState() {
        // TODO Auto-generated method stub
        return null;
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
    public Map<String, String> getEnv()
    {
        return mEnv;
    }
    
    
}
