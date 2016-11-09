package ch.tupperman.tupperman;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import ch.tupperman.tupperman.data.ServerCall;
import ch.tupperman.tupperman.data.callbacks.LoginCallback;
import ch.tupperman.tupperman.models.User;
import ch.tupperman.tupperman.Constants.RequestCode;
import layout.LoginFragment;

public class AccountManagementActivity extends AppCompatActivity implements LoginCallback,
        LoginFragment.InteractionListener {

    private ServerCall mServerCall;
    private Fragment mActiveFragment;
    private User mUser;

    @Override
    public void loginSuccess(String authToken) {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_file_id), MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(getString(R.string.preferences_key_auth_token), authToken);
        editor.putString(getString(R.string.preferences_key_email), mUser.getEmail());
        editor.putString(getString(R.string.preferences_key_password), mUser.getPassword());
        editor.apply();

        finish();
    }

    @Override
    public void loginError(String message) {
        ((LoginFragment) mActiveFragment).enableUserInterface();
        Toast.makeText(this, getString(R.string.toast_login_failed), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mServerCall = new ServerCall(this, "http://ark-5.citrin.ch:9080/api/");

        setContentView(R.layout.activity_account_management);

        Intent intent = getIntent();
        RequestCode request = (RequestCode) intent.getSerializableExtra(getString(R.string.extra_account_management_request_type));

        switch (request) {
            case LOGIN:
                mActiveFragment = new LoginFragment();
            default:
                break;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activity_account_management, mActiveFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onClickLogin(User user) {
        mUser = user;
        ((LoginFragment) mActiveFragment).disableUserInterface();
        mServerCall.authenticate(this, mUser);
    }
}
