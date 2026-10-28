package net.okitsu.ysmepicfightcompat.fabric.shim;

import net.fabricmc.loader.api.FabricLoader;

/** Pengganti sebagian net.okitsu.ysmepicfightcompat.fabric.shim.ModList. */
public final class ModList {
    private static final ModList INSTANCE = new ModList();
    private ModList() {}
    public static ModList get() { return INSTANCE; }
    public boolean isLoaded(String modId) { return FabricLoader.getInstance().isModLoaded(modId); }
}
