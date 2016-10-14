package ch.tupperman.tupperman.data;

import org.json.JSONObject;

public interface ServerCallback {
    void onSuccess(JSONObject result);
}
