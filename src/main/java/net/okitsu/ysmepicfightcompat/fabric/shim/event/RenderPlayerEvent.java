package net.okitsu.ysmepicfightcompat.fabric.shim.event;

import net.okitsu.ysmepicfightcompat.fabric.shim.bus.Event;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.player.Player;

public abstract class RenderPlayerEvent extends Event {
    private final Player player;
    private final PlayerRenderer renderer;
    private final float partialTick;
    private final PoseStack poseStack;
    private final MultiBufferSource buffers;
    private final int packedLight;
    protected RenderPlayerEvent(Player player, PlayerRenderer renderer, float partialTick, PoseStack poseStack, MultiBufferSource buffers, int packedLight) {
        this.player = player; this.renderer = renderer; this.partialTick = partialTick; this.poseStack = poseStack; this.buffers = buffers; this.packedLight = packedLight;
    }
    public Player getEntity() { return player; }
    public PlayerRenderer getRenderer() { return renderer; }
    public float getPartialTick() { return partialTick; }
    public PoseStack getPoseStack() { return poseStack; }
    public MultiBufferSource getMultiBufferSource() { return buffers; }
    public int getPackedLight() { return packedLight; }
    public static class Pre extends RenderPlayerEvent {
        public Pre(Player player, PlayerRenderer renderer, float partialTick, PoseStack poseStack, MultiBufferSource buffers, int packedLight) { super(player, renderer, partialTick, poseStack, buffers, packedLight); }
    }
}
