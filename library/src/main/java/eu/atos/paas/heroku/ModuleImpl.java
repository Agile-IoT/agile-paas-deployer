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

import eu.atos.paas.Module;


/**
 * Heroku module
 * @author
 *
 */
public class ModuleImpl implements Module {

    
    private App app;
    private List<String> lServices;
    private Map<String, String> mEnv;
    private URL url;
    private URL gitUrl;
    private State state;
    
    
    /**
     * 
     * @param app
     */
    public ModuleImpl(App app) {
        this(app, Collections.<Addon>emptyList(), Collections.<String, String>emptyMap());
        this.app = app;
        lServices = new ArrayList<String>(0);
    }
    
    
    public ModuleImpl(App app, List<Addon> serviceList, Map<String, String> env) {
        this.app = app;
        
        this.url = urlFromString(app.getWebUrl());
        this.gitUrl = urlFromString(app.getGitUrl());
        
        lServices = new ArrayList<String>(3);
        for (Addon ad : serviceList)
        {
            lServices.add(ad.getName());
        }
        
        this.mEnv = new HashMap<String, String>(3);
        for (Map.Entry<String, String> entry : env.entrySet()) {
            this.mEnv.put(entry.getKey(), entry.getValue());
        }
        
        if (mEnv.containsKey(HerokuSession.UNDEPLOYED_FLAG)) {
            this.state = State.UNDEPLOYED;
        }
        else {
            this.state = app.getDynos() > 0? State.STARTED : State.STOPPED;
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
        return state;
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

    
    @Override
    public String toString() {
        return String.format("ModuleImpl [app=%s, lServices=%s, mEnv=%s, url=%s, gitUrl=%s, state=%s]", 
                app, lServices, mEnv, url, gitUrl, state);
    }


    /**
     * Returns an URL from a String. It is supposed to be passed "safe" URL strings received from provider.
     */
    private URL urlFromString(String urlStr) {

        try {
            
            URL result = new URL(urlStr);
            return result;
            
        } catch (MalformedURLException e) {
            /*
             * this should not happen
             */
            throw new IllegalArgumentException("Error in URL=" + urlStr + " from provider ", e);
        }
    }
}
