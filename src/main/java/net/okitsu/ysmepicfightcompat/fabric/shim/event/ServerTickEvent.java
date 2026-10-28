package net.okitsu.ysmepicfightcompat.fabric.shim.event;

import net.okitsu.ysmepicfightcompat.fabric.shim.bus.Event;
import net.minecraft.server.MinecraftServer;

public abstract class ServerTickEvent extends Event {
    private final MinecraftServer server;
    protected ServerTickEvent(MinecraftServer server) { this.server = server; }
    public MinecraftServer getServer() { return server; }
    public static class Post extends ServerTickEvent { public Post(MinecraftServer server) { super(server); } }
}
