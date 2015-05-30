package de.fhkoeln.ngn.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import de.fhkoeln.ngn.R;
import de.fhkoeln.ngn.data.HeatMapDataProvider;
import de.fhkoeln.ngn.fragment.MapsFragment;
import de.fhkoeln.ngn.fragment.SmallDetailFragment;
import de.fhkoeln.ngn.service.BluetoothService;
import de.fhkoeln.ngn.service.CellularService;
import de.fhkoeln.ngn.service.LocationService;
import de.fhkoeln.ngn.service.WifiService;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.NoSubscriberEvent;

public class MainActivity extends BaseActivity {
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        setTitle(R.string.app_name);
        prepareSettingsDrawer();
        prepareContentFragment();
        startServices();
        EventBus.getDefault().register(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_save_measurement){
            HeatMapDataProvider.saveMeasurement(SmallDetailFragment.getMeasurement());
        }
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        stopServices();
    }

    public void onEvent(NoSubscriberEvent e) {
        Log.d("MainActivity", e.originalEvent.toString() + " is ignored");
    }

    private void prepareSettingsDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar actionBarToolbar = getActionBarToolbar();
        setSupportActionBar(actionBarToolbar);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, actionBarToolbar, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(drawerToggle);
    }

    private void prepareContentFragment() {
        MapsFragment mapsFragment = new MapsFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.main_fragment_container, mapsFragment);
        transaction.commit();
    }

    private void startServices() {
        startService(new Intent(this, WifiService.class));
        startService(new Intent(this, BluetoothService.class));
        startService(new Intent(this, CellularService.class));
        startService(new Intent(this, LocationService.class));
    }

    private void stopServices() {
        stopService(new Intent(this, WifiService.class));
        stopService(new Intent(this, BluetoothService.class));
        stopService(new Intent(this, CellularService.class));
        stopService(new Intent(this, LocationService.class));
    }
}


