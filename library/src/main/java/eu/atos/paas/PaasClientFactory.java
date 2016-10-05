package eu.atos.paas;

import eu.atos.paas.cloudfoundry.CloudFoundryClient;
import eu.atos.paas.heroku.HerokuClient;
import eu.atos.paas.openshift2.Openshift2Client;
import eu.atos.paas.openshift3.Openshift3Client;


/**
 * 
 *
 * @author ATOS
 * @date 21/3/2016-13:30:16
 */
public class PaasClientFactory {

    public PaasClient getClient(String provider) {
        switch (provider) {
            case "heroku":
                return new HerokuClient();
            case "cloudfoundry":
            case "bluemix":
            case "pivotal":
                return new CloudFoundryClient();
            case "openshift2":    
                return new Openshift2Client();
            case "openshift3":
            default:
                throw new IllegalArgumentException("Provider " + provider + " not supported");
        }
    }
    
    
}
