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
