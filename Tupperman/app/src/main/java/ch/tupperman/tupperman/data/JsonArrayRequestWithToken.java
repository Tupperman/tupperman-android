package ch.tupperman.tupperman.data;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class JsonArrayRequestWithToken extends JsonArrayRequest {
    private String mToken;
    public JsonArrayRequestWithToken(int method, String url, JSONArray jsonRequest, String token, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
        mToken = token;
    }
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<>();
        params.put("Content-Type", "application/json");
        params.put("x-access-token", mToken);
        return params;
    }
}
