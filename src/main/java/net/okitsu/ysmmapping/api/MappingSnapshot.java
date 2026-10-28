package net.okitsu.ysmmapping.api;

import java.util.Map;

public final class MappingSnapshot {
    private final Map<YsmSymbolKey<?>, Object> symbols;
    MappingSnapshot(Map<YsmSymbolKey<?>, Object> symbols) { this.symbols = symbols; }

    @SuppressWarnings("unchecked")
    public <T> T require(YsmSymbolKey<T> key) {
        Object value = symbols.get(key);
        if (value == null) throw new IllegalStateException("Simbol YSM tidak tersedia di Fox Model Loader: " + key.id());
        return (T) value;
    }
}
