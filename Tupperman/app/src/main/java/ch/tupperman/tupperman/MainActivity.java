package ch.tupperman.tupperman;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;

import org.json.JSONObject;

import java.util.List;

import ch.tupperman.tupperman.data.ServerCall;
import ch.tupperman.tupperman.data.ServerCallback;
import ch.tupperman.tupperman.models.Tupper;
import ch.tupperman.tupperman.models.TupperFactory;
import ch.tupperman.tupperman.models.FakeData;
import ch.tupperman.tupperman.service.TupperReceiver;
import ch.tupperman.tupperman.service.TupperService;
import layout.DetailFragment;
import layout.SettingsFragment;
import layout.TupperFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TupperFragment.OnListFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener, DetailFragment.OnFragmentInteractionListener, SearchView.OnQueryTextListener {

    private TupperFragment fragment = null;
    private List<Tupper> tupperList;
    private ServerCall serverCall;
    private Intent mServiceIntent;
    private TupperReceiver mTupperReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.Builder config = new Configuration.Builder(this);
        config.addModelClasses(Tupper.class);
        ActiveAndroid.initialize(config.create());

        setUpTupperService();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with add Tupper", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        serverCall = new ServerCall(MainActivity.this);
        setTuppers();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_scanner) {
            // Transaction to scanner, once the lib is found
        } else if (id == R.id.nav_tupperlist) {
            TupperFragment fragment = TupperFragment.newInstance(tupperList);
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_main, fragment).commit();
        } else if (id == R.id.nav_settings) {
            SettingsFragment fragment = SettingsFragment.newInstance();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_main, fragment).commit();
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
        if (mTupperReceiver.getIsOnline()) {
            DetailFragment detailFragment = new DetailFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("tupper", item);
            detailFragment.setArguments(bundle);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, detailFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        fragment.myTupperRecyclerViewAdapter.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() == 0) {
            fragment.myTupperRecyclerViewAdapter.getFilter().filter("");
        }
        return false;
    }


    private void setTuppers() {
        serverCall.getTuppers(new ServerCallback() {
            TupperFactory tupperFactory = new TupperFactory();

            @Override
            public void onSuccess(JSONObject jsonObject) {
                tupperList = tupperFactory.toTuppers(jsonObject);
                loadFragment();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                tupperList = tupperFactory.toTuppers(new FakeData().data);
                loadFragment();
            }

            private void loadFragment() {
                fragment = TupperFragment.newInstance(tupperList);
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.content_main, fragment).commit();
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
}

