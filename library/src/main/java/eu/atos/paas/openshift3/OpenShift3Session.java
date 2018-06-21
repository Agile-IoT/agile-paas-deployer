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
package eu.atos.paas.openshift3;

import com.openshift.client.OpenShiftException;
import com.openshift.restclient.IClient;
import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.authorization.ResourceForbiddenException;
import com.openshift.restclient.model.IDeploymentConfig;
import com.openshift.restclient.model.IResource;
import com.openshift.restclient.model.IService;
import com.openshift.restclient.model.route.IRoute;

import eu.atos.paas.AlreadyExistsException;
import eu.atos.paas.ForbiddenException;
import eu.atos.paas.Module;
import eu.atos.paas.NotDeployedException;
import eu.atos.paas.NotFoundException;
import eu.atos.paas.PaasException;
import eu.atos.paas.PaasProviderException;
import eu.atos.paas.PaasSession;
import eu.atos.paas.PaasSession.DeployParameters.Properties;
import eu.atos.paas.ServiceApp;

public class OpenShift3Session implements PaasSession {

    public static final String DEFAULT_PROJECT = "default-project";
    public static final int DEFAULT_PORT = 8080;
    
    private IClient client;
    private OpenShift3Connector connector;
    
    public OpenShift3Session(IClient osClient) {
        this.client = osClient;
        this.connector = new OpenShift3Connector(osClient);
    }

    @Override
    public Module createApplication(String moduleName, DeployParameters params)
            throws PaasProviderException, AlreadyExistsException {
        
        String[] parts = splitName(moduleName);
        String projectName = parts[0];
        String appName = parts[1];
        String dcName = buildDcName(appName);
        String serviceName = buildServiceName(appName);
        String routeName = buildRouteName(appName);
        
        Module m = getModuleImpl(projectName, appName);
        
        if (m != null) {
            throw new AlreadyExistsException(moduleName);
        }
        
        int instances = params.getPropertyAsInt(Properties.INSTANCES, 1);
        
        IDeploymentConfig dc = connector.createDcForContainer(
                projectName, 
                appName, 
                dcName, 
                params.getImageName(), 
                params.getEnvs(), 
                instances);
        
        int port = params.getPropertyAsInt(Properties.PORT, DEFAULT_PORT);
        IService service = connector.createService(projectName, appName, serviceName, port);
        
        String appType = params.getProperty(Properties.APPLICATION_TYPE, ApplicationType.WEB);
        IRoute route = null;
        if (ApplicationType.WEB.equals(appType)) {
            route = client.getResourceFactory().stub(ResourceKind.ROUTE, routeName, projectName);
            route.setServiceName(serviceName);
            route = client.create(route);
        }
        
        return new ModuleImpl(service, dc, route);
    }

    @Override
    public Module updateApplication(String moduleName, DeployParameters params)
            throws NotFoundException, PaasProviderException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Module deploy(String moduleName, DeployParameters params)
            throws PaasProviderException, AlreadyExistsException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void undeploy(String moduleName) throws NotFoundException, PaasProviderException {
        
        String[] parts = splitName(moduleName);
        String serviceName = buildServiceName(parts[1]);
        String dcName = buildDcName(parts[1]);
        String routeName = buildRouteName(parts[1]);
        
        IService service = getResource(ResourceKind.SERVICE, parts[0], serviceName);
        IDeploymentConfig dc = getResource(ResourceKind.DEPLOYMENT_CONFIG, parts[0], dcName);
        IRoute route = getResource(ResourceKind.ROUTE, parts[0], routeName);
        
        if (service != null) {
            client.delete(service);
        }
        if (dc != null) {
            client.delete(dc);
        }
        if (route != null) {
            client.delete(route);
        }
        
    }

    @Override
    public void startStop(Module module, StartStopCommand command)
            throws NotFoundException, NotDeployedException, PaasProviderException {
        throw new UnsupportedOperationException("Unsupported startStop");
    }

    @Override
    public void scaleUpDown(Module module, ScaleUpDownCommand command)
            throws PaasException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Unsupported scaleUpDown");
    }

    @Override
    public void scale(Module module, ScaleCommand command, int scale_value)
            throws PaasException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Unsupported scale");
    }

    @Override
    public void bindToService(Module module, ServiceApp service) throws PaasException {
        throw new UnsupportedOperationException("Unsupported bindToService");
    }

    @Override
    public void unbindFromService(Module module, ServiceApp service) throws PaasException {
        throw new UnsupportedOperationException("Unsupported unbindFromService");
    }

    @Override
    public Module getModule(String moduleName) throws PaasException {
        
        String[] parts = splitName(moduleName);
        Module m = getModuleImpl(parts[0], parts[1]);
        
        return m;
    }
    
    private Module getModuleImpl(String projectName, String moduleName) {
        
        String serviceName = buildServiceName(moduleName);
        String dcName = buildDcName(moduleName);
        String routeName = buildRouteName(moduleName);
        
        IService service = getResource(ResourceKind.SERVICE, projectName, serviceName);
        if (service == null) {
            return null;
        }
        IDeploymentConfig dc = getResource(ResourceKind.DEPLOYMENT_CONFIG, projectName, dcName);
        if (dc == null) {
            throw new IllegalStateException(String.format(
                    "Cannot exist service %s without deploymentconfig %s", serviceName, dcName));
        }
        IRoute route = getResource(ResourceKind.ROUTE, projectName, routeName);
        
        Module m = new ModuleImpl(service, dc, route);
        return m;
    }
    
    /*
     * Just a wrapper over client.get to throw exceptions as needed by PaasSession interface
     * Returns null if resource is not found
     */
    private <T extends IResource> T getResource(String kind, String projectName, String moduleName) {

        try {
            
            T resource = client.get(kind, moduleName, projectName);
            return resource;
            
        } catch (com.openshift.restclient.NotFoundException e) {
            
            return null;
            
        } catch (ResourceForbiddenException e) {
            
            throw new ForbiddenException(String.format("%s %s/%s", kind, projectName, moduleName), e);
        
        } catch (OpenShiftException e) {
            
            throw new PaasProviderException(e.getMessage(), e);
        }
    }
    
    private String[] splitName(String moduleName) {
        String[] parts = moduleName.split("/");

        if (parts.length > 2) {
            throw new IllegalArgumentException(moduleName + " is not a valid module name");
        } else if (parts.length < 2) {
            parts = new String[] { DEFAULT_PROJECT, parts[0] };
        }
        return parts;
    }
    
    private String buildDcName(String appName) {
        return appName + "-dc";
    }

    private String buildServiceName(String appName) {
        return appName;
    }
    
    private String buildRouteName(String appName) {
        return appName + "-route";
    }
    
    public IClient getClient() {
        return client;
    }
}
