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


/**
 * 
 *
 * @author ATOS
 * @date 21/3/2016-16:14:27
 */
public class Openshift3Connector
{
    
    
    /**
     * 
     * Constructor
     * @param url
     * @param login
     * @param passwd
     */
    public Openshift3Connector(String url, String login, String passwd)
    {
        //IClient client = new ClientFactory().create(url, new NoopSSLCertificateCallback());
        //client.setAuthorizationStrategy(new BasicAuthorizationStrategy(login, passwd, ""));
        
        throw new UnsupportedOperationException("Openshift3 connector not implemented");
    }
    
    
}
