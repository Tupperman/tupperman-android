package ch.tupperman.tupperman.data;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import ch.tupperman.tupperman.data.callbacks.CreateTupperCallback;
import ch.tupperman.tupperman.data.callbacks.GetTuppersCallback;
import ch.tupperman.tupperman.data.callbacks.ServerCallback;
import ch.tupperman.tupperman.models.Tupper;
import ch.tupperman.tupperman.models.TupperFactory;


public class ServerCall {
    private static final String TAG = "ServerCall";
    private String mUrl = "http://ark-5.citrin.ch:9080/api/"; //SET YOUR OWN IP AND RUN THE TUPPERMAN SERVER
    private String mToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIyYTMwYTQzMS03NDM2LTQzMWItOWZhNi1iZTdlZTFhZmJmYjAiLCJpYXQiOjE0NzgxNjMzNjAsImV4cCI6MTQ3ODI0OTc2MH0.DtyytypXZOHFnKNHbLiPW0e01k2sGCNBMzbfr1AfprA";
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

    public void getTuppers(final GetTuppersCallback callback) {
        String urlAllTuppers = mUrl + "tuppers";
        JsonArrayRequestWithToken jsObjRequest = new JsonArrayRequestWithToken(Request.Method.GET, urlAllTuppers, null, mToken, new Response.Listener<JSONArray>() {
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


    public void createTupper(final CreateTupperCallback callback, final JSONObject tupper) {
        String urlAllTuppers = mUrl + "tuppers/";
        JsonObjectRequestWithToken jsObjRequest = new JsonObjectRequestWithToken(Request.Method.POST, urlAllTuppers, tupper, mToken, new Response.Listener<JSONObject>() {
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
}
