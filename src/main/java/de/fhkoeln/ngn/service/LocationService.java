package de.fhkoeln.ngn.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

import de.fhkoeln.ngn.data.HeatMapDataProvider;
import de.fhkoeln.ngn.fragment.SmallDetailFragment;
import de.fhkoeln.ngn.service.event.LocationChangedEvent;
import de.greenrobot.event.EventBus;


public class LocationService extends Service implements LocationListener
{
    private LocationManager locationManager;
    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public void onCreate()
    {
        super.onCreate();
        EventBus.getDefault().register(this);
        Log.d("LocationService", "Start LocationService");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 50, this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 50, this);
    }

    public void onEvent(LocationChangedEvent e) {}

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        locationManager.removeUpdates(this);
        Log.d("LocationService", "Destroyed LocationService");
    }

    @Override
    public void onLocationChanged(Location location)
    {
        EventBus.getDefault().post(new LocationChangedEvent(location));
        HeatMapDataProvider.saveMeasurement(SmallDetailFragment.getMeasurement());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
