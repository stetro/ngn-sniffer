package de.fhkoeln.ngn.service.util;

import android.content.Context;
import android.telephony.CellInfo;

import de.fhkoeln.ngn.service.event.GSMResultEvent;

/**
 * Created by Ivan on 27.03.2015.
 */
public class GSMUtil
{
    public static String getGSMInfo(GSMResultEvent e)
    {
        StringBuilder sb = new StringBuilder();
        for(CellInfo cellInfo : e.getResults())
        {
            sb.append(cellInfo.describeContents()).append("\n");
        }
        return sb.toString();
    }
}
