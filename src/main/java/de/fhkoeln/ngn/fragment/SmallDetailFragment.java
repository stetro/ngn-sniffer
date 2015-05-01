package de.fhkoeln.ngn.fragment;


import android.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.fhkoeln.ngn.R;
import de.fhkoeln.ngn.data.Measurement;
import de.fhkoeln.ngn.service.CellularService;
import de.fhkoeln.ngn.service.event.CellularResultEvent;
import de.fhkoeln.ngn.service.event.LocationChangedEvent;
import de.fhkoeln.ngn.service.event.WifiScanResultEvent;
import de.fhkoeln.ngn.service.receiver.BluetoothResultsReceiver;
import de.fhkoeln.ngn.service.util.BluetoothUtil;
import de.fhkoeln.ngn.service.util.CellularUtil;
import de.greenrobot.event.EventBus;

public class SmallDetailFragment extends Fragment {
    private TextView signalStrengthTextView;
    private TextView wifiApsTextView;
    private TextView latTextView;
    private TextView lngTextView;
    private TextView bluetoothTextView;
    private TextView signalTypeTextView;
    private static Measurement measurement = new Measurement();

    public static Measurement getMeasurement() {
        return measurement;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.small_detail_fragment, container, false);
        signalStrengthTextView = (TextView) v.findViewById(R.id.small_detail_fragment_signal);
        signalTypeTextView =(TextView) v.findViewById(R.id.small_detail_fragment_signal_type);
        wifiApsTextView = (TextView) v.findViewById(R.id.small_detail_fragment_wifi);
        bluetoothTextView = (TextView) v.findViewById(R.id.small_detail_fragment_bluetooth);
        latTextView = (TextView) v.findViewById(R.id.small_detail_fragment_location_lat);
        lngTextView = (TextView) v.findViewById(R.id.small_detail_fragment_location_lng);
        return v;
    }

    public void onEvent(CellularResultEvent e) {
        signalStrengthTextView.setText(String.valueOf(CellularService.getTransformedSignalStrength()) + " dBm");
        measurement.setSignalDBm(CellularService.getSignalStrengthDbm());
        signalTypeTextView.setText(CellularService.getNetworkType());
        measurement.setType(CellularService.getNetworkType());
    }

    public void onEvent(WifiScanResultEvent e) {
        wifiApsTextView.setText(String.valueOf(e.getScanResults().size()) + " AP's");
        measurement.setWifiAPs(e.getScanResults().size());
    }

    public void onEvent(LocationChangedEvent e) {
        Location l = e.getLocation();
        latTextView.setText("Lat " + l.getLatitude());
        measurement.setLat(l.getLatitude());
        lngTextView.setText("Lng " + l.getLongitude());
        measurement.setLng(l.getLongitude());
    }

    public void onEvent(BluetoothResultsReceiver e) {
        bluetoothTextView.setText(BluetoothUtil.getDeviceCount(getActivity()));
        // TODO: Blauzahn?
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
