package de.fhkoeln.ngn.data;


public class Measurement {
    private double lat = 0;
    private double lng = 0;
    private String type = "";
    private int signalDBm = 0;
    private int wifiAPs = 0;

    public Measurement(double lat, double lng, String type, int signalDBm, int wifiAPs) {
        this.lat = lat;
        this.lng = lng;
        this.type = type;
        this.signalDBm = signalDBm;
        this.wifiAPs = wifiAPs;
    }

    public Measurement() {

    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
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
}