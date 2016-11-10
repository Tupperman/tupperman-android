package ch.tupperman.tupperman.data.callbacks;

public interface RegisterCallback {

    public enum Error {
        INVALID_EMAIL_ADDRESS,
        INVALID_PASSWORD,
        EMAIL_TAKEN,
        UNKNOWN
    }

    void registerSuccess();
    void registerError(Error error);
}
