package net.okitsu.ysmepicfightcompat.fabric.shim.event;

import net.okitsu.ysmepicfightcompat.fabric.shim.bus.Event;
import net.minecraft.world.entity.LivingEntity;

public class LivingShieldBlockEvent extends Event {
    private final LivingEntity entity;
    private final boolean blocked;
    private final float blockedDamage;
    public LivingShieldBlockEvent(LivingEntity entity, boolean blocked, float blockedDamage) { this.entity = entity; this.blocked = blocked; this.blockedDamage = blockedDamage; }
    public LivingEntity getEntity() { return entity; }
    public boolean getBlocked() { return blocked; }
    public float getBlockedDamage() { return blockedDamage; }
}
