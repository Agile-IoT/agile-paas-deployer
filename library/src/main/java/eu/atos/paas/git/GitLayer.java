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
import java.nio.file.Files;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;

public class GitLayer {

    public static void cloneAndPush(String from, String to, CredentialsProvider credentials) 
            throws IOException, GitAPIException {

        File tmpPath = Files.createTempDirectory("pul").toFile();
        GitLayer.cloneAndPush(from, tmpPath, to, credentials);
    }

    public static void cloneAndPush(String from, File whereToClone, String to, CredentialsProvider credentials) 
            throws IOException, GitAPIException {

        Repository repo = Repository.clone(from, whereToClone);
        repo.push(to, credentials);
        repo.close();
    }
    
    public static void initAndPush(File from, String to, CredentialsProvider credentials) 
            throws GitAPIException, IOException {
        
        Repository repo = Repository.init(from);
        repo.add(".");
        repo.commit("Initial import");
        repo.push(to, credentials);
        repo.close();
    }
}
