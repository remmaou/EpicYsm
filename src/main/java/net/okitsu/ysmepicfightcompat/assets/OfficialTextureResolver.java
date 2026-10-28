package net.okitsu.ysmepicfightcompat.assets;

import com.elfmcys.yesstevemodel.client.ClientModelManager;
import com.elfmcys.yesstevemodel.client.model.ModelAssembly;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import net.okitsu.ysmepicfightcompat.CompatMod;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Obtains Fox Model Loader textures through its public model-context API. */
public final class OfficialTextureResolver {
    private record Lease(AbstractTexture texture, ResourceLocation location) {
    }

    private static final Map<String, Lease> ACTIVE_LEASES = new ConcurrentHashMap<>();

    private OfficialTextureResolver() {
    }

    public static ResourceLocation resolve(String modelId, String requestedTexture) {
        if (modelId == null || modelId.isBlank() || !RenderSystem.isOnRenderThreadOrInit()) {
            return null;
        }
        String cacheKey = modelId + '#' + (requestedTexture == null ? "" : requestedTexture);
        try {
            Lease existing = ACTIVE_LEASES.get(cacheKey);
            if (existing != null) {
                return existing.location();
            }
            ModelAssembly model = ClientModelManager.getModelContext(modelId).orElse(null);
            if (model == null || model.getAnimationBundle() == null) {
                return null;
            }
            Map<?, ?> available = model.getAnimationBundle().getTextures();
            AbstractTexture selected = selectFrom(available, requestedTexture);
            if (selected == null) {
                return null;
            }
            ResourceLocation location = ResourceLocation.fromNamespaceAndPath(
                    CompatMod.MOD_ID, "fox_model/" + stableKey(cacheKey));
            Minecraft.getInstance().getTextureManager().register(location, selected);
            ACTIVE_LEASES.put(cacheKey, new Lease(selected, location));
            return location;
        } catch (Throwable exception) {
            CompatMod.LOG.warn(
                    "YSM-EF Compat: official texture lookup failed for '{}'", modelId, exception);
            return null;
        }
    }

    static AbstractTexture selectFrom(Map<?, ?> available, String requestedTexture) {
        for (String candidate : candidates(requestedTexture)) {
            Object selected = available.get(candidate);
            if (selected instanceof AbstractTexture texture) {
                return texture;
            }
        }
        for (Object selected : available.values()) {
            if (selected instanceof AbstractTexture texture) {
                return texture;
            }
        }
        return null;
    }

    public static void clear() {
        ACTIVE_LEASES.clear();
    }

    public static void release(String modelId) {
        String prefix = modelId + '#';
        ACTIVE_LEASES.keySet().removeIf(key -> key.startsWith(prefix));
    }

    private static List<String> candidates(String requestedTexture) {
        if (requestedTexture == null || requestedTexture.isBlank()) {
            return List.of();
        }
        LinkedHashSet<String> names = new LinkedHashSet<>();
        names.add(requestedTexture);
        String normalized = requestedTexture.replace('\\', '/');
        String filename = normalized.substring(normalized.lastIndexOf('/') + 1);
        names.add(filename);
        int dot = filename.lastIndexOf('.');
        names.add(dot > 0 ? filename.substring(0, dot) : filename + ".png");
        return new ArrayList<>(names);
    }

    private static String stableKey(String value) {
        return UUID.nameUUIDFromBytes(value.getBytes(StandardCharsets.UTF_8))
                .toString().replace("-", "");
    }
}
