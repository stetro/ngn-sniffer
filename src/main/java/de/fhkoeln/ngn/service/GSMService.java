package de.fhkoeln.ngn.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.CellInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.List;

import de.fhkoeln.ngn.service.event.GSMResultEvent;
import de.fhkoeln.ngn.service.event.GSMScanEvent;
import de.greenrobot.event.EventBus;

/**
 * Created by Ivan on 27.03.2015.
 */
public class GSMService extends Service
{
    private TelephonyManager telephonyManager;

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        EventBus.getDefault().register(this);

        Log.d("GSMService", "Created GSMService");
    }

    public void onEvent(GSMScanEvent e)
    {
        Log.d("GSMService", "Scanning GSM ...");
        EventBus.getDefault().post(new GSMResultEvent(telephonyManager));
        Log.d("GSMService", "GSM Cell Information");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        Log.d("GSMService", "Destroyed GSMService");
    }
}
