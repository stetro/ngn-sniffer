package de.fhkoeln.ngn.service.event;

import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;

import java.util.List;

/**
 * Created by Ivan on 27.03.2015.
 */
public class CellularResultEvent
{
    private TelephonyManager telephonyManager;

    public CellularResultEvent(TelephonyManager telephonyManager)
    {
        this.telephonyManager = telephonyManager;
    }

    public List<CellInfo> getLTECells()
    {
        return telephonyManager.getAllCellInfo();
    }

    public CellLocation getCellularCell()
    {
        return telephonyManager.getCellLocation();
    }

    public TelephonyManager getTelephonyManager()
    {
        return telephonyManager;
    }
}
