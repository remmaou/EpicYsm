package net.okitsu.ysmepicfightcompat.fabric.shim.event;

import net.okitsu.ysmepicfightcompat.fabric.shim.bus.Event;
import net.minecraft.server.MinecraftServer;

public class ServerStoppedEvent extends Event {
    private final MinecraftServer server;
    public ServerStoppedEvent(MinecraftServer server) { this.server = server; }
    public MinecraftServer getServer() { return server; }
}
