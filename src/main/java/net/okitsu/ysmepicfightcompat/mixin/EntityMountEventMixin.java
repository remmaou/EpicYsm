package net.okitsu.ysmepicfightcompat.mixin;

import net.minecraft.world.entity.Entity;
import net.okitsu.ysmepicfightcompat.fabric.shim.bus.EventBus;
import net.okitsu.ysmepicfightcompat.fabric.shim.event.EntityMountEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Emits the mount callback after vanilla has accepted a riding relationship. */
@Mixin(Entity.class)
public abstract class EntityMountEventMixin {
    @Inject(method = "startRiding(Lnet/minecraft/world/entity/Entity;Z)Z",
            at = @At("RETURN"))
    private void ysmCompat$publishMount(Entity vehicle, boolean force,
                                        CallbackInfoReturnable<Boolean> result) {
        if (Boolean.TRUE.equals(result.getReturnValue()) && vehicle != null) {
            Entity rider = (Entity) (Object) this;
            EventBus.GAME.post(new EntityMountEvent(rider, vehicle, rider.level(), true));
        }
    }
}
