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
package eu.atos.paas;


/**
 * 
 * @author
 *
 */
public class ServiceApp
{

    
    // third party service name: cleardb
    private String serviceName; 
    // service name for app: mycleardb
    private String serviceInstanceName;
    // service plan: spark (= free plan)
    private String servicePlan;

    
    public ServiceApp()
    {
        this.serviceName = "";
        this.serviceInstanceName = "";
        this.servicePlan = "";
    }
    
    /**
     * @param serviceName
     */
    public ServiceApp(String serviceName)
    {
        this.serviceName = serviceName;
        this.serviceInstanceName = "";
        this.servicePlan = "";
    }


    /**
     * @return the serviceName
     */
    public String getServiceName()
    {
        return serviceName;
    }
    
    
    /**
     * @param serviceName the serviceName to set
     */
    public void setServiceName(String serviceName)
    {
        this.serviceName = serviceName;
    }
    
    
    /**
     * @return the serviceInstanceName
     */
    public String getServiceInstanceName()
    {
        return serviceInstanceName;
    }
    
    
    /**
     * @param serviceInstanceName the serviceInstanceName to set
     */
    public void setServiceInstanceName(String serviceInstanceName)
    {
        this.serviceInstanceName = serviceInstanceName;
    }
    
    
    /**
     * @return the servicePlan
     */
    public String getServicePlan()
    {
        return servicePlan;
    }
    
    
    /**
     * @param servicePlan the servicePlan to set
     */
    public void setServicePlan(String servicePlan)
    {
        this.servicePlan = servicePlan;
    }
    
    
}
