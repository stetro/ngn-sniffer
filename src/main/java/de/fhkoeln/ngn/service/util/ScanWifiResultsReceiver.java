package de.fhkoeln.ngn.service.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;

import de.fhkoeln.ngn.service.event.ScanWifiResultEvent;
import de.greenrobot.event.EventBus;


public class ScanWifiResultsReceiver extends BroadcastReceiver {
    private WifiManager wifiManager;

    public ScanWifiResultsReceiver(WifiManager wifiManager) {
        this.wifiManager = wifiManager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        List<ScanResult> scanResults = wifiManager.getScanResults();
        EventBus.getDefault().post(new ScanWifiResultEvent(scanResults));
        Log.d("WifiScanResultsReceiver", "Received " + scanResults.size() + " results");
    }
}
