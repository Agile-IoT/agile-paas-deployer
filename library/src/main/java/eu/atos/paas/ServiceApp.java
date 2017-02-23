/**
 * Copyright 2016 Atos
 * Contact: Atos <roman.sosa@atos.net>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
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
