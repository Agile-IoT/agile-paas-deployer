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

import java.net.URL;
import java.util.List;
import java.util.Map;


public interface Module {
    
    String getName();
    
    URL getUrl();
    
    State getState();
    
    String getAppType();
    
    int getRunningInstances();
    
    List<String> getServices();
    
    /**
     * Get application environment values
     * @return
     */
    Map<String, String> getEnv();
    
    public enum State {
        UNDEPLOYED, STARTED, STOPPED, UPDATING, UNKNOWN
    }
}