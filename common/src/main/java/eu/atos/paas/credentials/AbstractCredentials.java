package eu.atos.paas.credentials;

public abstract class AbstractCredentials {
    protected void checkNull(String value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
    }
}