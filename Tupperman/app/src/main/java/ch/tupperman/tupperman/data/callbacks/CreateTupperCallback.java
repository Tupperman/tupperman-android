package ch.tupperman.tupperman.data.callbacks;

import ch.tupperman.tupperman.models.Tupper;

public interface CreateTupperCallback {
    void onSuccess();
    void onError(String message);
}

