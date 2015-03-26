package de.fhkoeln.ngn.service.util;


import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.fhkoeln.ngn.service.event.BluetoothScanResultEvent;
import de.greenrobot.event.EventBus;

public class BluetoothResultsReceiver extends BroadcastReceiver {

    private static Map<String, BluetoothDevice> deviceList = new HashMap<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        deviceList.put(device.getAddress(), device);
        EventBus.getDefault().post(new BluetoothScanResultEvent());
        Log.d("ScanBTResultsReceiver", "Found Device " + device.getName());
    }

    public static Collection<BluetoothDevice> getDeviceList() {
        return deviceList.values();
    }
}
