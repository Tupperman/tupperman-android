package ch.tupperman.tupperman;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

import ch.tupperman.tupperman.data.DataSync;
import ch.tupperman.tupperman.data.ServerCall;
import ch.tupperman.tupperman.data.callbacks.GetTuppersCallback;
import ch.tupperman.tupperman.models.Tupper;
import ch.tupperman.tupperman.service.TupperReceiver;
import ch.tupperman.tupperman.service.TupperService;
import layout.NoUserFragment;
import layout.SettingsFragment;
import layout.TupperListFragment;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        TupperListFragment.OnListFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener,
        SearchView.OnQueryTextListener,
        NoUserFragment.InteractionListener {

    private Intent mServiceIntent;
    private TupperReceiver mTupperReceiver;
    private FragmentManager mFragmentManager;
    private String tupperFragmentName;
    private String mAuthToken;
    private ServerCall mServerCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tupperFragmentName = getString(R.string.tupperFragment_Tag);

        Configuration.Builder config = new Configuration.Builder(this);
        config.addModelClasses(Tupper.class);
        ActiveAndroid.initialize(config.create());

        setUpTupperService();

        //TODO make config option
        mServerCall = new ServerCall(this, "http://ark-5.citrin.ch:9080/api/");
        updateAuthenticationToken();

        setContentView(R.layout.activity_main);

        mFragmentManager = getSupportFragmentManager();
        loadListFragment();

        if (mAuthToken == null) {
            //TODO do this in single activity
            setNoUserFragment();
        } else {
            initialize();
        }
    }

    private void setNoUserFragment() {
        NoUserFragment fragment = new NoUserFragment();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_main, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void initialize() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTupperReceiver.getIsOnline()) {
                    startEditActivity(null);
                }
            }
        });
        fab.show();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setTuppers();
    }

    private void updateAuthenticationToken() {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_file_id), MODE_PRIVATE);
        mAuthToken = preferences.getString(getString(R.string.preferences_key_auth_token), null);
        mServerCall.setToken(mAuthToken);
    }

    private void startLoginActivity() {
        final Constants.RequestCode requestType = Constants.RequestCode.LOGIN;
        Intent loginIntent = new Intent(this, AccountManagementActivity.class);
        loginIntent.putExtra(getString(R.string.extra_account_management_request_type), requestType);
        startActivityForResult(loginIntent, requestType.getValue());
    }

    private void startRegistrationActivity() {
        final Constants.RequestCode requestType = Constants.RequestCode.REGISTER;
        Intent loginIntent = new Intent(this, AccountManagementActivity.class);
        loginIntent.putExtra(getString(R.string.extra_account_management_request_type), requestType);
        startActivityForResult(loginIntent, requestType.getValue());
    }

    private void startEditActivity(Tupper tupper) {
        Intent loginIntent = new Intent(this, EditTupperActivity.class);
        loginIntent.putExtra(getString(R.string.extra_tupper_uuid), tupper);
        startActivityForResult(loginIntent, 1);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_scanner) {
            //TODO shorten if no options have to be set
            IntentIntegrator intentIntegrator = new IntentIntegrator(this);
            intentIntegrator.initiateScan();
        } else if (id == R.id.nav_tupperlist) {
            loadListFragment();
        } else if (id == R.id.nav_settings) {
            //TODO change to loadSettingsFragment
            SettingsFragment fragment = SettingsFragment.newInstance();
            mFragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public void onListFragmentInteraction(Tupper item) {
        startEditActivity(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        getTupperFragment().myTupperRecyclerViewAdapter.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() == 0) {
            getTupperFragment().myTupperRecyclerViewAdapter.getFilter().filter("");
        }
        return false;
    }


    private void setTuppers() {
//        if (getTupperFragment() == null) {
//            loadListFragment();
//        } else {
        getTupperFragment().setTuppers(DataSync.getAllTuppers());
//        }

        mServerCall.getTuppers(new GetTuppersCallback() {

            @Override
            public void onSuccess(List<Tupper> tuppers) {
                getTupperFragment().setTuppers(tuppers);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setUpTupperService() {
        mServiceIntent = new Intent(MainActivity.this, TupperService.class);
        IntentFilter mStatusIntentFilter = new IntentFilter("ch.tupperman.tupperman.rest");
        PendingIntent pending_intent = PendingIntent.getService(this, 0, mServiceIntent, 0);
        AlarmManager alarm_mgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm_mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 10000, pending_intent);
        mTupperReceiver = new TupperReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(mTupperReceiver, mStatusIntentFilter);
    }

    private void loadListFragment() {
        if (mAuthToken == null) {
            setNoUserFragment();
        } else {
            TupperListFragment tupperListFragment = getTupperFragment();
            if (tupperListFragment == null) {
                tupperListFragment = TupperListFragment.newInstance(DataSync.getAllTuppers());
            }
            mFragmentManager.beginTransaction().replace(R.id.content_main, tupperListFragment, tupperFragmentName).commit();
            mFragmentManager.executePendingTransactions();
        }

    }

    private TupperListFragment getTupperFragment() {
        return (TupperListFragment) mFragmentManager.findFragmentByTag(tupperFragmentName);
    }

//    public void updateTupperList(Tupper tupper) {
//        if (!getTupperFragment().exists(tupper)) {
//            addTupper(tupper);
//        }
//        create(tupper);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                String uuid = result.getContents();
                List<Tupper> allTuppers = DataSync.getAllTuppers();
                Tupper match = null;
                for (Tupper t : allTuppers) {
                    if (t.uuid.equals(uuid)) {
                        match = t;
                    }
                }
                if (match != null) {
                    startEditActivity(match);
                } else {
                    Toast.makeText(this, "No Tupper found for this code.", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            updateAuthenticationToken();
            initialize();
        }
    }

    @Override
    public void onFragmentInteraction(Tupper tupper) {

    }

    @Override
    public void onClickLogin() {
        startLoginActivity();
    }

    @Override
    public void onClickRegister() {
        startRegistrationActivity();
    }
}

