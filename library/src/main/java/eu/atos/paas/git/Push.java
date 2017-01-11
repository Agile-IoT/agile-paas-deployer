package eu.atos.paas.git;

import java.util.Objects;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;

public class Push {

    private Repository repository;
    
    public Push(Repository repository) {

        this.repository = Objects.requireNonNull(repository);
    }
    
    public void call(String remote, CredentialsProvider credentials) throws GitAPIException {
        try (Git git = repository.buildGit()) {
            git.push()
                .setCredentialsProvider(credentials)
                .setRemote(remote)
                .call();
        }
    }
    
}
