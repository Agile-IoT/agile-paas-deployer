package eu.seaclouds.paas;

import java.util.List;
import java.util.Map;


public interface Module {
    
    
    String getName();
    
    
    String getUrl();
    
    
    String getAppType();
    
    
    int getRunningInstances();
    
    
    List<String> getServices();
    
    
    /**
     * Get application environment values
     * @return
     */
    Map<String, Object> getmEnv();
    
    
}