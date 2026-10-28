package net.okitsu.ysmepicfightcompat.fabric.shim.event;

import net.okitsu.ysmepicfightcompat.fabric.shim.bus.Event;
import net.minecraft.server.packs.resources.PreparableReloadListener;

public class RegisterClientReloadListenersEvent extends Event {
    private final java.util.List<PreparableReloadListener> listeners = new java.util.ArrayList<>();
    public void registerReloadListener(PreparableReloadListener listener) { listeners.add(listener); }
    public java.util.List<PreparableReloadListener> getListeners() { return listeners; }
}
