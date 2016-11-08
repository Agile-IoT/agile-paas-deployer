package eu.atos.paas.openshift2;

import java.net.URL;

import eu.atos.paas.PaasSession;


/**
 * 
 *
 * @author ATOS
 * @date 21/3/2016-13:49:55
 */
public class DeployParameters implements PaasSession.DeployParameters {

    
    private String path;
    private String cartridge;
    private URL gitUrl;

    
    public DeployParameters(URL gitUrl, String cartridge) {
        this.cartridge = cartridge;
        this.gitUrl = gitUrl;
    }
    
    
    @Override
    public String getPath() {
        return path;
    }


    @Override
    public String getCartridge()
    {
        return cartridge;
    }


    @Override
    public String getBuildpackUrl()
    {
        return null;
    }
    
    @Override
    public URL getGitUrl() {
        return gitUrl;
    }
}
