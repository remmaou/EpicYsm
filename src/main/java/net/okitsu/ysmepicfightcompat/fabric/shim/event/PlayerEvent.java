package net.okitsu.ysmepicfightcompat.fabric.shim.event;

import net.okitsu.ysmepicfightcompat.fabric.shim.bus.Event;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public abstract class PlayerEvent extends Event {
    private final Player player;
    protected PlayerEvent(Player player) { this.player = player; }
    public Player getEntity() { return player; }

    public static class PlayerLoggedInEvent extends PlayerEvent { public PlayerLoggedInEvent(Player p) { super(p); } }
    public static class PlayerLoggedOutEvent extends PlayerEvent { public PlayerLoggedOutEvent(Player p) { super(p); } }
    public static class PlayerChangedDimensionEvent extends PlayerEvent { public PlayerChangedDimensionEvent(Player p) { super(p); } }
    public static class PlayerRespawnEvent extends PlayerEvent {
        private final boolean endConquered;
        public PlayerRespawnEvent(Player p, boolean endConquered) { super(p); this.endConquered = endConquered; }
        public boolean isEndConquered() { return endConquered; }
    }
    public static class Clone extends PlayerEvent {
        private final Player original;
        private final boolean wasDeath;
        public Clone(Player player, Player original, boolean wasDeath) { super(player); this.original = original; this.wasDeath = wasDeath; }
        public Player getOriginal() { return original; }
        public boolean isWasDeath() { return wasDeath; }
    }
    public static class StartTracking extends PlayerEvent {
        private final Entity target;
        public StartTracking(Player p, Entity target) { super(p); this.target = target; }
        public Entity getTarget() { return target; }
    }
    public static class StopTracking extends PlayerEvent {
        private final Entity target;
        public StopTracking(Player p, Entity target) { super(p); this.target = target; }
        public Entity getTarget() { return target; }
    }
}
