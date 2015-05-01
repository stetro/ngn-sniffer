package de.fhkoeln.ngn.service.event;

import android.location.Location;


public class LocationChangedEvent {
    private Location location;

    public LocationChangedEvent(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
