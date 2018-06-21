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

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.InitCommand;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.Transport;
import org.eclipse.jgit.transport.OpenSshConfig.Host;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Session;

/**
 * Wrapper around a JGit Repository
 */
public class Repository {
    private static Logger logger = LoggerFactory.getLogger(Repository.class);

    private org.eclipse.jgit.lib.Repository repo;
    private File projectDir;

    private SshSessionFactory sshSessionFactory;
    private TransportConfigCallback transportConfigCallback;
    
    public static Repository clone(String from, File dest) throws GitAPIException, IOException {
        
        CloneCommand cmd = Git.cloneRepository();
        logger.debug("Start git clone {} into {}", from, dest.getPath());
        Git gitRepo = cmd
                .setURI(from)
                .setDirectory(dest)
                .call();
        gitRepo.close();
        logger.debug("End git clone {}", from);
        
        return new Repository(dest);
    }
    
    public static Repository init(File path) throws GitAPIException, IOException {
        
        InitCommand cmd = Git.init();
        logger.debug("Start git init in {}", path.getPath());
        Git gitRepo = cmd
                .setDirectory(path)
                .call();
        gitRepo.close();
        logger.debug("End git init in {}", path.getPath());
        return new Repository(path);
    }
    
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
    
    public void add(String relativePath) throws GitAPIException {
        
        logger.debug("Start git add {} in {}", "", this);
        try (Git git = buildGit()) {
            git.add()
                .addFilepattern(relativePath)
                .call();
        }
        logger.debug("End git add {}", relativePath);
    }

    public void commit(String message) throws GitAPIException {
        logger.debug("Start git commit in {}", this);
        try (Git git = buildGit()) {
            git.commit()
                .setMessage(message)
                .call();
        }
        logger.debug("End git commit in {}", this);
    }
    
    public void push(String remote, CredentialsProvider credentials) throws GitAPIException {
        logger.debug("Start git push {} from {}", remote, this);
        try (Git git = buildGit()) {
            PushCommand cmd = git.push()
                    .setRemote(remote);
            
            if (remote.startsWith("git:")) {
                cmd.setTransportConfigCallback(getTransportConfigCallback());
                /*
                 * TODO: set pwd if needed
                 */
            }
            else if (credentials != null) {
                cmd.setCredentialsProvider(credentials);
            }
            cmd.call();
        }
        logger.debug("End git push {}", remote);
    }
    
    private TransportConfigCallback getTransportConfigCallback() {
        
        if (transportConfigCallback == null) {
            transportConfigCallback = new TransportConfigCallback() {
                @Override
                public void configure( Transport transport ) {
                  SshTransport sshTransport = ( SshTransport )transport;
                  sshTransport.setSshSessionFactory( getSshSessionFactory() );
                }
            };
        }
        return transportConfigCallback;
    }
    
    private SshSessionFactory getSshSessionFactory() {
        
        if (sshSessionFactory == null) {
            sshSessionFactory = new JschConfigSessionFactory() {
                
                @Override
                protected void configure(Host hc, Session session) {
                    /*
                     * does nothing
                     */
                }
                
            };
        }
        return sshSessionFactory;
    }
    
    public void close() {
        this.repo.close();
    }

    @Override
    public String toString() {
        return String.format("Repository [repo=%s, projectDir=%s]", repo, projectDir);
    }

    
}