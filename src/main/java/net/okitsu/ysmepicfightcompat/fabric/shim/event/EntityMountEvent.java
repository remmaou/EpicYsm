package net.okitsu.ysmepicfightcompat.fabric.shim.event;

import net.okitsu.ysmepicfightcompat.fabric.shim.bus.Event;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class EntityMountEvent extends Event {
    private final Entity mounting, beingMounted;
    private final Level level;
    private final boolean mount;
    public EntityMountEvent(Entity mounting, Entity beingMounted, Level level, boolean mount) { this.mounting = mounting; this.beingMounted = beingMounted; this.level = level; this.mount = mount; }
    public Entity getEntityMounting() { return mounting; }
    public Entity getEntityBeingMounted() { return beingMounted; }
    public Level getLevel() { return level; }
    public boolean isMounting() { return mount; }
    public boolean isDismounting() { return !mount; }
}
