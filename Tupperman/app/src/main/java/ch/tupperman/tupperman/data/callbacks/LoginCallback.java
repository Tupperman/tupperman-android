package ch.tupperman.tupperman.data.callbacks;

public interface LoginCallback {

    public enum Error {
        INVALID_CREDENTIALS,
        UNKNOWN
    }

    void loginSuccess(String token);
    void loginError(Error error);
}
