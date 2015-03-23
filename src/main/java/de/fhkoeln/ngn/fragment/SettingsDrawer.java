package de.fhkoeln.ngn.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gc.materialdesign.views.CheckBox;

import de.fhkoeln.ngn.R;

public class SettingsDrawer extends Fragment {
    private CheckBox wifiCheckBox;
    private CheckBox bluetoothCheckBox;
    private CheckBox gsmCheckBox;
    private CheckBox gpsCheckBox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_drawer, container, false);
        wifiCheckBox = (CheckBox) view.findViewById(R.id.switch_wifi);
        bluetoothCheckBox = (CheckBox) view.findViewById(R.id.switch_bluetooth);
        gsmCheckBox = (CheckBox) view.findViewById(R.id.switch_gsm);
        gpsCheckBox = (CheckBox) view.findViewById(R.id.switch_gps);
        return view;
    }

}
