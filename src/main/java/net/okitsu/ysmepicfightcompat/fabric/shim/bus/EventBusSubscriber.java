package net.okitsu.ysmepicfightcompat.fabric.shim.bus;

import net.okitsu.ysmepicfightcompat.fabric.shim.Dist;
import java.lang.annotation.*;

/** Tidak ada auto-scan di Fabric: kelas didaftarkan manual lewat EventBus#register(Class) di FabricEventBridge. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EventBusSubscriber {
    enum Bus { GAME, MOD }

    String modid() default "";
    Bus bus() default Bus.GAME;
    Dist[] value() default {Dist.CLIENT, Dist.DEDICATED_SERVER};
}
