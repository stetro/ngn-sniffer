package de.fhkoeln.ngn.service.util;


import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import java.util.Collection;
import java.util.Set;

import de.fhkoeln.ngn.R;

public class BluetoothUtil {
    private static String listBluetoothDeviceList(Context context, Collection<BluetoothDevice> deviceList) {
        if (deviceList.size() == 0) {
            return context.getString(R.string.bluetooth_nothing_found);
        }
        String list = "";
        for (BluetoothDevice device : deviceList) {
            list += convertBluetoothDevice(device);
        }
        return list;
    }

    private static String convertBluetoothDevice(BluetoothDevice device) {
        String list;
        list = "Address: " + device.getAddress();
        list += ", Name: " + device.getName();
        list += ", Class: " + device.getBluetoothClass();
        list += ", State: " + device.getBondState();
        list += "\n\n";
        return list;
    }

    public static String aggregateBluetoothScanResult(Context context) {
        Collection<BluetoothDevice> deviceList = ScanBluetoothResultsReceiver.getDeviceList();
        return listBluetoothDeviceList(context, deviceList);
    }

    public static String aggregateBluetoothInfoResult(Context context) {
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        Set<BluetoothDevice> bondedDevices = bluetoothManager.getAdapter().getBondedDevices();
        return listBluetoothDeviceList(context, bondedDevices);
    }
}
