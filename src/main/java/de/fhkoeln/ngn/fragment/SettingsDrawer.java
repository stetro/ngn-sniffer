package de.fhkoeln.ngn.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gc.materialdesign.views.CheckBox;

import de.fhkoeln.ngn.R;
import de.fhkoeln.ngn.service.WifiService;

public class SettingsDrawer extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_drawer, container, false);
        setupWifiService(view);

        CheckBox bluetoothCheckBox = (CheckBox) view.findViewById(R.id.switch_bluetooth);
        CheckBox gsmCheckBox = (CheckBox) view.findViewById(R.id.switch_gsm);
        CheckBox gpsCheckBox = (CheckBox) view.findViewById(R.id.switch_gps);
        return view;
    }

    private void setupWifiService(View view) {
        CheckBox wifiCheckBox = (CheckBox) view.findViewById(R.id.switch_wifi);
        wifiCheckBox.setOncheckListener(new CheckBox.OnCheckListener() {
            @Override
            public void onCheck(boolean checked) {
                Activity context = getActivity();
                Intent intent = new Intent(context, WifiService.class);
                if (checked) {
                    context.startService(intent);
                } else {
                    context.stopService(intent);
                }
            }
        });
    }
}
