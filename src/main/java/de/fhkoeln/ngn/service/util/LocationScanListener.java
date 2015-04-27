package de.fhkoeln.ngn.service.util;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import de.fhkoeln.ngn.service.event.LocationFoundEvent;
import de.greenrobot.event.EventBus;


public class LocationScanListener implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {
        //Log.d("LocationScanListener", "onLocationChanged");
        EventBus.getDefault().post(new LocationFoundEvent(location));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        //Log.d("LocationScanListener", "onStatusChanged");
    }

    @Override
    public void onProviderEnabled(String s) {
        //Log.d("LocationScanListener", "onProviderEnabled");
    }

    @Override
    public void onProviderDisabled(String s) {
        //Log.d("LocationScanListener", "onProviderDisabled");
    }
}
