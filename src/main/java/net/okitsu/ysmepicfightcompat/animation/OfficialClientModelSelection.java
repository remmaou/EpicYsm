package net.okitsu.ysmepicfightcompat.animation;

import com.elfmcys.yesstevemodel.capability.ModelInfoCapability;
import net.minecraft.world.entity.player.Player;

/** Reads the selected client model directly from Fox Model Loader's public capability API. */
public final class OfficialClientModelSelection {
    private OfficialClientModelSelection() {
    }

    /**
     * Kept as a no-op for the old mixin call site. The Fabric port no longer captures a
     * private YSM state object; ModelInfoCapability is the supported Fox Model Loader API.
     */
    public static void register(Player player, Object state) {
    }

    public static String modelId(Player player) {
        if (player == null) return null;
        try {
            return ModelInfoCapability.get(player)
                    .filter(info -> !info.isDisabled())
                    .map(ModelInfoCapability::getModelId)
                    .filter(modelId -> modelId != null && !modelId.isBlank())
                    .orElse(null);
        } catch (RuntimeException | LinkageError unavailable) {
            return null;
        }
    }

    public static void remove(Player player) {
    }

    public static void clear() {
    }
}
