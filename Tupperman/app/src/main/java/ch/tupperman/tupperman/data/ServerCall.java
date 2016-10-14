package ch.tupperman.tupperman.data;


import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import ch.tupperman.tupperman.services.TupperService;



public class ServerCall {
    private String url = "http://192.168.1.8:9080"; //SET YOUR OWN IP AND RUN THE TUPPERMAN SERVER
    private RequestQueue mRequestQueue;

    public ServerCall(Context context) {
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(context);
        }

    }

    public void getTuppers(final ServerCallback callback) {
        String urlAllTuppers = url + "/api/tuppers";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, urlAllTuppers, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub

            }
        });
        mRequestQueue.add(jsObjRequest);
    }
}
