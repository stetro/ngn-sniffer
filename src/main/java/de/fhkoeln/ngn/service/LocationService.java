package de.fhkoeln.ngn.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;

import de.fhkoeln.ngn.service.event.LocationSearchStartedEvent;
import de.fhkoeln.ngn.service.util.LocationScanListener;
import de.greenrobot.event.EventBus;


public class LocationService extends Service {
    private static String source = LocationManager.GPS_PROVIDER;
    private LocationManager locationManager;

    public static String getSource() {
        return source;
    }

    public static void setSource(String source) {
        LocationService.source = source;
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
        locationManager.requestLocationUpdates(getSource(), 0, 0, new LocationScanListener());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        Log.d("LocationService", "Destroyed LocationService");
    }
}
