package de.fhkoeln.ngn.service.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import de.fhkoeln.ngn.service.event.ScanWifiResultEvent;

public class WifiUtil {
    private static WifiInfo getCurrentWifiInfo(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            return wifiManager.getConnectionInfo();
        }
        return null;
    }

    public static String aggregateWifiInfoResult(Context context) {
        WifiInfo currentWifiInfo = WifiUtil.getCurrentWifiInfo(context);
        if (currentWifiInfo == null) {
            return "No Wifi Connection";
        } else {
            return String.valueOf(currentWifiInfo.toString());
        }
    }

    public static String aggregateWifiScanResult(ScanWifiResultEvent e) {
        String wifiScan = "";
        for (ScanResult scanResult : e.getScanResults()) {
            wifiScan += String.valueOf(scanResult) + "\n";
        }
        return wifiScan;
    }
}
