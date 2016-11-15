package ch.tupperman.tupperman.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import ch.tupperman.tupperman.R;


public class TupperService extends IntentService {
    private String url;
    private RequestQueue mRequestQueue;

    public TupperService() {
        super("TupperService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preferences_file_id), IntentService.MODE_PRIVATE);
        url = sharedPreferences.getString(getString(R.string.preferences_key_server_url), getString(R.string.preferences_default_url));
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(this);
        }
        isOnline();
    }

    private void isOnline() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                broadCast(true);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                broadCast(false);
            }


        });

        mRequestQueue.add(stringRequest);
    }

    private void broadCast(boolean isOnline) {
        Intent localIntent = new Intent("ch.tupperman.tupperman.rest").putExtra("isOnline", isOnline);
        LocalBroadcastManager.getInstance(TupperService.this).sendBroadcast(localIntent);
    }
}