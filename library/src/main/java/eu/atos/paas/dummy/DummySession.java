package eu.atos.paas.dummy;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import eu.atos.paas.Module;
import eu.atos.paas.PaasException;
import eu.atos.paas.PaasSession;
import eu.atos.paas.ServiceApp;

public class DummySession implements PaasSession {

    private static Map<String, ModuleImpl> modules = new HashMap<String, ModuleImpl>();
    
    public DummySession() {
    }

    @Override
    public Module deploy(String moduleName, DeployParameters params) throws PaasException {
        URL url;
        try {
            url = new URL("http://www.example.com/" + moduleName);
        } catch (MalformedURLException e) {
            throw new PaasException(e.getMessage(), e);
        }
        ModuleImpl m = new ModuleImpl(moduleName, url, "web", 1);
        modules.put(moduleName, m);
        return m;
    }

    @Override
    public void undeploy(String moduleName) throws PaasException {
        modules.remove(moduleName);
    }

    @Override
    public void startStop(Module module, StartStopCommand command) throws PaasException, UnsupportedOperationException {
        return;
    }

    @Override
    public void scaleUpDown(Module module, ScaleUpDownCommand command)
            throws PaasException, UnsupportedOperationException {

        ModuleImpl m = modules.get(module.getName());
        switch (command) {
        case SCALE_UP_INSTANCES:
            m.scaleInstances(m.getRunningInstances() + 1);
            break;
        case SCALE_DOWN_INSTANCES:
            m.scaleInstances(m.getRunningInstances() + 1);
            break;
        default:
        }
        return;
    }

    @Override
    public void scale(Module module, ScaleCommand command, int scale_value)
            throws PaasException, UnsupportedOperationException {
        
        ModuleImpl m = modules.get(module.getName());
        switch (command) {
        case SCALE_INSTANCES:
            m.scaleInstances(scale_value);
            break;
        default:
        }
    }

    @Override
    public void bindToService(Module module, ServiceApp service) throws PaasException {
        
        ModuleImpl m = modules.get(module.getName());
        m.addService(service.getServiceName());
    }

    @Override
    public void unbindFromService(Module module, ServiceApp service) throws PaasException {
        ModuleImpl m = modules.get(module.getName());
        m.delService(service.getServiceName());
    }

    @Override
    public Module getModule(String moduleName) throws PaasException {
        ModuleImpl m = modules.get(moduleName);
        return m;
    }

}
