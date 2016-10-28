package eu.atos.paas;

import java.net.URL;
import java.util.List;
import java.util.Map;


public interface Module {
    
    
    String getName();
    
    
    URL getUrl();
    
    
    String getAppType();
    
    
    int getRunningInstances();
    
    
    List<String> getServices();
    
    
    /**
     * Get application environment values
     * @return
     */
    Map<String, Object> getEnv();
    
    
}