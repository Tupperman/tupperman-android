package ch.tupperman.tupperman.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private final String mEmail;
    private final String mPassword;

    public User(String email, String password) {
        mEmail = email;
        mPassword = password;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getPassword() {
        return mPassword;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("email", mEmail);
            json.put("password", mPassword);
        } catch (JSONException e) {
        }

        return json;
    }
}
