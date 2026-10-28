package net.okitsu.ysmepicfightcompat.fabric.shim.event;

import net.okitsu.ysmepicfightcompat.fabric.shim.bus.Event;

public abstract class RenderFrameEvent extends Event {
    public static class Pre extends RenderFrameEvent {}
}
