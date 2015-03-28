package de.fhkoeln.ngn.service.event;

import android.telephony.CellInfo;

import java.util.List;

/**
 * Created by Ivan on 27.03.2015.
 */
public class GSMResultEvent
{
    private List<CellInfo> cellResults;

    public GSMResultEvent(List<CellInfo> scanResults) {
        this.cellResults = scanResults;
    }

    public List<CellInfo> getResults() {
        return cellResults;
    }
}
