package de.fhkoeln.ngn.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;

import de.fhkoeln.ngn.service.util.LocationScanListener;


public class LocationService extends Service {
    private LocationManager locationManager;
    private LocationScanListener locationScanListener;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Log.d("LocationService", "Created LocationService");
        locationScanListener = new LocationScanListener();
        if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationScanListener);
        }
        if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationScanListener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(locationScanListener);
        Log.d("LocationService", "Destroyed LocationService");
    }
}
