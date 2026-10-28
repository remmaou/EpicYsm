package net.okitsu.ysmepicfightcompat.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.okitsu.ysmepicfightcompat.CompatMod;
import net.okitsu.ysmepicfightcompat.config.ClientPreferences;
import net.okitsu.ysmepicfightcompat.fabric.shim.bus.EventBus;
import net.okitsu.ysmepicfightcompat.fabric.shim.event.FMLClientSetupEvent;
import net.okitsu.ysmepicfightcompat.fabric.shim.event.FMLLoadCompleteEvent;
import net.okitsu.ysmepicfightcompat.fabric.shim.event.RegisterClientReloadListenersEvent;

/** Entrypoint klien. */
public class CompatModFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPreferences.register();
        net.okitsu.ysmepicfightcompat.fabric.shim.network.PayloadRegistrar.registerClientReceivers();

        // Subscriber sisi klien
        EventBus.GAME.register(net.okitsu.ysmepicfightcompat.event.ScreenInputEvents.class);
        EventBus.GAME.register(net.okitsu.ysmepicfightcompat.event.ClientMaintenanceEvents.class);
        EventBus.GAME.register(net.okitsu.ysmepicfightcompat.event.ClientLifecycleEvents.class);
        EventBus.GAME.register(net.okitsu.ysmepicfightcompat.event.CombatRenderInterceptor.class);

        FabricEventBridge.registerClient();

        // Setara FMLClientSetupEvent / RegisterClientReloadListenersEvent / FMLLoadCompleteEvent
        EventBus.GAME.post(new FMLClientSetupEvent());
        RegisterClientReloadListenersEvent reload = EventBus.GAME.post(new RegisterClientReloadListenersEvent());
        FabricEventBridge.installReloadListeners(reload.getListeners());
        EventBus.GAME.post(new FMLLoadCompleteEvent());
        CompatMod.LOG.info("YSM-EF Compat (Fabric): sisi klien dimuat");
    }
}
