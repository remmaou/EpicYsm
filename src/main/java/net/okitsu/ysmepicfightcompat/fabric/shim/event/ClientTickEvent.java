package net.okitsu.ysmepicfightcompat.fabric.shim.event;

import net.okitsu.ysmepicfightcompat.fabric.shim.bus.Event;

public abstract class ClientTickEvent extends Event {
    public static class Pre extends ClientTickEvent {}
    public static class Post extends ClientTickEvent {}
}
