package net.okitsu.ysmepicfightcompat.fabric.shim.bus;

import java.util.function.Consumer;

public interface IEventBus {
    <E> void addListener(Consumer<E> listener);
    void register(Object target);
    <E> E post(E event);
}
