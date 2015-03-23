package de.fhkoeln.ngn.fragment;


import android.app.Fragment;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFlat;

import de.fhkoeln.ngn.R;
import de.fhkoeln.ngn.service.event.ScanWifiEvent;
import de.fhkoeln.ngn.service.event.ScanWifiResultEvent;
import de.greenrobot.event.EventBus;

public class ScanFragment extends Fragment implements View.OnClickListener {

    private TextView resultText;

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
        resultText = (TextView) view.findViewById(R.id.scan_fragment_scan_result);
        return view;
    }

    @Override
    public void onClick(View v) {
        EventBus.getDefault().post(new ScanWifiEvent());
    }

    public void onEvent(ScanWifiResultEvent e) {
        String ssids = "Wifis:\n";
        for (ScanResult scanResult : e.getScanResults()) {
            ssids += scanResult.BSSID + " " + scanResult.level + " " + scanResult.frequency + " " + scanResult.SSID + "\n";
        }
        resultText.setText(ssids);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
