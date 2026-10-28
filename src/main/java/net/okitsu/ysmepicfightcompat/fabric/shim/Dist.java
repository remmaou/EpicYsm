package net.okitsu.ysmepicfightcompat.fabric.shim;

/** Pengganti net.okitsu.ysmepicfightcompat.fabric.shim.Dist. */
public enum Dist {
    CLIENT, DEDICATED_SERVER;

    public boolean isClient() { return this == CLIENT; }
    public boolean isDedicatedServer() { return this == DEDICATED_SERVER; }
}
