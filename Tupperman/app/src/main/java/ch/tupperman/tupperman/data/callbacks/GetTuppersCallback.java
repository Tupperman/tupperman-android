package ch.tupperman.tupperman.data.callbacks;

import java.util.List;

import ch.tupperman.tupperman.models.Tupper;


public interface GetTuppersCallback {
    void onSuccess(List<Tupper> tuppers);
    void onError(String message);
}
