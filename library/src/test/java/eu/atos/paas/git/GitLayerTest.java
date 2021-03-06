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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.*;

@Test
public class GitLayerTest {

    @Test
    public void cloneAndPush() throws MalformedURLException, IOException, GitAPIException {

        Path initialPath = createWorkingDir();
        createRepository(initialPath);
        Repository repo = new Repository(initialPath.toFile());
        repo.add(".");
        repo.commit("Initial import");

        Path fromPath = Files.createTempDirectory("pul");
        
        Path toPath = Files.createTempDirectory("pul");
        createRepository(toPath);
        
        System.out.println(String.format("cloneAndPush. initial=%s, from=%s, to=%s", initialPath, fromPath, toPath));

        GitLayer.cloneAndPush(
                initialPath.toString(),
                fromPath.toFile(),
                toPath.toString(),
                new UsernamePasswordCredentialsProvider("",  new char[] {}));
    }
    
    @Test
    public void initAndPush() throws MalformedURLException, GitAPIException, IOException {
        
        Path fromPath = createWorkingDir();

        Path toPath = Files.createTempDirectory("pul");
        createRepository(toPath);
        
        System.out.println(String.format("initAndPush. from=%s, to=%s", fromPath, toPath));
        
        GitLayer.initAndPush(
                fromPath.toFile(),
                toPath.toString(), 
                new UsernamePasswordCredentialsProvider("",  new char[] {}));

        Repository repo = new Repository(toPath.toFile());
        assertEquals(1, logsize(repo));
    }

    /*
     * Slow test
     */
    @Test(enabled = false)
    public void cloneRemote() throws IOException, GitAPIException {
        
        URL fromUrl = new URL("https://github.com/rosogon/node-red-azure");
        Path toPath = Files.createTempDirectory("pul");
        System.out.println(String.format("cloneRemote. from=%s, to=%s", fromUrl, toPath));
        Repository.clone(fromUrl.toString(), toPath.toFile());
    }
    
    /*
     * Slow test
     * Needs an empty repository and a valid pubkey in ~/.ssh
     */
    @Test(enabled = false)
    public void cloneAndPushRemote() throws IOException, GitAPIException {
        
        URL initialUrl = new URL("https://github.com/octocat/Spoon-Knife");
        
        Path fromPath = Files.createTempDirectory("pul");
        
        String to = "git@github.com:rosogon/empty-repository.git";
        
        System.out.println(String.format("cloneAndPushRemote. initial=%s, from=%s, to=%s", initialUrl, fromPath, to));

        GitLayer.cloneAndPush(
                initialUrl.toString(),
                fromPath.toFile(),
                to,
                null);
    }

    private Path createWorkingDir() throws IOException {
        
        Path rootPath = Files.createTempDirectory("pul");
        Path dirPath = Files.createDirectory(rootPath.resolve("dir"));
        Files.createFile(rootPath.resolve("file1"));
        Files.createFile(dirPath.resolve("file2"));
                
        return rootPath;
    }
    
    private Path createRepository(Path where) throws IOException, GitAPIException {
        
        Repository.init(where.toFile()).close();
        
        return where;
    }
    
    private int logsize(Repository repository) throws GitAPIException, IOException {
        try (Git git = repository.buildGit()) {
            Iterable<RevCommit> commits = git.log()
                .all()
                .call();
            
            int size = 0;
            for (@SuppressWarnings("unused") RevCommit commit : commits) {
                size++;
            }
            return size;
        }
        
    }

    
}
