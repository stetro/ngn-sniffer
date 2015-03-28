package de.fhkoeln.ngn.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

import de.fhkoeln.ngn.service.event.GSMScanEvent;
import de.fhkoeln.ngn.service.event.GSMScanStartedEvent;
import de.fhkoeln.ngn.service.util.GSMResultReceiver;
import de.greenrobot.event.EventBus;

/**
 * Created by Ivan on 27.03.2015.
 */
public class GSMService extends Service
{
    private TelephonyManager telephonyManager;
    private GSMResultReceiver receiver;

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
        receiver = new GSMResultReceiver(telephonyManager);
        registerReceiver(receiver, new IntentFilter(TelephonyManager.EXTRA_STATE_IDLE));
        EventBus.getDefault().register(this);

        Log.d("GSMService", "Created GSMService");
    }

    public void onEvent(GSMScanEvent e)
    {
        Log.d("GSMService", "Scanning GSM ...");
        EventBus.getDefault().post(new GSMScanStartedEvent());
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(receiver);
        EventBus.getDefault().unregister(this);
        Log.d("GSMService", "Destroyed GSMService");
    }
}
