package net.okitsu.ysmepicfightcompat.fabric.shim.event;

import net.okitsu.ysmepicfightcompat.fabric.shim.bus.Event;

public class FMLClientSetupEvent extends Event {
    /** Fabric tidak punya antrean setup paralel: pekerjaan langsung dijalankan. */
    public void enqueueWork(Runnable work) { work.run(); }
}
