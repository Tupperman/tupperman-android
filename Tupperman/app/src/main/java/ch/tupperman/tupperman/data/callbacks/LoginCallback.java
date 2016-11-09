package ch.tupperman.tupperman.data.callbacks;

public interface LoginCallback {
    void onSuccess(String token);
    void onError(String message);
}
