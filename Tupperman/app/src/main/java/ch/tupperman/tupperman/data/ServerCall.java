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
import org.json.JSONException;
import org.json.JSONObject;



public class ServerCall {
    private static final String TAG = "ServerCall";
    private String mUrl = "http://ark-5.citrin.ch:9080/api/"; //SET YOUR OWN IP AND RUN THE TUPPERMAN SERVER
    private String mToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIyYTMwYTQzMS03NDM2LTQzMWItOWZhNi1iZTdlZTFhZmJmYjAiLCJpYXQiOjE0NzgxNjMzNjAsImV4cCI6MTQ3ODI0OTc2MH0.DtyytypXZOHFnKNHbLiPW0e01k2sGCNBMzbfr1AfprA";
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
    }

    public void getTuppers(final ServerCallback callback) {
        String urlAllTuppers = mUrl + "tuppers";
        JsonArrayRequestWithToken jsObjRequest = new JsonArrayRequestWithToken(Request.Method.GET, urlAllTuppers, null, mToken, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                callback.onSuccessArray(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError("Something went wrong during the get tuppers request!");
                System.out.println("error Tuppers: " + error.toString());
                Log.e(TAG, error.toString());
            }
        });
        mRequestQueue.add(jsObjRequest);
    }


    public void createTupper(final ServerCallback callback, JSONObject tupper) {
        String urlAllTuppers = null;
        try {
            urlAllTuppers = mUrl + "tuppers/" + tupper.get("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, urlAllTuppers, tupper, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError("Something went wrong during the create tupper request!");
                System.out.println("error Tupper: " + error.toString());
                Log.e(TAG, error.toString());
            }
        });
        mRequestQueue.add(jsObjRequest);
    }
}
