package ch.tupperman.tupperman;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import ch.tupperman.tupperman.data.ServerCall;
import ch.tupperman.tupperman.data.callbacks.LoginCallback;

public class AccountManagementActivity extends AppCompatActivity implements LoginCallback, View.OnClickListener {

    private EditText mEmailFiled;
    private EditText mPasswordField;
    private Button mLoginButton;

    @Override
    public void onSuccess(String authToken) {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_file_id), MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(getString(R.string.preferences_key_auth_token), authToken);
        editor.putString(getString(R.string.preferences_key_email), mEmailFiled.getText().toString());
        editor.putString(getString(R.string.preferences_key_password), mPasswordField.getText().toString());
        editor.apply();

        finish();
    }

    @Override
    public void onError(String message) {
        enableUserInterface();
        Toast.makeText(this, getString(R.string.toast_login_failed), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        JSONObject accountData = new JSONObject();
        disableUserInterface();
        try {
            accountData.put("email", mEmailFiled.getText().toString());
            accountData.put("password", mPasswordField.getText().toString());
            doLogin(accountData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void disableUserInterface() {
        mEmailFiled.setEnabled(false);
        mPasswordField.setEnabled(false);
        mLoginButton.setEnabled(false);
    }

    private void enableUserInterface() {
        mEmailFiled.setEnabled(true);
        mPasswordField.setEnabled(true);
        mLoginButton.setEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_management);

        mEmailFiled = (EditText) findViewById(R.id.editText_login_email);
        mPasswordField = (EditText) findViewById(R.id.editText_login_password);
        mLoginButton = (Button) findViewById(R.id.button_login);

        mLoginButton.setOnClickListener(this);
    }

    private void doLogin(JSONObject accountData) {
        ServerCall call = new ServerCall(AccountManagementActivity.this);
        call.authenticate(this, accountData);
    }
}
