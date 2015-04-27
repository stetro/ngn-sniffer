package de.fhkoeln.ngn.service.event;

import android.location.Location;
import android.util.Log;


public class LocationFoundEvent {
    private Location location;

    public LocationFoundEvent(Location location) {
        this.location = location;
        //Log.d("LocationFoundEvent", "LocationFoundEvent was fired.");
    }

    public Location getLocation() {
        return location;
    }
}
