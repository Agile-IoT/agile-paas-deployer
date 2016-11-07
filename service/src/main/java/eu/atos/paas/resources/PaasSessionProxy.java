package eu.atos.paas.resources;

import java.util.Objects;

import javax.ws.rs.core.Response.Status;

import eu.atos.paas.AlreadyExistsException;
import eu.atos.paas.Module;
import eu.atos.paas.NotFoundException;
import eu.atos.paas.PaasException;
import eu.atos.paas.PaasProviderException;
import eu.atos.paas.PaasSession;
import eu.atos.paas.ServiceApp;
import eu.atos.paas.resources.exceptions.ResourceException;

/**
 * Proxy that converts any library exception into a REST exception. 
 * 
 * @see PaaSResource to check the correspondency between library exceptions and REST exceptions.
 *
 */
/*
 * NOTE: handle() translates from library exceptions to REST exceptions. Add any new correspondency there.
 */
public class PaasSessionProxy implements PaasSession {

    private PaasSession session;
    
    public PaasSessionProxy(PaasSession session) {
        this.session = Objects.requireNonNull(session);
    }

    @Override
    public Module deploy(String moduleName, DeployParameters params)
            throws PaasProviderException, AlreadyExistsException {
        
        try {
            return session.deploy(moduleName, params);
        } catch (RuntimeException e) {
            handle(e);
            throw new AssertionError("this should not be reached");
        }
    }

    @Override
    public void undeploy(String moduleName) throws NotFoundException, PaasProviderException {

        try {
            session.undeploy(moduleName);
        } catch (RuntimeException e) {
            handle(e);
        }
    }

    @Override
    public void startStop(Module module, StartStopCommand command) throws NotFoundException, PaasProviderException {

        try {
            session.startStop(module, command);
        } catch (RuntimeException e) {
            handle(e);
        }
    }

    @Override
    public void scaleUpDown(Module module, ScaleUpDownCommand command)
            throws PaasException, UnsupportedOperationException {

        try {
            session.scaleUpDown(module, command);
        } catch (RuntimeException e) {
            handle(e);
        }
    }

    @Override
    public void scale(Module module, ScaleCommand command, int scale_value)
            throws PaasException, UnsupportedOperationException {

        try {
            session.scale(module, command, scale_value);
        } catch (RuntimeException e) {
            handle(e);
        }
    }

    @Override
    public void bindToService(Module module, ServiceApp service) throws PaasException {

        try {
            session.bindToService(module, service);
        } catch (RuntimeException e) {
            handle(e);
        }
    }

    @Override
    public void unbindFromService(Module module, ServiceApp service) throws PaasException {

        try {
            session.unbindFromService(module, service);
        } catch (RuntimeException e) {
            handle(e);
        }
    }

    @Override
    public Module getModule(String moduleName) throws PaasException {

        try {
            return session.getModule(moduleName);
        } catch (RuntimeException e) {
            handle(e);
            throw new AssertionError("this should not be reached");
        }
    }

    /*
     * Modifying library exceptions to throw the right exception is not desired.
     * 
     * Other implementations:
     * - throw the same exception and catch
     * - a switch with the class name
     * - static map
     */
    private void handle(RuntimeException e) {

        if (e instanceof PaasProviderException) {
            throw new ResourceException(new ErrorEntity(Status.BAD_GATEWAY, e.getMessage()));
        } else if (e instanceof NotFoundException) {
            throw new ResourceException(new ErrorEntity(Status.NOT_FOUND, e.getMessage()));
        } else if (e instanceof AlreadyExistsException) {
            throw new ResourceException(new ErrorEntity(Status.CONFLICT, e.getMessage()));
        } else if (e instanceof UnsupportedOperationException) {
            throw new ResourceException(new ErrorEntity(Status.NOT_IMPLEMENTED, e.getMessage()));
        } else {
            throw e;
        }
    }
}
