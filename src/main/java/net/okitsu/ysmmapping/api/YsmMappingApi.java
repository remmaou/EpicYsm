package net.okitsu.ysmmapping.api;

import java.util.Map;
import java.util.Set;

/**
 * Pengganti YSM Mapping API. Daftar simbol di sini diisi satu per satu terhadap Fox Model Loader
 * (lihat PORT_STATUS.md). Simbol yang belum diisi menyebabkan fitur terkait dimatikan dengan aman
 * (pemanggil sudah menangkap exception ini).
 */
public final class YsmMappingApi {
    private static final Map<YsmSymbolKey<?>, Object> KNOWN = Map.of();

    private YsmMappingApi() {}

    public static MappingSnapshot resolve(String consumerId, Set<YsmSymbolKey<?>> required) {
        for (YsmSymbolKey<?> key : required) {
            if (!KNOWN.containsKey(key)) {
                throw new IllegalStateException("Simbol YSM belum dipetakan ke Fox Model Loader: " + key.id());
            }
        }
        return new MappingSnapshot(KNOWN);
    }
}
