package eu.atos.paas.credentials;

public class CredentialsUtils {

    public static String shadowPassword(String passwd) {
        return passwd.replaceAll(".", "*");
    }
    
    public static String shadowJsonCredentials(String credentials) {
        return credentials.replaceFirst("\"password\"[^,}]*", "\"password\":\"***\"");
    }
}
