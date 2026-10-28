package net.okitsu.ysmepicfightcompat.fabric;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.okitsu.ysmepicfightcompat.CompatMod;
import net.okitsu.ysmepicfightcompat.fabric.shim.ServerLifecycleHooks;
import net.okitsu.ysmepicfightcompat.fabric.shim.bus.EventBus;
import net.okitsu.ysmepicfightcompat.fabric.shim.event.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/// Menjembatani callback Fabric ke event bergaya NeoForge yang dipakai kode compat.
/// Belum ada padanan Fabric untuk: EntityMountEvent (perlu mixin Entity#startRiding/stopRiding),
/// LivingShieldBlockEvent (perlu mixin), ScreenEvent.MouseButtonPressed.Post, RenderPlayerEvent.Pre.
public final class FabricEventBridge {
    private FabricEventBridge() {
    }

    public static void registerServer() {
        ServerLifecycleEvents.SERVER_STARTING.register(ServerLifecycleHooks::setCurrentServer);
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            EventBus.GAME.post(new ServerStoppedEvent(server));
            ServerLifecycleHooks.setCurrentServer(null);
        });
        ServerTickEvents.END_SERVER_TICK.register(server -> EventBus.GAME.post(new ServerTickEvent.Post(server)));

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                EventBus.GAME.post(new PlayerEvent.PlayerLoggedInEvent(handler.getPlayer())));
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) ->
                EventBus.GAME.post(new PlayerEvent.PlayerLoggedOutEvent(handler.getPlayer())));
        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) ->
                EventBus.GAME.post(new PlayerEvent.Clone(newPlayer, oldPlayer, !alive)));
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) ->
                EventBus.GAME.post(new PlayerEvent.PlayerRespawnEvent(newPlayer, false)));
        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) ->
                EventBus.GAME.post(new PlayerEvent.PlayerChangedDimensionEvent(player)));

        EntityTrackingEvents.START_TRACKING.register((tracked, player) ->
                EventBus.GAME.post(new PlayerEvent.StartTracking(player, tracked)));
        EntityTrackingEvents.STOP_TRACKING.register((tracked, player) ->
                EventBus.GAME.post(new PlayerEvent.StopTracking(player, tracked)));

        ServerEntityEvents.ENTITY_LOAD.register((entity, level) -> EventBus.GAME.post(new EntityJoinLevelEvent(entity, level)));
        ServerEntityEvents.ENTITY_UNLOAD.register((entity, level) -> EventBus.GAME.post(new EntityLeaveLevelEvent(entity, level)));

        CommonLifecycleEvents.TAGS_LOADED.register((registries, client) -> EventBus.GAME.post(new TagsUpdatedEvent(
                client ? TagsUpdatedEvent.UpdateCause.CLIENT_PACKET_RECEIVED : TagsUpdatedEvent.UpdateCause.SERVER_DATA_LOAD)));
    }

    public static void registerClient() {
        ClientEntityEvents.ENTITY_LOAD.register((entity, level) -> EventBus.GAME.post(new EntityJoinLevelEvent(entity, level)));
        ClientEntityEvents.ENTITY_UNLOAD.register((entity, level) -> EventBus.GAME.post(new EntityLeaveLevelEvent(entity, level)));
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> EventBus.GAME.post(new ClientPlayerNetworkEvent.LoggingOut()));
        ClientTickEvents.START_CLIENT_TICK.register(client -> EventBus.GAME.post(new ClientTickEvent.Pre()));
        ClientTickEvents.END_CLIENT_TICK.register(client -> EventBus.GAME.post(new ClientTickEvent.Post()));
        WorldRenderEvents.START.register(context -> EventBus.GAME.post(new RenderFrameEvent.Pre()));
        ClientSendMessageEvents.COMMAND.register(command -> EventBus.GAME.post(new CommandEvent(command)));
    }

    public static void installReloadListeners(List<PreparableReloadListener> listeners) {
        int index = 0;
        for (PreparableReloadListener listener : listeners) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(CompatMod.MOD_ID, "reload_" + index++);
            ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new IdentifiableResourceReloadListener() {
                @Override
                public ResourceLocation getFabricId() {
                    return id;
                }

                @Override
                public CompletableFuture<Void> reload(PreparationBarrier barrier, ResourceManager manager, ProfilerFiller prepareProfiler,
                                                      ProfilerFiller applyProfiler, Executor backgroundExecutor, Executor gameExecutor) {
                    return listener.reload(barrier, manager, prepareProfiler, applyProfiler, backgroundExecutor, gameExecutor);
                }
            });
        }
    }
}
