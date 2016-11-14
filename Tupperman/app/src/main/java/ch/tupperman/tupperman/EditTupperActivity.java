package ch.tupperman.tupperman;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;

import ch.tupperman.tupperman.data.ServerCall;
import ch.tupperman.tupperman.data.callbacks.CreateOrUpdateTupperCallback;
import ch.tupperman.tupperman.data.callbacks.DeleteTupperCallback;
import ch.tupperman.tupperman.models.Tupper;
import layout.DetailFragment;

public class EditTupperActivity extends AppCompatActivity implements DetailFragment.OnFragmentInteractionListener {
    private Tupper tupper;
    private String mAuthToken;
    private FragmentManager mFragmentManager;
    private ServerCall mServerCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tupper);

        mFragmentManager = getSupportFragmentManager();

        mServerCall = new ServerCall(this, "http://ark-5.citrin.ch:9080/api/");
        updateAuthenticationToken();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        tupper = (Tupper) intent.getSerializableExtra(getString(R.string.extra_tupper_uuid));

        Configuration.Builder config = new Configuration.Builder(this);
        config.addModelClasses(Tupper.class);
        ActiveAndroid.initialize(config.create());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getDetailFragment().setTupper(tupper);
    }

    private void updateAuthenticationToken() {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_file_id), MODE_PRIVATE);
        mAuthToken = preferences.getString(getString(R.string.preferences_key_auth_token), null);
        mServerCall.setToken(mAuthToken);
    }

    private DetailFragment getDetailFragment() {
        return (DetailFragment) mFragmentManager.findFragmentById(R.id.fragment_edit_tupper);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                getDetailFragment().saveTupper();
                return true;
            case R.id.action_delete:
                getDetailFragment().deleteTupper();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(final Tupper tupper) {
        mServerCall.postTupper(new CreateOrUpdateTupperCallback() {
            @Override
            public void onSuccess() {
                tupper.save();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(EditTupperActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }, tupper);
    }

    @Override
    public void onUpdate(final Tupper tupper) {
        mServerCall.postTupper(new CreateOrUpdateTupperCallback() {
            @Override
            public void onSuccess() {
                tupper.save();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(EditTupperActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }, tupper);

    }

    @Override
    public void onDelete(final Tupper tupper) {
        mServerCall.deleteTupper(new DeleteTupperCallback() {
            @Override
            public void onSuccess() {
                tupper.delete();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(EditTupperActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }, tupper);
    }
}
