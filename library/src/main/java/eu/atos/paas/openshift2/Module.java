package eu.atos.paas.openshift2;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import com.openshift.client.IApplication;
import com.openshift.client.IGear;
import com.openshift.client.IGearGroup;
import com.openshift.client.cartridge.ICartridge;


/**
 * 
 *
 * @author ATOS
 * @date 21/3/2016-14:05:37
 */
public class Module implements eu.atos.paas.Module
{

    
    private IApplication app;
    private URL url;
    
    /**
     * 
     * Constructor
     * @param app
     */
    public Module(IApplication appl)
    {
        this.app = appl;
        try {
            this.url = new URL(app.getApplicationUrl());
        } catch (MalformedURLException e) {
            /*
             * this should not happen
             */
            throw new IllegalArgumentException("Error in URL=" + app.getApplicationUrl() + " from provider ", e);
        }
    }
    
    
    @Override
    public String getName()
    {
        return this.app.getName();
    }

    
    @Override
    public URL getUrl()
    {
        return url;
    }

    
    @Override
    public State getState() {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String getAppType()
    {
        return app.getCartridge().getName();
    }

    
    @Override
    public int getRunningInstances()
    {
        int runningInst = 0;
        
        Collection<IGearGroup> res = app.getGearGroups();
        for (IGearGroup g : res)
        {
            for (ICartridge cres2 : g.getCartridges())
            {
                if (cres2.getName().equalsIgnoreCase(app.getCartridge().getName()))
                {
                    for (IGear ig : g.getGears())
                    {
                        if (ig.getState().getState().equalsIgnoreCase("STARTED"))
                        {
                            runningInst++;
                        }
                    }
                    break;
                }
            }
        }
        return runningInst;
    }

    
    @Override
    public List<String> getServices()
    {
        boolean isApp = false;
        String serv = "";
        ArrayList<String> lServices = new ArrayList<String>(3);
        
        Collection<IGearGroup> res = app.getGearGroups();
        for (IGearGroup g : res)
        {
            for (ICartridge cres2 : g.getCartridges())
            {
                
                if (cres2.getName().equalsIgnoreCase(app.getCartridge().getName()))
                {
                    for (IGear ig : g.getGears())
                    {
                        if (ig.getState().getState().equalsIgnoreCase("STARTED"))
                        {
                            isApp = true;
                        }
                    }
                    break;
                }
                else 
                {
                    serv = cres2.getName();
                }
            }
            
            if (!isApp)
            {
                lServices.add(serv);
            }
            isApp = false;
        }
        
        return lServices;
    }

    
    @Override
    public Map<String, String> getEnv()
    {
        // TODO Auto-generated method stub
        return null;
    }
    

}
