package net.okitsu.ysmepicfightcompat.fabric.shim;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;
import java.util.function.Supplier;

/** Pengganti net.okitsu.ysmepicfightcompat.fabric.shim.FMLPaths. */
public enum FMLPaths {
    CONFIGDIR(() -> FabricLoader.getInstance().getConfigDir()),
    GAMEDIR(() -> FabricLoader.getInstance().getGameDir()),
    MODSDIR(() -> FabricLoader.getInstance().getGameDir().resolve("mods"));

    private final Supplier<Path> path;
    FMLPaths(Supplier<Path> path) { this.path = path; }
    public Path get() { return path.get(); }
}
