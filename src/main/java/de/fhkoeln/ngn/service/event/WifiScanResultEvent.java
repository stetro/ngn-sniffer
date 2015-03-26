package de.fhkoeln.ngn.service.event;

import android.net.wifi.ScanResult;

import java.util.List;


public class WifiScanResultEvent {
    private List<ScanResult> scanResults;

    public WifiScanResultEvent(List<ScanResult> scanResults) {
        this.scanResults = scanResults;
    }

    public List<ScanResult> getScanResults() {
        return scanResults;
    }
}
