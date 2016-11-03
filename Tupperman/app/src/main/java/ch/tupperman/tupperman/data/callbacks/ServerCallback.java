package ch.tupperman.tupperman.data.callbacks;

import org.json.JSONArray;
import org.json.JSONObject;

public interface ServerCallback {
    void onSuccess(JSONObject jsonObject);
    void onSuccessArray(JSONArray jsonObject);
    void onError(String message);
}
