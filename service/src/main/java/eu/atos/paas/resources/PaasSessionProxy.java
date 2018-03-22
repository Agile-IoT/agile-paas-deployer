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
package eu.atos.paas.resources;

import java.util.Objects;

import javax.ws.rs.core.Response.Status;

import eu.atos.paas.AlreadyExistsException;
import eu.atos.paas.ForbiddenException;
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
 * @see PaasResource to check the correspondency between library exceptions and REST exceptions.
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
    public Module createApplication(String moduleName, DeployParameters params)
            throws PaasProviderException, AlreadyExistsException {

        try {
            return session.createApplication(moduleName, params);
        } catch (RuntimeException e) {
            throw handle(e);
        }
    }
    
    @Override
    public Module updateApplication(String moduleName, DeployParameters params)
            throws NotFoundException, PaasProviderException {

        try {
            return session.updateApplication(moduleName, params);
        } catch (RuntimeException e) {
            throw handle(e);
        }
    }
    
    @Override
    public Module deploy(String moduleName, DeployParameters params)
            throws PaasProviderException, AlreadyExistsException {
        
        try {
            return session.deploy(moduleName, params);
        } catch (RuntimeException e) {
            throw handle(e);
        }
    }

    @Override
    public void undeploy(String moduleName) throws NotFoundException, PaasProviderException {

        try {
            session.undeploy(moduleName);
        } catch (RuntimeException e) {
            throw handle(e);
        }
    }

    @Override
    public void startStop(Module module, StartStopCommand command) throws NotFoundException, PaasProviderException {

        try {
            session.startStop(module, command);
        } catch (RuntimeException e) {
            throw handle(e);
        }
    }

    @Override
    public void scaleUpDown(Module module, ScaleUpDownCommand command)
            throws PaasException, UnsupportedOperationException {

        try {
            session.scaleUpDown(module, command);
        } catch (RuntimeException e) {
            throw handle(e);
        }
    }

    @Override
    public void scale(Module module, ScaleCommand command, int scale_value)
            throws PaasException, UnsupportedOperationException {

        try {
            session.scale(module, command, scale_value);
        } catch (RuntimeException e) {
            throw handle(e);
        }
    }

    @Override
    public void bindToService(Module module, ServiceApp service) throws PaasException {

        try {
            session.bindToService(module, service);
        } catch (RuntimeException e) {
            throw handle(e);
        }
    }

    @Override
    public void unbindFromService(Module module, ServiceApp service) throws PaasException {

        try {
            session.unbindFromService(module, service);
        } catch (RuntimeException e) {
            throw handle(e);
        }
    }

    @Override
    public Module getModule(String moduleName) throws PaasException {

        try {
            return session.getModule(moduleName);
        } catch (RuntimeException e) {
            throw handle(e);
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
    private RuntimeException handle(RuntimeException e) {

        if (e instanceof PaasProviderException) {
            return new ResourceException(new ErrorEntity(Status.BAD_GATEWAY, e.getMessage()), e);
        } 
        else if (e instanceof NotFoundException) {
            return new ResourceException(new ErrorEntity(Status.NOT_FOUND, e.getMessage()));
        } 
        else if (e instanceof AlreadyExistsException) {
            return new ResourceException(new ErrorEntity(Status.CONFLICT, e.getMessage()));
        } 
        else if (e instanceof UnsupportedOperationException) {
            return new ResourceException(new ErrorEntity(Status.NOT_IMPLEMENTED, e.getMessage()));
        } 
        else if (e instanceof IllegalArgumentException) {
            return new ResourceException(new ErrorEntity(Status.BAD_REQUEST, e.getMessage()));
        } 
        else if (e instanceof ForbiddenException) {
            return new ResourceException(new ErrorEntity(Status.FORBIDDEN, e.getMessage()));
        } 
        else {
            return e;
        }
    }
}
