package net.okitsu.ysmepicfightcompat.fabric.shim.event;

import net.okitsu.ysmepicfightcompat.fabric.shim.bus.Event;

public class FMLLoadCompleteEvent extends Event {
    public void enqueueWork(Runnable work) { work.run(); }
}
