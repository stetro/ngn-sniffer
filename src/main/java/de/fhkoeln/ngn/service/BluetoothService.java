package de.fhkoeln.ngn.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import de.fhkoeln.ngn.R;
import de.fhkoeln.ngn.service.event.BluetoothScanEvent;
import de.fhkoeln.ngn.service.event.BluetoothScanStartedEvent;
import de.fhkoeln.ngn.service.event.BluetoothScanStopEvent;
import de.fhkoeln.ngn.service.receiver.BluetoothResultsReceiver;
import de.greenrobot.event.EventBus;

public class BluetoothService extends Service {

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothResultsReceiver receiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        SharedPreferences settings = getSharedPreferences("settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("bluetoothEnabled", bluetoothAdapter.isEnabled());
        editor.apply();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        receiver = new BluetoothResultsReceiver();
        registerReceiver(receiver, filter);
        EventBus.getDefault().register(this);
        if (!bluetoothAdapter.isEnabled()) {
            Toast.makeText(this, getString(R.string.activate_bluetooth), Toast.LENGTH_SHORT).show();
            bluetoothAdapter.enable();
        }
        Log.d("BluetoothService", "Created BluetoothService");
    }

    public void onEvent(BluetoothScanEvent e) {
        Log.d("BluetoothService", "Discovering Bluethooth Devices...");
        bluetoothAdapter.startDiscovery();
        EventBus.getDefault().post(new BluetoothScanStartedEvent());
    }

    public void onEvent(BluetoothScanStopEvent e) {
        bluetoothAdapter.cancelDiscovery();
        Log.d("BluetoothService", "Stop Bluethooth Discovering...");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        EventBus.getDefault().unregister(this);
        Log.d("BluetoothService", "Destroyed BluetoothService");

        if(getSharedPreferences("settings", 0).getBoolean("bluetoothEnabled", false))
        {
            bluetoothAdapter.disable();
            Toast.makeText(this, getString(R.string.deactivate_bluetooth), Toast.LENGTH_LONG).show();
        }
    }
}
