package de.fhkoeln.ngn.data;

import java.util.Arrays;

public class Measurement {
    private double lat = 0;
    private double lng = 0;
    private String type = "";
    private int signalDBm = 0;
    private int wifiAPs = 0;

    private PointLocation location;

    public Measurement(double lat, double lng, String type, int signalDBm, int wifiAPs) {
        this.lat = lat;
        this.lng = lng;
        this.type = type;
        this.signalDBm = signalDBm;
        this.wifiAPs = wifiAPs;
        this.location = new PointLocation("Point", Arrays.asList(lat, lng));
    }

    public Measurement() {

    }

    public double getLat() {
        if (location != null)
            return location.getLat();
        else
            return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        if (location != null)
            return location.getLng();
        else
            return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSignalDBm() {
        return signalDBm;
    }

    public void setSignalDBm(int signalDBm) {
        this.signalDBm = signalDBm;
    }

    public int getWifiAPs() {
        return wifiAPs;
    }

    public void setWifiAPs(int wifiAPs) {
        this.wifiAPs = wifiAPs;
    }

    public PointLocation getLocation() {
        return location;
    }
}