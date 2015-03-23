package de.fhkoeln.ngn.activity;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import de.fhkoeln.ngn.R;

public class BaseActivity extends ActionBarActivity {
    private Toolbar mActionBarToolbar;

    protected Toolbar getActionBarToolbar() {
        if (mActionBarToolbar == null) {
            mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
            if (mActionBarToolbar != null) {
                setSupportActionBar(mActionBarToolbar);
            }
        }
        return mActionBarToolbar;
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getActionBarToolbar();
    }
}
