package de.fhkoeln.ngn.service.event;

import android.net.wifi.ScanResult;

import java.util.List;


public class ScanWifiResultEvent {
    private List<ScanResult> scanResults;

    public ScanWifiResultEvent(List<ScanResult> scanResults) {
        this.scanResults = scanResults;
    }

    public List<ScanResult> getScanResults() {
        return scanResults;
    }
}
