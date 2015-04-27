package de.fhkoeln.ngn.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;

import de.fhkoeln.ngn.service.event.LocationFoundEvent;
import de.fhkoeln.ngn.service.event.LocationSearchStartedEvent;
import de.fhkoeln.ngn.service.util.LocationScanListener;
import de.greenrobot.event.EventBus;


public class LocationService extends Service {
    private static String sourceGPS = LocationManager.GPS_PROVIDER;
    private static String sourceNET = LocationManager.NETWORK_PROVIDER;
    private LocationManager locationManager;
    private LocationScanListener locationScanListener;

    public static String getSourceGPS() {
        return sourceGPS;
    }
    public static String getSourceNET() { return sourceNET; }

    public static void setSource(String source) {
        LocationService.sourceGPS = source;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        EventBus.getDefault().register(this);
        Log.d("LocationService", "Created LocationService");

    }

    public void onEvent(LocationSearchStartedEvent e) {
        //Log.d("LocationService", "LocationSearch started.");
        locationScanListener = new LocationScanListener();
        locationManager.requestLocationUpdates(getSourceGPS(), 0, 0, locationScanListener);
        locationManager.requestLocationUpdates(getSourceNET(), 0, 0, locationScanListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(locationScanListener);
        EventBus.getDefault().unregister(this);
        Log.d("LocationService", "Destroyed LocationService");
    }
}
