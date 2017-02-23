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
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.testng.annotations.Test;

import eu.atos.paas.Groups;
import eu.atos.paas.TestConfigProperties;

@Test(groups = "XXX", enabled = false)
public class GitRepositoryIT {

    @Test
    public void testCreateRepository() throws IOException {
        Repository repo = new Repository(new File("c:/cygwin64/tmp/cs-invoice-ninja-source"));
    }
    
    @Test
    public void testCreateRepositoryShouldFail() throws IOException {
        try {
            
            Repository repo = new Repository(new File("noexiste"));
            
        } catch (IllegalArgumentException e) {
            /* pass */
        }
        
        try {

            Repository repo = new Repository(new File("c:/windows"));
        
        } catch (IllegalArgumentException e) {
            /* pass */
        }
    }
    
    @Test
    public void testPush() throws IOException, GitAPIException {
        String apikey = TestConfigProperties.getInstance().getHeroku_apiKey();
        Repository repo = new Repository(new File("c:/cygwin64/tmp/cs-invoice-ninja-source"));
        
        Push pushCmd = new Push(repo);
        
        pushCmd.call(
                "https://git.heroku.com/secure-reaches-36190.git", 
                new UsernamePasswordCredentialsProvider("", apikey));
    }
    
    @Test
    public void testClone() throws MalformedURLException, IOException, GitAPIException {
        
        Clone cloneCmd = new Clone(new URL("https://github.com/seybi87/cs-invoice-ninja-source.git"));
        Repository repo = cloneCmd.call();
        System.out.println(repo.toString());
    }
}
