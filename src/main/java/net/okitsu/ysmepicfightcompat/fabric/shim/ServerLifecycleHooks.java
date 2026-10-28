package net.okitsu.ysmepicfightcompat.fabric.shim;

import net.minecraft.server.MinecraftServer;

/** Pengganti net.okitsu.ysmepicfightcompat.fabric.shim.ServerLifecycleHooks. Diisi oleh FabricEventBridge. */
public final class ServerLifecycleHooks {
    private static volatile MinecraftServer current;
    private ServerLifecycleHooks() {}
    public static MinecraftServer getCurrentServer() { return current; }
    public static void setCurrentServer(MinecraftServer server) { current = server; }
}
