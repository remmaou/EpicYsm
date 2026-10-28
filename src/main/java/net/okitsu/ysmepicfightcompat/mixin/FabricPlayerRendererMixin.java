package net.okitsu.ysmepicfightcompat.mixin;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.okitsu.ysmepicfightcompat.fabric.shim.bus.EventBus;
import net.okitsu.ysmepicfightcompat.fabric.shim.event.RenderPlayerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Provides the pre-player-render hook that NeoForge supplied to the original mod. */
@Mixin(PlayerRenderer.class)
public abstract class FabricPlayerRendererMixin {
    @Inject(
            method = "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void ysmCompat$beforePlayerRender(AbstractClientPlayer player, float entityYaw,
                                               float partialTick, PoseStack poseStack,
                                               MultiBufferSource buffers, int packedLight,
                                               CallbackInfo callback) {
        RenderPlayerEvent.Pre event = EventBus.GAME.post(new RenderPlayerEvent.Pre(
                player, (PlayerRenderer) (Object) this, partialTick,
                poseStack, buffers, packedLight));
        if (event.isCanceled()) {
            callback.cancel();
        }
    }
}
