package de.fhkoeln.ngn.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

import de.fhkoeln.ngn.service.event.CellularResultEvent;
import de.fhkoeln.ngn.service.event.LocationChangedEvent;
import de.fhkoeln.ngn.tool.LimitedQueue;
import de.fhkoeln.ngn.service.util.CellularUtil;
import de.greenrobot.event.EventBus;


public class CellularService extends Service {

    private static LimitedQueue<Integer> signalStrengthQueue = new LimitedQueue<>(5);
    private static String networkType;
    private TelephonyManager telephonyManager;
    private MyPhoneStateListener phoneStateListener;

    public static int getTransformedSignalStrength() {
        int signalStrengthDbm = getSignalStrengthDbm();
        return (signalStrengthDbm <= 0) ? -113 : signalStrengthDbm * 2 - 113;
    }

    public static int getSignalStrengthDbm() {
        int signalStrengthDbm = 0;
        if (signalStrengthQueue.size() != 0) {
            for (int i : signalStrengthQueue) {
                signalStrengthDbm += i;
            }
            signalStrengthDbm = signalStrengthDbm / signalStrengthQueue.size();
        }
        return signalStrengthDbm;
    }

    public static String getNetworkType() {
        return networkType;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        phoneStateListener = new MyPhoneStateListener();
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

        Log.d("CellularService", "Created CellularService");
    }

    public void onEvent(LocationChangedEvent e) {
        networkType = CellularUtil.getNetworkType(telephonyManager);
        EventBus.getDefault().post(new CellularResultEvent(telephonyManager));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        EventBus.getDefault().unregister(this);
        Log.d("CellularService", "Destroyed CellularService");
    }

    private static class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            signalStrengthQueue.add(signalStrength.getGsmSignalStrength());

        }
    }
}
