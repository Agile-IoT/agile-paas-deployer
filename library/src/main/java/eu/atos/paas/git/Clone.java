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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Objects;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Clone {
    private static Logger logger = LoggerFactory.getLogger(Clone.class);
    private File destProjectDir;
    private URL sourceUrl;
    
    public Clone(URL sourceUrl) throws IOException {
        
        this.sourceUrl = Objects.requireNonNull(sourceUrl);
        this.destProjectDir = Files.createTempDirectory("pul").toFile();
    }
    
    public Repository call() throws GitAPIException, IOException {

        CloneCommand cmd = Git.cloneRepository();
        logger.debug("Start git clone {} into {}", sourceUrl, destProjectDir.getPath());
        Git gitRepo = cmd
                .setURI(sourceUrl.toString())
                .setDirectory(destProjectDir)
                .call();
        gitRepo.close();
        logger.debug("End git clone {}", sourceUrl);
        
        return new Repository(destProjectDir);
    }
}
