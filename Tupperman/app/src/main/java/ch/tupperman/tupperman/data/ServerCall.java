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

import java.util.List;

import ch.tupperman.tupperman.data.callbacks.CreateOrUpdateTupperCallback;
import ch.tupperman.tupperman.data.callbacks.DeleteTupperCallback;
import ch.tupperman.tupperman.data.callbacks.GetTuppersCallback;
import ch.tupperman.tupperman.data.callbacks.LoginCallback;
import ch.tupperman.tupperman.data.callbacks.RegisterCallback;
import ch.tupperman.tupperman.models.Tupper;
import ch.tupperman.tupperman.models.TupperFactory;
import ch.tupperman.tupperman.models.User;

/* Test account:
 *
 * email: tes3t@hsr.ch
 * password: Test12345678!
 */

public class ServerCall {
    private static final String TAG = "ServerCall";
    private static final String ENDPOINT_TUPPERS = "tuppers/";
    private static final String ENDPOINT_AUTHENTICATE = "users/authenticate/";
    private static final String ENDPOINT_REGISTER = "users/create";

    private String mUrl;
    private String mAuthenticationToken;
    private TupperFactory mTupperFactory;
    private RequestQueue mRequestQueue;

    public ServerCall(Context context, String endpoint) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }

        mUrl = endpoint;
        mTupperFactory = new TupperFactory();
    }

    public void setToken(String authenticationToken) {
        mAuthenticationToken = authenticationToken;
    }

    /**
     * Retrieve all tuppers from the Server
     *
     * @param callback The callback to notify on request completion
     */
    public void getTuppers(final GetTuppersCallback callback) {
        String url = mUrl + ENDPOINT_TUPPERS;
        JsonArrayRequestWithToken jsObjRequest = new JsonArrayRequestWithToken(Request.Method.GET, url, null, mAuthenticationToken, new Response.Listener<JSONArray>() {
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

    /**
     * Send a tupper to the server.
     *
     * Sending a tupper can be used to create a new as well as to update an existing tupper.
     *
     * @param callback The callback to notify on request completion
     * @param tupper The tupper to transmit to the server
     */
    public void postTupper(final CreateOrUpdateTupperCallback callback, Tupper tupper) {
        String url = mUrl + ENDPOINT_TUPPERS;
        JsonObjectRequestWithToken jsObjRequest = new JsonObjectRequestWithToken(Request.Method.POST, url, tupper.toJSON(), mAuthenticationToken, new Response.Listener<JSONObject>() {
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

    /**
     * Delete a tupper on the server
     *
     * @param callback The callback to notify on request completion
     * @param tupper The tupper to delete on the server
     */
    public void deleteTupper(final DeleteTupperCallback callback, Tupper tupper){
        String url = mUrl + ENDPOINT_TUPPERS + tupper.uuid;
        JsonObjectRequestWithToken jsObjRequest = new JsonObjectRequestWithToken(Request.Method.DELETE, url, null, mAuthenticationToken, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError("Something went wrong during the create tupper request!");
                Log.e(TAG, "deletTupper: " + error.toString());
            }
        });
        mRequestQueue.add(jsObjRequest);
    }

    /**
     * Authenticate a user with the server
     *
     * @param callback The callback to notify on request completion
     * @param user The user to authenticate
     */
    public void authenticate(final LoginCallback callback, User user) {
        final String urlLogin = mUrl + ENDPOINT_AUTHENTICATE;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlLogin, user.toJSON(), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                boolean success = response.optBoolean("success", false);
                if(success) {
                    callback.loginSuccess(response.optString("token"));
                } else {
                    callback.loginError(LoginCallback.Error.UNKNOWN);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String jsonData = new String(error.networkResponse.data);
                try {
                    JSONObject jsonObject = new JSONObject(jsonData);
                    String message = jsonObject.optString("message");

                    if (message != null && message.endsWith("password wrong.")) {
                        callback.loginError(LoginCallback.Error.INVALID_CREDENTIALS);
                    } else {
                        callback.loginError(LoginCallback.Error.UNKNOWN);
                    }
                } catch (JSONException e) {
                    callback.loginError(LoginCallback.Error.UNKNOWN);
                }
            }
        });
        mRequestQueue.add(request);
    }

    /**
     * Register a new user with the server
     *
     * @param callback The callback to notify on request completion
     * @param user The user to register
     */
    public void register(final RegisterCallback callback, User user) {
        final String urlRegister = mUrl + ENDPOINT_REGISTER;
        mRequestQueue.add(new JsonObjectRequest(Request.Method.POST, urlRegister, user.toJSON(), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                boolean success = response.optBoolean("success", false);
                if(success) {
                    callback.registerSuccess();
                } else {
                    callback.registerError(RegisterCallback.Error.EMAIL_TAKEN);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String jsonData = new String(error.networkResponse.data);
                try {
                    JSONObject jsonObject = new JSONObject(jsonData);
                    String message = jsonObject.optString("message");
                    if(message == null) {
                        callback.registerError(RegisterCallback.Error.UNKNOWN);
                    } else if (message.endsWith("email address")) {
                        callback.registerError(RegisterCallback.Error.INVALID_EMAIL_ADDRESS);
                    } else {
                        callback.registerError(RegisterCallback.Error.INVALID_PASSWORD);
                    }
                } catch (JSONException e) {
                    callback.registerError(RegisterCallback.Error.UNKNOWN);
                }
            }
        }));
    }
}
