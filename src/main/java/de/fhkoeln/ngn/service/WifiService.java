package de.fhkoeln.ngn.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import de.fhkoeln.ngn.R;
import de.fhkoeln.ngn.service.event.ScanWifiEvent;
import de.fhkoeln.ngn.service.util.ScanWifiResultsReceiver;
import de.greenrobot.event.EventBus;

public class WifiService extends Service {

    private WifiManager wifiManager;
    private ScanWifiResultsReceiver receiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        receiver = new ScanWifiResultsReceiver(wifiManager);
        registerReceiver(receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        EventBus.getDefault().register(this);
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(this, getString(R.string.activate_wifi), Toast.LENGTH_SHORT).show();
            wifiManager.setWifiEnabled(true);
        }
        Log.d("WifiService", "Created WifiService");
    }

    public void onEvent(ScanWifiEvent e) {
        Log.d("WifiService", "Scanning Wifis ...");
        wifiManager.startScan();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        EventBus.getDefault().unregister(this);
        Log.d("WifiService", "Destroyed WifiService");
    }
}
