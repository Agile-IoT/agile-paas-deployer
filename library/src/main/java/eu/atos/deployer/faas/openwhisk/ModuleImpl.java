/**
 * Copyright 2018 Atos
 * Contact: Atos <elena.garrido@atos.net>
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
package eu.atos.deployer.faas.openwhisk;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.atos.deployer.faas.openwhisk.model.ActionInformation;
import eu.atos.paas.Module;

public class ModuleImpl implements Module
{

    ActionInformation actionInformation;
    State currentState = State.UNKNOWN;
    public ModuleImpl(ActionInformation actionInformation) {
        this.actionInformation = actionInformation;
        //there are some of the methods that must do a call to openwhisk, therefore we need the openwhisk connector
        currentState = State.STARTED;
    }

    @Override
    public String getName() {
        return actionInformation.getName();
    }

    @Override
    public URI getUrl() {
        try{
            String url = actionInformation.getUrl();
            if (url == null) {
                // sanity check
                url = String.format("/%s/%s", actionInformation.getNamespace(), actionInformation.getName());
            }
            return new URI(url);
        } catch (URISyntaxException e) {
            /*
             * this should not happen
             */
            throw new IllegalArgumentException("Error in URL=" + actionInformation.getUrl() + " from provider ", e);
        }
    }

    @Override
    public State getState() {
        return currentState;
    }

    @Override
    public String getAppType() {
        return "service";
    }

    @Override
    public int getRunningInstances() {
        return -1;
    }

    @Override
    public List<String> getServices() {
        return Collections.emptyList();
    }

    @Override
    public Map<String, String> getEnv() {
        Map<String, String> m = new HashMap<>();
        /*        for (IEnvironmentVariable v : dc.getEnvironmentVariables()) {
            m.put(v.getName(), v.getValue());
        }*/
        return m;
    }

    @Override
    public String toString() {
        return String.format(
                "ModuleImpl[name=%s, url=%s, state()=%s, type=%s, instances=%s, env=%s]",
                getName(), getUrl(), getState(), getAppType(), getRunningInstances(), getEnv());
    }


}
