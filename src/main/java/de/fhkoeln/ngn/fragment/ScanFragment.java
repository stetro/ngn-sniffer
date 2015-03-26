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
import de.fhkoeln.ngn.service.event.ScanWifiEvent;
import de.fhkoeln.ngn.service.event.ScanWifiResultEvent;
import de.fhkoeln.ngn.service.event.ScanWifiStartedEvent;
import de.fhkoeln.ngn.service.util.WifiUtil;
import de.greenrobot.event.EventBus;

public class ScanFragment extends Fragment implements View.OnClickListener {

    private TextView wifiScanResult;
    private TextView wifiInfoResult;
    private LinearLayout wifiLayout;
    private ProgressBarCircularIndeterminate wifiProgress;

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
        return view;
    }

    @Override
    public void onClick(View v) {
        wifiLayout.setVisibility(View.GONE);
        EventBus.getDefault().post(new ScanWifiEvent());
    }

    public void onEvent(ScanWifiResultEvent e) {
        wifiScanResult.setText(WifiUtil.aggregateWifiScanResult(e));
        wifiInfoResult.setText(WifiUtil.aggregateWifiInfoResult(getActivity()));
        wifiProgress.setVisibility(View.GONE);
        wifiLayout.setVisibility(View.VISIBLE);
    }

    public void onEvent(ScanWifiStartedEvent e) {
        wifiProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
