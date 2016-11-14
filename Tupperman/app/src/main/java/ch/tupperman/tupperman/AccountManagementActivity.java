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

        setContentView(R.layout.activity_account_management);

        Intent intent = getIntent();
        RequestCode request = (RequestCode) intent.getSerializableExtra(getString(R.string.extra_account_management_request_type));

        switch (request) {
            case LOGIN:
                mActiveFragment = new LoginFragment();
                setTitle(getString(R.string.title_activity_login));
                break;
            case REGISTER:
                mActiveFragment = new RegisterFragment();
                setTitle(getString(R.string.title_activity_register));
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
        mServerCall = ServerCall.newInstance(this);
        mServerCall.authenticate(this, mUser);
    }

    @Override
    public void loginSuccess(String authToken) {
        storeUserInformation(authToken);
        finish();
    }

    @Override
    public void loginError(LoginCallback.Error error) {
        if (mActiveFragment instanceof  LoginFragment) {
            ((LoginFragment) mActiveFragment).enableUserInterface();
            switch (error) {
                case INVALID_CREDENTIALS:
                    Toast.makeText(this, getString(R.string.toast_login_failed), Toast.LENGTH_LONG).show();
                    break;
                case UNKNOWN:
                    Toast.makeText(this, getString(R.string.toast_unknown_error), Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    @Override
    public void onClickRegister(User user) {
        mUser = user;
        ((RegisterFragment) mActiveFragment).disableUserInterface();
        mServerCall = ServerCall.newInstance(this);
        mServerCall.register(this, mUser);
    }

    @Override
    public void registerSuccess() {
        mServerCall.authenticate(this, mUser);
    }

    @Override
    public void registerError(RegisterCallback.Error error) {
        if (mActiveFragment instanceof RegisterFragment) {
            ((RegisterFragment) mActiveFragment).enableUserInterface();
            switch (error) {
                case INVALID_EMAIL_ADDRESS:
                    Toast.makeText(this, getString(R.string.toast_invalid_email_address), Toast.LENGTH_LONG).show();
                    break;
                case INVALID_PASSWORD:
                    Toast.makeText(this, getString(R.string.toast_invalid_password), Toast.LENGTH_LONG).show();
                    break;
                case EMAIL_TAKEN:
                    Toast.makeText(this, getString(R.string.toast_email_taken), Toast.LENGTH_LONG).show();
                    break;
                case UNKNOWN:
                    Toast.makeText(this, getString(R.string.toast_unknown_error), Toast.LENGTH_LONG).show();
                    break;
            }
        }
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
