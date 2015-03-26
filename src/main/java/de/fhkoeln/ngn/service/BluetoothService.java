package de.fhkoeln.ngn.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import de.fhkoeln.ngn.R;
import de.fhkoeln.ngn.service.event.ScanBluetoothEvent;
import de.fhkoeln.ngn.service.event.ScanBluetoothStartedEvent;
import de.fhkoeln.ngn.service.event.ScanBluetoothStopEvent;
import de.fhkoeln.ngn.service.util.ScanBluetoothResultsReceiver;
import de.greenrobot.event.EventBus;

public class BluetoothService extends Service {

    private BluetoothAdapter bluetoothAdapter;
    private ScanBluetoothResultsReceiver receiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        receiver = new ScanBluetoothResultsReceiver();
        registerReceiver(receiver, filter);
        EventBus.getDefault().register(this);
        if (!bluetoothAdapter.isEnabled()) {
            Toast.makeText(this, getString(R.string.activate_bluetooth), Toast.LENGTH_SHORT).show();
            bluetoothAdapter.enable();
        }
        Log.d("BluetoothService", "Created BluetoothService");
    }

    public void onEvent(ScanBluetoothEvent e) {
        Log.d("BluetoothService", "Discovering Bluethooth Devices...");
        bluetoothAdapter.startDiscovery();
        EventBus.getDefault().post(new ScanBluetoothStartedEvent());
    }

    public void onEvent(ScanBluetoothStopEvent e) {
        bluetoothAdapter.cancelDiscovery();
        Log.d("BluetoothService", "Stop Bluethooth Discovering...");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        EventBus.getDefault().unregister(this);

    }
}
