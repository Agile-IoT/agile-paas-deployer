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

@Test(groups = Groups.HEROKU, enabled = false)
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
