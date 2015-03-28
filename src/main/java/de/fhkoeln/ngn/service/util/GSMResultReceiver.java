package de.fhkoeln.ngn.service.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.CellInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.List;

import de.fhkoeln.ngn.service.event.GSMResultEvent;
import de.greenrobot.event.EventBus;

/**
 * Created by Ivan on 27.03.2015.
 */
public class GSMResultReceiver extends BroadcastReceiver
{
    private TelephonyManager telephonyManager;

    public GSMResultReceiver(TelephonyManager telephonyManager)
    {
        this.telephonyManager = telephonyManager;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        List<CellInfo> cellInfos = telephonyManager.getAllCellInfo();
        EventBus.getDefault().post(new GSMResultEvent(cellInfos));
        Log.d("GSMResultsReceiver", "Received " + cellInfos.size() + " results");
    }
}
