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
import ch.tupperman.tupperman.data.callbacks.RegisterCallback;
import ch.tupperman.tupperman.models.User;
import ch.tupperman.tupperman.Constants.RequestCode;
import layout.LoginFragment;
import layout.RegisterFragment;

public class AccountManagementActivity extends AppCompatActivity implements
        LoginCallback,
        LoginFragment.InteractionListener,
        RegisterCallback,
        RegisterFragment.InteractionListener{

    private ServerCall mServerCall;
    private Fragment mActiveFragment;
    private User mUser;

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
                setTitle("Sign In");
                break;
            case REGISTER:
                mActiveFragment = new RegisterFragment();
                setTitle("Register");
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

    @Override
    public void loginSuccess(String authToken) {
        storeUserInformation(authToken);
        finish();
    }

    @Override
    public void loginError(String message) {
        ((LoginFragment) mActiveFragment).enableUserInterface();
        Toast.makeText(this, getString(R.string.toast_login_failed), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClickRegister(User user) {
        mUser = user;
        ((RegisterFragment) mActiveFragment).disableUserInterface();
        mServerCall.register(this, mUser);
    }

    @Override
    public void registerSuccess(String token) {
        mServerCall.authenticate(this, mUser);
    }

    @Override
    public void registerError(String message) {
        ((RegisterFragment) mActiveFragment).enableUserInterface();
        Toast.makeText(this, getString(R.string.toast_registration_failed), Toast.LENGTH_LONG).show();
    }

    private void storeUserInformation(String token) {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_file_id), MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(getString(R.string.preferences_key_auth_token), token);
        editor.putString(getString(R.string.preferences_key_email), mUser.getEmail());
        editor.putString(getString(R.string.preferences_key_password), mUser.getPassword());
        editor.apply();
    }

}
