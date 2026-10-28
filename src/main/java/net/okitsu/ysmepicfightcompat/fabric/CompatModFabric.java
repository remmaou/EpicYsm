package net.okitsu.ysmepicfightcompat.fabric;

import net.fabricmc.api.ModInitializer;
import net.okitsu.ysmepicfightcompat.CompatMod;
import net.okitsu.ysmepicfightcompat.config.ServerPreferences;
import net.okitsu.ysmepicfightcompat.fabric.shim.bus.EventBus;
import net.okitsu.ysmepicfightcompat.network.CompatNetwork;

/** Entrypoint sisi umum (server dan klien). */
public class CompatModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerPreferences.register();
        CompatNetwork.registerMessages();

        // Subscriber sisi server (di NeoForge didaftarkan otomatis oleh @EventBusSubscriber)
        EventBus.GAME.register(net.okitsu.ysmepicfightcompat.network.MaidPreferenceBroadcaster.class);
        EventBus.GAME.register(net.okitsu.ysmepicfightcompat.network.SelectionBroadcaster.class);
        EventBus.GAME.register(net.okitsu.ysmepicfightcompat.network.HeldItemPreferenceBroadcaster.class);
        EventBus.GAME.register(net.okitsu.ysmepicfightcompat.network.MovementAnimationPreferenceBroadcaster.class);
        EventBus.GAME.register(net.okitsu.ysmepicfightcompat.network.SubEntityPreferenceBroadcaster.class);

        FabricEventBridge.registerServer();
        CompatMod.LOG.info("YSM-EF Compat (Fabric): sisi umum dimuat");
    }
}
