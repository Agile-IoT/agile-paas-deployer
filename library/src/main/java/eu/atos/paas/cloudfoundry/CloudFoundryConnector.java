/**
 *  Copyright (c) 2017 Atos, and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License 2.0
 *  which accompanies this distribution, and is available at
 *  https://www.eclipse.org/legal/epl-2.0/
 *
 *  Contributors:
 *  Atos - initial implementation
 */
package eu.atos.paas.cloudfoundry;

import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.lib.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.atos.paas.PaasProviderException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class CloudFoundryConnector
{
    
    
    // logger
    private static final Logger logger = LoggerFactory.getLogger(CloudFoundryConnector.class);

    // CF client
    private CloudFoundryClient _cfclient;
    // default values
    private final int DEFAULT_MEMORY = 512; // MB
    

    /**
     * 
     * @param APIEndPoint
     * @param login
     * @param passwd
     * @param trustSelfSignedCerts
     */
    public CloudFoundryConnector(String APIEndPoint, String login, String passwd, boolean trustSelfSignedCerts)
    {
        this(APIEndPoint, login, passwd, "", "", trustSelfSignedCerts);
    }


    /**
     * 
     * @param APIEndPoint
     * @param login
     * @param passwd
     * @param organization
     * @param space
     */
    public CloudFoundryConnector(String APIEndPoint, String login, String passwd, String organization, String space)
    {
        this(APIEndPoint, login, passwd, organization, space, false);
    }


    /**
     * 
     * @param APIEndPoint
     * @param login
     * @param passwd
     * @param organization
     * @param space
     * @param trustSelfSignedCerts
     */
    public CloudFoundryConnector(String APIEndPoint, String login, String passwd, String organization, String space, boolean trustSelfSignedCerts)
    {
        logger.info(">> Connecting to CloudFoundry [" + APIEndPoint + "] ...");
        try
        {
            if ((organization != null && !organization.isEmpty()) && (space != null && !space.isEmpty()))
            {
                _cfclient = new CloudFoundryClient(new CloudCredentials(login, passwd), getTargetURL(APIEndPoint), organization, space, trustSelfSignedCerts);
            }
            else
            {
                _cfclient = new CloudFoundryClient(new CloudCredentials(login, passwd), getTargetURL(APIEndPoint), trustSelfSignedCerts);
            }

            _cfclient.login();
            logger.info(">> Connection established");
        }
        catch (CloudFoundryException ex)
        {
            logger.warn(">> " + ex.getMessage());
            throw ex;
        }
    }


    /**
     * Returns the cloud foundry client object
     * 
     * @return
     */
    public CloudFoundryClient getConnectedClient()
    {
        return _cfclient;
    }


    /**
     * DEPLOY an application in CF 1. Create application 2. Upload application
     * 3. Start application
     * 
     * -push command examples (CLI): 
     *   cf push ehealthgui -p ./apps/WebGUI_v3.war -d 62.14.219.157.xip.io -b https://github.com/rsucasas/java-buildpack.git
     *     -m 512MB -i 1 cf push softcare-ws -p ./apps/softcare-ws.war -b https://github.com/rsucasas/java-buildpack.git -m 512MB -i 1
     *
     * @param applicationName Application name
     * @param domainName
     * @param warFile
     * @param buildpackUrl For example: https://github.com/rsucasas/java-buildpack.git
     * @return
     */
    public boolean deployApp(String applicationName, String domainName, String warFile, String buildpackUrl)
    {
        // 1. Create application
        CloudApplication app = createApplication(applicationName, domainName, buildpackUrl);

        // 2. Upload application
        if ((app != null) && (uploadApplication(app.getName(), warFile)))
        {
            // 3. Start application
            logger.info(">> Starting application ... ");
            _cfclient.startApplication(app.getName());
            return true;
        }

        return false;
    }


    /**
     * 
     * @param applicationName
     * @param warFile
     * @param buildpackUrl
     */
    public boolean deployApp(String applicationName, String warFile, String buildpackUrl)
    {
        return deployApp(applicationName, "", warFile, buildpackUrl);
    }


    /**
     * DEPLOY an application with database (only binding) in CF
     * 
     * 1. Create service 2. Create application 3. bind to service 4. get
     * database parameters and setup application environment variables 5. Upload application 6. Start application
     * 
     * @param applicationName
     * @param domainName
     * @param warFile
     * @param buildpackUrl
     * @param serviceOffered
     * @param serviceName
     * @param servicePlan
     */
    public void deployAppWithDatabase(String applicationName, String domainName, String warFile, String buildpackUrl,
            String serviceOffered, String serviceName, String servicePlan)
    {
        // 1. Create service
        // http://docs.cloudfoundry.org/devguide/services/managing-services.html
        CloudService cs = createService(serviceOffered, serviceName, servicePlan);

        // 2. Create application
        CloudApplication app = createApplication(applicationName, domainName, buildpackUrl);

        if (app != null)
        {
            if (cs != null)
            {
                // 3. bind to service
                logger.info(">> Binding application to service [" + serviceName + "] ... ");
                _cfclient.bindService(app.getName(), serviceName);
            }

            // 5. Upload application
            if (uploadApplication(app.getName(), warFile))
            {
                // 6. Start application
                logger.info(">> Starting application ... ");
                _cfclient.startApplication(app.getName());
            }
        }
    }


    /**
     * 
     * @param applicationName
     * @param warFile
     * @param buildpackUrl
     * @param serviceOffered
     * @param serviceName
     * @param servicePlan
     * @param freePlan
     */
    public void deployAppWithDatabase(String applicationName, String warFile, String buildpackUrl,
            String serviceOffered, String serviceName, String servicePlan)
    {
        deployAppWithDatabase(applicationName, "", warFile, buildpackUrl, serviceOffered, serviceName, servicePlan);
    }


    /**
     * Deletes an application and the services binded to this application
     * 
     * @param applicationName
     */
    public void deleteApp(String applicationName)
    {
        CloudApplication app = null;

        /*
         * throws CloudFoundryException
         */
        app = _cfclient.getApplication(applicationName);

        // 1. delete application
        logger.info(">> Deleting application [" + applicationName + "] ... ");
        _cfclient.deleteApplication(applicationName);

        // 2. delete services if not attached to other applications
        List<String> lServices = app.getServices(); // services used by the deleted application

        if (lServices.size() > 0)
        {
            List<CloudApplication> lApplications = _cfclient.getApplications(); // other client  applications

            for (String serviceName : lServices)
            {
                // if there are other applications, check if the services are being used by any of them
                if (lApplications.size() > 0)
                {
                    for (CloudApplication capp : lApplications)
                    {
                        List<String> lServices2 = capp.getServices();

                        if (!lServices2.contains(serviceName))
                        {
                            logger.info(">> Deleting service [" + serviceName + "] ... ");
                            _cfclient.deleteService(serviceName);
                        }
                        else
                        {
                            logger.info(">> Service [" + serviceName
                                    + "] is used by other applications ");
                        }
                    }
                }
                // if there are no more applications, then it's safe to delete the service
                else
                {
                    logger.info(">> Deleting service [" + serviceName + "] ... ");
                    _cfclient.deleteService(serviceName);
                }
            }
        }
        logger.info(">> [" + applicationName + "] deleted ");
    }


    /**
     * 
     * @param applicationName
     * @param domainName
     * @param buildpackUrl
     * @return
     */
    public CloudApplication createApplication(String applicationName, String domainName, String buildpackUrl)
    {
        // initialize parameters ...
        // buildpack: -b https://github.com/cloudfoundry/java-buildpack.git
        Staging staging = null;
        if (buildpackUrl != null)
        {
            staging = new Staging(null, buildpackUrl);
        }
        else
        {
            staging = new Staging();
        }

        // uris
        List<String> uris = new ArrayList<String>();
        uris.add(computeAppUrl(applicationName, domainName));

        // serviceNames
        List<String> serviceNames = new ArrayList<String>();

        // 1. Create application
        logger.info(">> Creating application ... ");

        _cfclient.createApplication(applicationName, staging, DEFAULT_MEMORY, uris, serviceNames);
        CloudApplication app = _cfclient.getApplication(applicationName);

        logger.info(">> Application details: " + app.toString());

        return app;
    }


    /**
     * 
     * @param app
     * @param warFile
     * @return
     */
    public boolean uploadApplication(String appName, String warFile)
    {
        String path;
            
        try {
            
            File f = new File(warFile);
            if (!f.isFile()) {
                throw new IllegalArgumentException(f.getPath() + " is not a regular file");
            }
            path = f.getCanonicalPath();
            
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
        
        try
        {
            // 2. Upload application
            logger.info(">> Uploading application from " + path + " ... ");
            _cfclient.uploadApplication(appName, path);
            return true;
        }
        catch (IOException e)
        {
            throw new PaasProviderException(e.getMessage(), e);
        }
    }


    public void updateEnvironment(String appName, Map<String, String> vars) {
        _cfclient.updateApplicationEnv(appName, vars);
    }

    
    public void updateEnvironment(CloudApplication app, Map<String, String> vars) {
        _cfclient.updateApplicationEnv(app.getName(), vars);
    }
    /**
     * 
     * @param serviceOffered
     * @param serviceName
     * @param servicePlan
     * @return
     */
    public CloudService createService(String serviceOffered, String serviceName, String servicePlan)
    {
        logger.info(">> Looking for installed services ... ");
        CloudService cs = _cfclient.getService(serviceName);

        if ((cs != null) && (cs.getLabel().equalsIgnoreCase(serviceOffered)))
        {
            logger.info(">> Service already installed ... ");
            return cs;
        }

        logger.info(">> Creating service [" + serviceOffered + ", " + servicePlan + "] ... ");

        try
        {
            List<CloudServiceOffering> l = _cfclient.getServiceOfferings();

            for (CloudServiceOffering cservice : l)
            {
                if (cservice.getName().equalsIgnoreCase(serviceOffered))
                {
                    // create service object
                    CloudService newService = new CloudService();

                    newService.setLabel(cservice.getName());
                    newService.setName(serviceName);
                    newService.setProvider(cservice.getProvider());
                    newService.setVersion(cservice.getVersion());
                    newService.setMeta(cservice.getMeta());
                    newService.setPlan(null);

                    List<CloudServicePlan> lPlans = cservice.getCloudServicePlans();
                    for (CloudServicePlan plan : lPlans)
                    {
                        // look for a plan with name = 'servicePlan'
                        if (plan.getName().equalsIgnoreCase(servicePlan))
                        {
                            newService.setPlan(servicePlan);
                            break;
                        }
                    }

                    if (newService.getPlan() != null)
                    {
                        _cfclient.createService(newService);
                        logger.info(">> Service created ... ");
                        return newService;
                    }

                    break;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        logger.warn(">> Service was not created");
        return null;
    }


    /**
     * 
     * @param target
     * @return
     */
    private URL getTargetURL(String target)
    {
        try
        {
            return URI.create(target).toURL();
        }
        catch (MalformedURLException e)
        {
            throw new RuntimeException("The target URL is not valid: " + e.getMessage());
        }
    }


    /**
     * 
     * @param appName
     * @param domainName
     * @return
     */
    private String computeAppUrl(String appName, String domainName)
    {
        return appName + "." + ((domainName.equals("") || domainName.equals(appName)) ? _cfclient.getDefaultDomain().getName() : domainName);
    }


}
