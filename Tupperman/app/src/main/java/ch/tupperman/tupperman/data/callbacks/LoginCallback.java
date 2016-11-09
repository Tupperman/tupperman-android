package ch.tupperman.tupperman.data.callbacks;

public interface LoginCallback {
    void loginSuccess(String token);
    void loginError(String message);
}
