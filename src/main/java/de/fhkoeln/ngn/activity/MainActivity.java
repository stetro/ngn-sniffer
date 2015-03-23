package de.fhkoeln.ngn.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;

import de.fhkoeln.ngn.R;
import de.fhkoeln.ngn.fragment.ScanFragment;
import de.fhkoeln.ngn.service.WifiService;

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
        startService(new Intent(this, WifiService.class));
    }

    private void prepareContentFragment() {
        ScanFragment scanFragment = new ScanFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.main_fragment_container, scanFragment);
        transaction.commit();
    }

    private void prepareSettingsDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar actionBarToolbar = getActionBarToolbar();
        setSupportActionBar(actionBarToolbar);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, actionBarToolbar, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
}


