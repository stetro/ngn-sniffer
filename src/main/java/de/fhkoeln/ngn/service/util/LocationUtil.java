package de.fhkoeln.ngn.service.util;


import android.content.Context;

import de.fhkoeln.ngn.service.event.LocationFoundEvent;

public class LocationUtil {
    public static String aggregateLocationInfoResult(Context context, LocationFoundEvent e) {
        String locationLatLong;
        locationLatLong = ""+e.getLocation().getLatitude() + " Latitude\n"+ e.getLocation().getLongitude()+" Longitude";
        return locationLatLong;
    }
}
