package net.okitsu.ysmepicfightcompat.fabric.shim.event;

import net.okitsu.ysmepicfightcompat.fabric.shim.bus.Event;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class EntityJoinLevelEvent extends Event {
    private final Entity entity;
    private final Level level;
    public EntityJoinLevelEvent(Entity entity, Level level) { this.entity = entity; this.level = level; }
    public Entity getEntity() { return entity; }
    public Level getLevel() { return level; }
}
