package de.fhkoeln.ngn.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

import de.fhkoeln.ngn.R;
import de.fhkoeln.ngn.service.event.ScanBluetoothEvent;
import de.fhkoeln.ngn.service.event.ScanBluetoothResultEvent;
import de.fhkoeln.ngn.service.event.ScanBluetoothStartedEvent;
import de.fhkoeln.ngn.service.event.ScanBluetoothStopEvent;
import de.fhkoeln.ngn.service.event.ScanWifiEvent;
import de.fhkoeln.ngn.service.event.ScanWifiResultEvent;
import de.fhkoeln.ngn.service.event.ScanWifiStartedEvent;
import de.fhkoeln.ngn.service.util.BluetoothUtil;
import de.fhkoeln.ngn.service.util.WifiUtil;
import de.greenrobot.event.EventBus;

public class ScanFragment extends Fragment implements View.OnClickListener {

    private TextView wifiScanResult;
    private TextView wifiInfoResult;
    private LinearLayout wifiLayout;
    private ProgressBarCircularIndeterminate wifiProgress;

    private TextView bluetoothScanResult;
    private TextView bluetoothInfoResult;
    private LinearLayout bluetoothLayout;
    private ProgressBarCircularIndeterminate bluetoothProgress;
    private ButtonFlat bluetoothStopButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scan_fragment, container, false);

        ButtonFlat scanButton = (ButtonFlat) view.findViewById(R.id.scan_fragment_scan_button);
        scanButton.setOnClickListener(this);

        wifiScanResult = (TextView) view.findViewById(R.id.scan_fragment_wifi_scan_result);
        wifiInfoResult = (TextView) view.findViewById(R.id.scan_fragment_wifi_info_result);
        wifiProgress = (ProgressBarCircularIndeterminate) view.findViewById(R.id.scan_fragment_wifi_progress);
        wifiLayout = (LinearLayout) view.findViewById(R.id.scan_fragment_wifi);

        bluetoothScanResult = (TextView) view.findViewById(R.id.scan_fragment_bluetooth_scan_result);
        bluetoothInfoResult = (TextView) view.findViewById(R.id.scan_fragment_bluetooth_info_result);
        bluetoothProgress = (ProgressBarCircularIndeterminate) view.findViewById(R.id.scan_fragment_bluetooth_progress);
        bluetoothLayout = (LinearLayout) view.findViewById(R.id.scan_fragment_bluetooth);
        bluetoothStopButton = (ButtonFlat) view.findViewById(R.id.scan_fragment_bluetooth_stop_button);

        bluetoothStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new ScanBluetoothStopEvent());
                bluetoothStopButton.setVisibility(View.GONE);
                bluetoothProgress.setVisibility(View.GONE);
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        wifiLayout.setVisibility(View.GONE);
        bluetoothLayout.setVisibility(View.GONE);
        EventBus.getDefault().post(new ScanWifiEvent());
        EventBus.getDefault().post(new ScanBluetoothEvent());
    }

    public void onEvent(ScanBluetoothResultEvent e) {
        bluetoothLayout.setVisibility(View.VISIBLE);
        bluetoothScanResult.setText(BluetoothUtil.aggregateBluetoothScanResult(getActivity()));
        bluetoothInfoResult.setText(BluetoothUtil.aggregateBluetoothInfoResult(getActivity()));
    }

    public void onEvent(ScanWifiResultEvent e) {
        wifiProgress.setVisibility(View.GONE);
        wifiLayout.setVisibility(View.VISIBLE);
        wifiScanResult.setText(WifiUtil.aggregateWifiScanResult(e, getActivity()));
        wifiInfoResult.setText(WifiUtil.aggregateWifiInfoResult(getActivity()));
    }

    public void onEvent(ScanWifiStartedEvent e) {
        wifiProgress.setVisibility(View.VISIBLE);
    }

    public void onEvent(ScanBluetoothStartedEvent e) {
        bluetoothProgress.setVisibility(View.VISIBLE);
        bluetoothStopButton.setVisibility(View.VISIBLE);
        bluetoothLayout.setVisibility(View.VISIBLE);
        bluetoothInfoResult.setText(BluetoothUtil.aggregateBluetoothInfoResult(getActivity()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
