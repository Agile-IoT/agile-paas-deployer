package eu.atos.paas.openshift3;

import com.openshift.restclient.IClient;

import eu.atos.paas.AlreadyExistsException;
import eu.atos.paas.Module;
import eu.atos.paas.NotDeployedException;
import eu.atos.paas.NotFoundException;
import eu.atos.paas.PaasException;
import eu.atos.paas.PaasProviderException;
import eu.atos.paas.PaasSession;
import eu.atos.paas.ServiceApp;

public class Openshift3Session implements PaasSession {

    private IClient osClient;
    
    public Openshift3Session(IClient osClient) {
        this.osClient = osClient;
    }

    @Override
    public Module createApplication(String moduleName, DeployParameters params)
            throws PaasProviderException, AlreadyExistsException {
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub

    }

    @Override
    public void startStop(Module module, StartStopCommand command)
            throws NotFoundException, NotDeployedException, PaasProviderException {
        // TODO Auto-generated method stub

    }

    @Override
    public void scaleUpDown(Module module, ScaleUpDownCommand command)
            throws PaasException, UnsupportedOperationException {
        // TODO Auto-generated method stub

    }

    @Override
    public void scale(Module module, ScaleCommand command, int scale_value)
            throws PaasException, UnsupportedOperationException {
        // TODO Auto-generated method stub

    }

    @Override
    public void bindToService(Module module, ServiceApp service) throws PaasException {
        // TODO Auto-generated method stub

    }

    @Override
    public void unbindFromService(Module module, ServiceApp service) throws PaasException {
        // TODO Auto-generated method stub

    }

    @Override
    public Module getModule(String moduleName) throws PaasException {
        // TODO Auto-generated method stub
        return null;
    }

}
