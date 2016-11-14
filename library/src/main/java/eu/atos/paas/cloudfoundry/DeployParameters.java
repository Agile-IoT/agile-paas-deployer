package eu.atos.paas.cloudfoundry;

import java.net.URL;
import java.util.Objects;

import eu.atos.paas.PaasSession;


/**
 * 
 *
 * @author ATOS
 * @date 21/3/2016-14:03:16
 */
public class DeployParameters implements PaasSession.DeployParameters
{
    
    private String path;
    private String buildpack_url;

    
    public DeployParameters(String path) {
        this.path = Objects.requireNonNull(path);
        this.buildpack_url = "";
    }
    
    
    public DeployParameters(String path, String buildpack_url) {
        this.path = Objects.requireNonNull(path);
        this.buildpack_url = Objects.requireNonNull(buildpack_url);
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
        return "";
    }
    
    @Override
    public URL getGitUrl() {
        return null;
    }
}
