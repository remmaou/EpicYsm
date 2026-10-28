package net.okitsu.ysmepicfightcompat.network;

import com.elfmcys.yesstevemodel.capability.ModelInfoCapability;
import net.minecraft.world.entity.player.Player;

/** Membaca pilihan model pemain dari Fox Model Loader (mod id yes_steve_model) lewat ModelInfoCapability. */
public final class PlayerSelectionNbt {
    public record Selection(String modelId, String textureName) {
    }

    private PlayerSelectionNbt() {
    }

    public static Selection read(Player player) {
        if (player == null) {
            return null;
        }
        try {
            return ModelInfoCapability.get(player)
                    .filter(info -> !info.isDisabled())
                    .map(info -> {
                        String model = info.getModelId();
                        String texture = info.getSelectTexture();
                        return model == null || model.isEmpty() ? null
                                : new Selection(model, texture == null ? "" : texture);
                    })
                    .orElse(null);
        } catch (RuntimeException | LinkageError exception) {
            return null;
        }
    }
}
