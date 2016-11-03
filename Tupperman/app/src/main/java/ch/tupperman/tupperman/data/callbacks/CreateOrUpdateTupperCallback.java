package ch.tupperman.tupperman.data.callbacks;


public interface CreateOrUpdateTupperCallback {
    void onSuccess();
    void onError(String message);
}

