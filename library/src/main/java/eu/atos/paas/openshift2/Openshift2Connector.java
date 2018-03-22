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
package eu.atos.paas.openshift2;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openshift.client.ApplicationScale;
import com.openshift.client.ConnectionBuilder;
import com.openshift.client.IApplication;
import com.openshift.client.IDomain;
import com.openshift.client.IEnvironmentVariable;
import com.openshift.client.IOpenShiftConnection;
import com.openshift.client.OpenShiftException;
import com.openshift.client.cartridge.IEmbeddableCartridge;
import com.openshift.client.cartridge.IEmbeddedCartridge;
import com.openshift.client.cartridge.StandaloneCartridge;


/**
 * 
 *
 * @author ATOS
 * @date 19/2/2016-16:01:46
 */
public class Openshift2Connector
{
    
    
    // logger
    private static final Logger logger = LoggerFactory.getLogger(Openshift2Connector.class);
    
    // Openshift client
    private IOpenShiftConnection _of2client;
    
    
    /**
     * 
     * @param login
     * @param passwd
     */
    public Openshift2Connector(URL serverUrl, String login, String passwd)
    {
        logger.info(">> Connecting to Openshift2 ...");
        try
        {
            _of2client = new ConnectionBuilder(serverUrl.toString()).credentials(login, passwd).create();
        }
        catch (OpenShiftException e)
        {
            logger.warn(">> Not connected to Openshift2: " + e.getMessage());
            throw new RuntimeException("Not connected to Openshift2: " + e.getMessage(), e);
        }
        catch (IOException e)
        {
            logger.error(">> Not connected to Openshift2: " + e.getMessage());
            throw new RuntimeException("Not connected to Openshift2: " + e.getMessage(), e);
        }
    }
    
    
    /**
     * Returns the client object
     * 
     * @return
     */
    public IOpenShiftConnection getConnectedClient()
    {
        return _of2client;
    }
    
    
    /**
     * Deploy in a specific domain
     * @param applicationName
     * @param domainId
     * @param gitURL
     * @param appType
     */
    public IApplication deployAppFromGit(String applicationName, String domainId, String gitURL, String appType)
    {
        logger.info(">> Creating application '" + applicationName + "' in domain '" + domainId + "' ...");
        
        for (IDomain d : _of2client.getDomains())
        {
            if (d.getId().equalsIgnoreCase(domainId))
            {
                return d.createApplication(applicationName, new StandaloneCartridge(appType), ApplicationScale.SCALE, null, gitURL);
            }
        }
        
        logger.warn(">> Domain '" + domainId + "' not found");
        return deployAppFromGit(applicationName, gitURL, appType);
    }
    
    
    /**
     * Deploy in the first available domain
     * @param applicationName
     * @param gitURL
     * @param appType
     * @return
     */
    public IApplication deployAppFromGit(String applicationName, String gitURL, String appType)
    {
        logger.info(">> Creating application '" + applicationName + "' in default domain ...");
        
        return _of2client.getDomains().get(0).createApplication(applicationName, 
                                                                new StandaloneCartridge(appType), 
                                                                ApplicationScale.SCALE,
                                                                null,
                                                                gitURL);
    }
    
    
    /**
     * 
     * @param applicationName
     */
    public void deleteApp(String applicationName) 
    {
        logger.info(">> Deleting application '" + applicationName + "' ...");
        
        IApplication app = getAppFromDomains(applicationName);
        if (app != null)
        {
            app.destroy();
            logger.info(">> Application '" + applicationName + "' deleted");
        }
    }
    
    
    /**
     * 
     * @param applicationName
     */
    public void startApp(String applicationName) 
    {
        logger.info(">> Starting application '" + applicationName + "' ...");
        
        IApplication app = getAppFromDomains(applicationName);
        if (app != null)
        {
            app.start();
            logger.info(">> Application '" + applicationName + "' started");
        }
    }
    
    
    /**
     * 
     * @param applicationName
     */
    public void stopApp(String applicationName) 
    {
        logger.info(">> Stopping application '" + applicationName + "' ...");
        
        IApplication app = getAppFromDomains(applicationName);
        if (app != null)
        {
            app.stop();
            logger.info(">> Application '" + applicationName + "' stopped");
        }
    }
    
    
    /**
     * 
     * @param applicationName
     */
    public void scaleUp(String applicationName) 
    {
        logger.info(">> Scaling (up) application '" + applicationName + "' ...");
        
        IApplication app = getAppFromDomains(applicationName);
        if (app != null)
        {
            app.scaleUp();
            logger.info(">> Application '" + applicationName + "' scaled");
        }
    }
    
    
    /**
     * 
     * @param applicationName
     */
    public void scaleDown(String applicationName) 
    {
        logger.info(">> Scaling (down) application '" + applicationName + "' ...");
        
        IApplication app = getAppFromDomains(applicationName);
        if (app != null)
        {
            app.scaleDown();
            logger.info(">> Application '" + applicationName + "' scaled");
        }
    }
    
    
    /**
     * 
     * @param applicationName
     * @return
     */
    public Map<String, IEnvironmentVariable> getEnvProperties(String applicationName)
    {
        IApplication app = getAppFromDomains(applicationName);
        if (app != null)
        {
            return app.getEnvironmentVariables();
        }
        
        return null;
    }
    
    
    /**
     * 
     * @param applicationName
     * @param serviceName
     * @return
     */
    public IEmbeddedCartridge bindToService(String applicationName, String serviceName) 
    {
        logger.info(">> Binding application '" + applicationName + "' to service '" + serviceName + "' ...");
        IApplication app = getAppFromDomains(applicationName);
        if (app != null)
        {
            for (IEmbeddableCartridge cartridge : _of2client.getEmbeddableCartridges())
            {
                if (cartridge.getName().equalsIgnoreCase(serviceName))
                {
                    return app.addEmbeddableCartridge(cartridge);
                }
            }
        }
        
        return null;
    }
    
    
    /**
     * 
     * @param applicationName
     * @param serviceName
     * @return
     */
    public boolean unbindFromService(String applicationName, String serviceName) 
    {
        logger.info(">> Unbinding application '" + applicationName + "' from service '" + serviceName + "' ...");
        IApplication app = getAppFromDomains(applicationName);
        if (app != null)
        {
            for (IEmbeddableCartridge cartridge : app.getEmbeddedCartridges())
            {
                if (cartridge.getName().equalsIgnoreCase(serviceName))
                {
                    app.removeEmbeddedCartridge(cartridge);
                    return true;
                }
            }
        }
        
        return false;
    }
    
    
    /**
     * 
     * @param applicationName
     * @return
     */
    public IApplication getAppFromDomains(String applicationName)
    {
        if (_of2client != null)
        {
            for (IDomain d : _of2client.getDomains())
            {
                IApplication app = d.getApplicationByName(applicationName);
                if (app != null)
                {
                    return app;
                }
            }
        }
        return null;
    }
        

}
