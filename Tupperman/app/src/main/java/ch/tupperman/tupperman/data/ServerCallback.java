package ch.tupperman.tupperman.data;

import org.json.JSONObject;

public interface ServerCallback {
    void onSuccess(JSONObject jsonObject);
    void onError(String message);
}
