package de.fhkoeln.ngn.service.util;

import android.content.Context;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;

import java.util.List;

import de.fhkoeln.ngn.service.event.GSMResultEvent;

/**
 * Created by Ivan on 27.03.2015.
 */
public class GSMUtil
{
    public static String getLTEInfo(GSMResultEvent e)
    {
        StringBuilder sb = new StringBuilder();
        if(e.getLTECells() != null)
        {
            for(CellInfo cellInfo : e.getLTECells())
            {
                sb.append(cellInfo.describeContents()).append("\n");
            }
        }
        else
        {
            sb.append("No LTE cells could be found!");
        }

        return sb.toString();
    }

    public static String getNeighboringCellInfo(GSMResultEvent e)
    {
        StringBuilder sb = new StringBuilder();
        List<NeighboringCellInfo> nci = e.getTelephonyManager().getNeighboringCellInfo();

        for(NeighboringCellInfo neighboringCellInfo : nci)
        {
            sb.append("Neighboring Cell Info: ").append(neighboringCellInfo.describeContents()).append("\n")
              .append("CID: ").append(neighboringCellInfo.getCid()).append("\n")
              .append("LAC: ").append(neighboringCellInfo.getLac()).append("\n")
              .append("Network Type: ").append(neighboringCellInfo.getNetworkType()).append("\n")
              .append("PSC: ").append(neighboringCellInfo.getPsc()).append("\n")
              .append("RSSI: ").append(neighboringCellInfo.getRssi()).append("\n\n");

        }
        return sb.toString();
    }

    public static String getAllInfo(GSMResultEvent e)
    {
        StringBuilder sb = new StringBuilder();
        TelephonyManager tm = e.getTelephonyManager();

        sb.append("Call State: ").append(tm.getCallState()).append("\n")
          .append("Data Activity: ").append(tm.getDataActivity()).append("\n")
          .append("Data State: ").append(tm.getDataState()).append("\n")
          .append("Device ID: ").append(tm.getDeviceId()).append("\n")
          .append("Device Software Version: ").append(tm.getDeviceSoftwareVersion()).append("\n")
          .append("Group ID Level 1: ").append(tm.getGroupIdLevel1()).append("\n")
          .append("Line 1 Number: ").append(tm.getLine1Number()).append("\n")
          .append("Network Country ISO: ").append(tm.getNetworkCountryIso()).append("\n")
          .append("Network Operator Name: ").append(tm.getNetworkOperatorName()).append("\n")
          .append("Network Operator: ").append(tm.getNetworkOperator()).append("\n")
          .append("Network Type: ").append(tm.getNetworkType()).append("\n")
          .append("Phone Type: ").append(tm.getPhoneType()).append("\n")
          .append("Sim Country ISO: ").append(tm.getSimCountryIso()).append("\n")
          .append("Sim Operator: ").append(tm.getSimOperator()).append("\n")
          .append("Sim Operator Name: ").append(tm.getSimOperatorName()).append("\n")
          .append("Sim Serial Number: ").append(tm.getSimSerialNumber()).append("\n")
          .append("Sim State: ").append(tm.getSimState()).append("\n")
          .append("Subscriber ID: ").append(tm.getSubscriberId()).append("\n")
          .append("Voice Mail Alpha Tag: ").append(tm.getVoiceMailAlphaTag()).append("\n")
          .append("Voice Mail number: ").append(tm.getVoiceMailNumber()).append("\n");

        if(Build.VERSION.SDK_INT >= 19)
        {
            sb.append("MMS UA Prof Url: ").append(tm.getMmsUAProfUrl()).append("\n")
              .append("MMS User Agent: ").append(tm.getMmsUserAgent()).append("\n");
        }

        return sb.toString();
    }
}
