package de.fhkoeln.ngn.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.CheckBox;

import de.fhkoeln.ngn.R;
import de.fhkoeln.ngn.service.BluetoothService;
import de.fhkoeln.ngn.service.LocationService;
import de.fhkoeln.ngn.service.WifiService;

public class SettingsDrawer extends Fragment {

    private ButtonFlat collectButton;
    private Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_drawer, container, false);
        collectButton = (ButtonFlat) view.findViewById(R.id.settings_scan_button);
        collectButton.setOnClickListener(collectButtonListener);
        context = getActivity();
        setupWifiService(view);
        return view;
    }

    private void setupWifiService(View view) {
        CheckBox wifiCheckBox = (CheckBox) view.findViewById(R.id.switch_wifi);
        wifiCheckBox.setOncheckListener(new CheckBox.OnCheckListener() {
            @Override
            public void onCheck(boolean checked) {
                //Activity context = getActivity();
                Intent intent = new Intent(context, WifiService.class);
                if (checked) {
                    context.startService(intent);
                } else {
                    context.stopService(intent);
                }
            }
        });
        CheckBox bluetoothCheckBox = (CheckBox) view.findViewById(R.id.switch_bluetooth);
        bluetoothCheckBox.setOncheckListener(new CheckBox.OnCheckListener() {
            @Override
            public void onCheck(boolean checked) {
                //Activity context = getActivity();
                Intent intent = new Intent(context, BluetoothService.class);
                if (checked) {
                    context.startService(intent);
                } else {
                    context.stopService(intent);
                }
            }
        });
    }

    View.OnClickListener collectButtonListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            if(v.isSelected())
            {
                context.stopService(new Intent(context, LocationService.class));
                v.setSelected(false);
                collectButton.setText(context.getString(R.string.start_collecting));
            }
            else
            {
                context.startService(new Intent(context, LocationService.class));
                v.setSelected(true);
                collectButton.setText(context.getString(R.string.stop_collecting));
            }
        }
    };
}
