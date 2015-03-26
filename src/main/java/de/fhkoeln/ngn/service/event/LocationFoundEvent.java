package de.fhkoeln.ngn.service.event;

import android.location.Location;


public class LocationFoundEvent {
    private Location location;

    public LocationFoundEvent(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
