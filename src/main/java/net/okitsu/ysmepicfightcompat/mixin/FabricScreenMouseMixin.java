package net.okitsu.ysmepicfightcompat.mixin;

import net.minecraft.client.gui.screens.Screen;
import net.okitsu.ysmepicfightcompat.fabric.shim.bus.EventBus;
import net.okitsu.ysmepicfightcompat.fabric.shim.event.ScreenEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Bridges screen mouse handling to the post-click event used by the input policy. */
@Mixin(Screen.class)
public abstract class FabricScreenMouseMixin {
    @Inject(method = "mouseClicked(DDI)Z", at = @At("RETURN"), cancellable = true)
    private void ysmCompat$afterMouseClicked(double mouseX, double mouseY, int button,
                                             CallbackInfoReturnable<Boolean> result) {
        ScreenEvent.MouseButtonPressed.Post event =
                EventBus.GAME.post(new ScreenEvent.MouseButtonPressed.Post(
                        Boolean.TRUE.equals(result.getReturnValue())));
        if (event.getResult() == ScreenEvent.MouseButtonPressed.Post.Result.FORCE_HANDLED) {
            result.setReturnValue(true);
        } else if (event.getResult()
                == ScreenEvent.MouseButtonPressed.Post.Result.FORCE_UNHANDLED) {
            result.setReturnValue(false);
        }
    }
}
