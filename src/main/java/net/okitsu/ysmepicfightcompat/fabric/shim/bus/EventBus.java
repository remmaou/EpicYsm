package net.okitsu.ysmepicfightcompat.fabric.shim.bus;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Consumer;

/** Event bus berbasis refleksi. Dispatch dilakukan lewat cache per jenis event supaya tidak mahal di jalur panas. */
public final class EventBus implements IEventBus {
    public static final EventBus GAME = new EventBus();
    public static final EventBus MOD = new EventBus();

    private record Listener(EventPriority priority, Class<?> type, Consumer<Object> fn) {}

    private final List<Listener> listeners = new ArrayList<>();
    private final Map<Class<?>, Listener[]> cache = new HashMap<>();

    @Override
    public <E> void addListener(Consumer<E> listener) {
        throw new UnsupportedOperationException("Gunakan register(Class) atau addListener(Class, Consumer)");
    }

    @SuppressWarnings("unchecked")
    public <E> void addListener(Class<E> type, Consumer<E> listener) {
        add(new Listener(EventPriority.NORMAL, type, (Consumer<Object>) (Consumer<?>) listener));
    }

    /** Daftarkan method static ber-anotasi @SubscribeEvent dari sebuah kelas. */
    @Override
    public void register(Object target) {
        Class<?> cls = target instanceof Class<?> c ? c : target.getClass();
        boolean statics = target instanceof Class<?>;
        for (Method m : cls.getDeclaredMethods()) {
            SubscribeEvent a = m.getAnnotation(SubscribeEvent.class);
            if (a == null || m.getParameterCount() != 1 || Modifier.isStatic(m.getModifiers()) != statics) continue;
            m.setAccessible(true);
            add(new Listener(a.priority(), m.getParameterTypes()[0], ev -> {
                try {
                    m.invoke(statics ? null : target, ev);
                } catch (java.lang.reflect.InvocationTargetException e) {
                    throw new RuntimeException("Listener gagal: " + m, e.getCause());
                } catch (ReflectiveOperationException e) {
                    throw new RuntimeException("Listener gagal: " + m, e);
                }
            }));
        }
    }

    private synchronized void add(Listener l) {
        listeners.add(l);
        listeners.sort(Comparator.comparingInt(x -> x.priority.ordinal()));
        cache.clear();
    }

    @Override
    public <E> E post(E event) {
        Listener[] snapshot;
        synchronized (this) {
            snapshot = cache.computeIfAbsent(event.getClass(), c -> listeners.stream().filter(l -> l.type.isAssignableFrom(c)).toArray(Listener[]::new));
        }
        for (Listener l : snapshot) {
            if (event instanceof Event e && e.isCanceled()) break;
            l.fn.accept(event);
        }
        return event;
    }
}
