package ch.tupperman.tupperman.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


public class TupperService extends IntentService {
    private String url = "http://192.168.1.8:9080"; //SET YOUR OWN IP AND RUN THE TUPPERMAN SERVER
    private RequestQueue mRequestQueue;

    public TupperService() {
        super("TupperService");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(this);
        }
        getTuppers();
    }

    private void getTuppers() {
        String urlAllTuppers = url + "/api/tuppers";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, urlAllTuppers, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Intent localIntent = new Intent("ch.tupperman.tupperman.rest").putExtra("json", response.toString());
                LocalBroadcastManager.getInstance(TupperService.this).sendBroadcast(localIntent);

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
