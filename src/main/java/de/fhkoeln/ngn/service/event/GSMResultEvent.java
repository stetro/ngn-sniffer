package de.fhkoeln.ngn.service.event;

import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;

import java.util.List;

/**
 * Created by Ivan on 27.03.2015.
 */
public class GSMResultEvent
{
    private TelephonyManager telephonyManager;

    public GSMResultEvent(TelephonyManager telephonyManager)
    {
        this.telephonyManager = telephonyManager;
    }

    public List<CellInfo> getLTECells()
    {
        return telephonyManager.getAllCellInfo();
    }

    public CellLocation getGSMCell()
    {
        return telephonyManager.getCellLocation();
    }

    public TelephonyManager getTelephonyManager()
    {
        return telephonyManager;
    }
}
