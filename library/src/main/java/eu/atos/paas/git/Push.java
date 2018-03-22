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
package eu.atos.paas.git;

import java.util.Objects;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Push {
    private static Logger logger = LoggerFactory.getLogger(Push.class);
    private Repository repository;
    
    public Push(Repository repository) {

        this.repository = Objects.requireNonNull(repository);
    }
    
    public void call(String remote, CredentialsProvider credentials) throws GitAPIException {
        
        logger.debug("Start git push {} from {}", remote, repository);
        try (Git git = repository.buildGit()) {
            git.push()
                .setCredentialsProvider(credentials)
                .setRemote(remote)
                .call();
        }
        logger.debug("End git push {}", remote);
    }
    
}
