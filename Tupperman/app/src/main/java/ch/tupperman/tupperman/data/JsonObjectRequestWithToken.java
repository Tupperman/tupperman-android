package ch.tupperman.tupperman.data;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JsonObjectRequestWithToken extends JsonObjectRequest {

    private String mToken;
    public JsonObjectRequestWithToken(int method, String url, JSONObject jsonRequest, String token, Response.Listener listener, Response.ErrorListener errorListener) {
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
