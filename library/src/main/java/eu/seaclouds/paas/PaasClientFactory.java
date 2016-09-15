package eu.seaclouds.paas;

import eu.seaclouds.paas.cloudfoundry.CloudFoundryClient;
import eu.seaclouds.paas.heroku.HerokuClient;
import eu.seaclouds.paas.openshift2.Openshift2Client;
import eu.seaclouds.paas.openshift3.Openshift3Client;


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
