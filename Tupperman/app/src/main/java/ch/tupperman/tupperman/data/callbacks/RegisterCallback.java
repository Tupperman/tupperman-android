package ch.tupperman.tupperman.data.callbacks;

public interface RegisterCallback {
    void registerSuccess(String token);
    void registerError(String message);
}
