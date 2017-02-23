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
import java.util.Objects;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

/**
 * Wrapper around a JGit Repository
 */
public class Repository {

    private org.eclipse.jgit.lib.Repository repo;
    private File projectDir;
    
    public Repository(File projectDir) throws IOException {
        
        if (!projectDir.isDirectory()) {
            throw new IllegalArgumentException(projectDir + " must be a directory");
        }
        this.projectDir = Objects.requireNonNull(projectDir);
        
        FileRepositoryBuilder builder = new FileRepositoryBuilder().findGitDir(this.projectDir);

        if (builder.getGitDir() == null) {
            throw new IllegalArgumentException("Could not find a Git repository up from " + projectDir);
        }

        this.repo = builder.build();
    }
    
    public Git buildGit() {
        return new Git(repo);
    }
    
    public void close() {
        this.repo.close();
    }

    @Override
    public String toString() {
        return String.format("Repository [repo=%s, projectDir=%s]", repo, projectDir);
    }

    
}