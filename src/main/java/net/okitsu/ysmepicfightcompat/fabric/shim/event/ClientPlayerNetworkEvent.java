package net.okitsu.ysmepicfightcompat.fabric.shim.event;

import net.okitsu.ysmepicfightcompat.fabric.shim.bus.Event;

public abstract class ClientPlayerNetworkEvent extends Event {
    public static class LoggingOut extends ClientPlayerNetworkEvent {}
}
