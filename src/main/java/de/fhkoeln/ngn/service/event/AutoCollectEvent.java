package de.fhkoeln.ngn.service.event;

/**
 * Created by Ivan on 30.05.2015.
 */
public class AutoCollectEvent
{
    private boolean autoCollect;

    public AutoCollectEvent(boolean autoCollect) {this.autoCollect = autoCollect; }

    public boolean isAutoCollect() { return autoCollect; }
}
