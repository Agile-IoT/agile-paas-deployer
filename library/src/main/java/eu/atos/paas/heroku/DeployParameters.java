package eu.atos.paas.heroku;

import java.net.URL;

import eu.atos.paas.PaasSession;


/**
 * 
 *
 * @author ATOS
 * @date 21/3/2016-14:03:24
 */
public class DeployParameters implements PaasSession.DeployParameters {

    
    private String path;
    private String buildpack_url;
    private URL gitUrl;
    
    public DeployParameters(String path) {
        this.path = path;
        this.buildpack_url = "";
    }
    
    
    public DeployParameters(String path, String buildpack_url) {
        this.path = path;
        this.buildpack_url = buildpack_url;
    }
    
    public DeployParameters(URL gitUrl) {
        this.gitUrl = gitUrl;
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
        return null;
    }


    @Override
    public URL getGitUrl() {
        return gitUrl;
    }
    
    
}
