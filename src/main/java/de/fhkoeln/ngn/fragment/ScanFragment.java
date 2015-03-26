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
import de.fhkoeln.ngn.service.event.BluetoothScanEvent;
import de.fhkoeln.ngn.service.event.BluetoothScanResultEvent;
import de.fhkoeln.ngn.service.event.BluetoothScanStartedEvent;
import de.fhkoeln.ngn.service.event.BluetoothScanStopEvent;
import de.fhkoeln.ngn.service.event.LocationFoundEvent;
import de.fhkoeln.ngn.service.event.LocationSearchStartedEvent;
import de.fhkoeln.ngn.service.event.WifiScanEvent;
import de.fhkoeln.ngn.service.event.WifiScanResultEvent;
import de.fhkoeln.ngn.service.event.WifiScanStartedEvent;
import de.fhkoeln.ngn.service.util.BluetoothUtil;
import de.fhkoeln.ngn.service.util.LocationUtil;
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
    private TextView locationInfoResult;
    private ProgressBarCircularIndeterminate locationProgress;
    private LinearLayout locationLayout;

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
                EventBus.getDefault().post(new BluetoothScanStopEvent());
                bluetoothStopButton.setVisibility(View.GONE);
                bluetoothProgress.setVisibility(View.GONE);
            }
        });

        locationInfoResult = (TextView) view.findViewById(R.id.scan_fragment_location_info_result);
        locationProgress = (ProgressBarCircularIndeterminate) view.findViewById(R.id.scan_fragment_location_progress);
        locationLayout = (LinearLayout) view.findViewById(R.id.scan_fragment_location);

        return view;
    }

    @Override
    public void onClick(View v) {
        wifiLayout.setVisibility(View.GONE);
        bluetoothLayout.setVisibility(View.GONE);
        EventBus.getDefault().post(new WifiScanEvent());
        EventBus.getDefault().post(new BluetoothScanEvent());
        EventBus.getDefault().post(new LocationSearchStartedEvent());
    }

    public void onEvent(BluetoothScanResultEvent e) {
        bluetoothLayout.setVisibility(View.VISIBLE);
        bluetoothScanResult.setText(BluetoothUtil.aggregateBluetoothScanResult(getActivity()));
        bluetoothInfoResult.setText(BluetoothUtil.aggregateBluetoothInfoResult(getActivity()));
    }

    public void onEvent(BluetoothScanStartedEvent e) {
        bluetoothProgress.setVisibility(View.VISIBLE);
        bluetoothStopButton.setVisibility(View.VISIBLE);
        bluetoothLayout.setVisibility(View.VISIBLE);
        bluetoothInfoResult.setText(BluetoothUtil.aggregateBluetoothInfoResult(getActivity()));
    }

    public void onEvent(WifiScanResultEvent e) {
        wifiProgress.setVisibility(View.GONE);
        wifiLayout.setVisibility(View.VISIBLE);
        wifiScanResult.setText(WifiUtil.aggregateWifiScanResult(e, getActivity()));
        wifiInfoResult.setText(WifiUtil.aggregateWifiInfoResult(getActivity()));
    }

    public void onEvent(WifiScanStartedEvent e) {
        wifiProgress.setVisibility(View.VISIBLE);
    }

    public void onEvent(LocationFoundEvent e) {
        locationProgress.setVisibility(View.GONE);
        locationLayout.setVisibility(View.VISIBLE);
        locationInfoResult.setText(LocationUtil.aggregateLocationInfoResult(getActivity(), e));
    }

    public void onEvent(LocationSearchStartedEvent e) {
        locationProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
