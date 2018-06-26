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

import java.net.URI;
import java.util.List;
import java.util.Map;


public interface Module {
    
    String getName();
    
    URI getUrl();
    
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