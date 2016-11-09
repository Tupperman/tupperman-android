package ch.tupperman.tupperman.data;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import ch.tupperman.tupperman.data.callbacks.CreateOrUpdateTupperCallback;
import ch.tupperman.tupperman.data.callbacks.DeleteTupperCallback;
import ch.tupperman.tupperman.data.callbacks.GetTuppersCallback;
import ch.tupperman.tupperman.data.callbacks.LoginCallback;
import ch.tupperman.tupperman.models.Tupper;
import ch.tupperman.tupperman.models.TupperFactory;


public class ServerCall {
    private static final String TAG = "ServerCall";
    private String mUrl = "http://ark-5.citrin.ch:9080/api/"; //SET YOUR OWN IP AND RUN THE TUPPERMAN SERVER
    private TupperFactory mTupperFactory;
    /*
    {
    "email": "tes3t@hsr.ch",
    "password": "Test12345678!"
    }
    */
    private RequestQueue mRequestQueue;

    public ServerCall(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }


        mTupperFactory = new TupperFactory();
    }

    public void getTuppers(final GetTuppersCallback callback, String token) {
        String url = mUrl + "tuppers/";
        JsonArrayRequestWithToken jsObjRequest = new JsonArrayRequestWithToken(Request.Method.GET, url, null, token, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<Tupper> tuppers = mTupperFactory.toTuppers(response);
                callback.onSuccess(tuppers);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError("Something went wrong during the get tuppers request!");
                Log.e(TAG, error.toString());
            }
        });
        mRequestQueue.add(jsObjRequest);
    }


    public void createOrUpdateTupper(final CreateOrUpdateTupperCallback callback, Tupper tupper, String token) {
        String url = mUrl + "tuppers/";
        JsonObjectRequestWithToken jsObjRequest = new JsonObjectRequestWithToken(Request.Method.POST, url, tupper.toJSON(), token, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError("Something went wrong during the create tupper request!");
                Log.e(TAG, "createTupper: " + error.toString());
            }
        });
        mRequestQueue.add(jsObjRequest);
    }

    public void deleteTupper(final DeleteTupperCallback callback, Tupper tupper, String token){
        String url = mUrl + "tuppers/" + tupper.uuid;
        JsonObjectRequestWithToken jsObjRequest = new JsonObjectRequestWithToken(Request.Method.DELETE, url, tupper.toJSON(), token, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError("Something went wrong during the create tupper request!");
                Log.e(TAG, "createTupper: " + error.toString());
            }
        });
        mRequestQueue.add(jsObjRequest);
    }

    public void authenticate(final LoginCallback callback, JSONObject accountData) {
        final String urlLogin = mUrl + "users/authenticate";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlLogin, accountData, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(response.optString("token"));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError("Authentication failed");
            }
        });
        mRequestQueue.add(request);
    }
}
