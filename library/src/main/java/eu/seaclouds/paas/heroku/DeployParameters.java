package eu.seaclouds.paas.heroku;

import eu.seaclouds.paas.PaasSession;


/**
 * 
 *
 * @author ATOS
 * @date 21/3/2016-14:03:24
 */
public class DeployParameters implements PaasSession.DeployParameters {

    
    private String path;
    private String buildpack_url;

    
    public DeployParameters(String path) {
        this.path = path;
        this.buildpack_url = "";
    }
    
    
    public DeployParameters(String path, String buildpack_url) {
        this.path = path;
        this.buildpack_url = buildpack_url;
    }
    
    
    @Override
    public String getPath() {
        return path;
    }
    
    
    @Override
    public String getBuildpackUrl()
    {
        return buildpack_url;
    }


    @Override
    public String getCartridge()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    
}
