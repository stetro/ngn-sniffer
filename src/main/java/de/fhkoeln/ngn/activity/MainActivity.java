package de.fhkoeln.ngn.activity;

import android.os.Bundle;

import de.fhkoeln.ngn.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        setTitle(R.string.app_name);

    }
}


