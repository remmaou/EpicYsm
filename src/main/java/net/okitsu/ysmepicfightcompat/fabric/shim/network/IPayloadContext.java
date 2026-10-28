package net.okitsu.ysmepicfightcompat.fabric.shim.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.world.entity.player.Player;

/** Pengganti net.neoforged.neoforge.network.handling.IPayloadContext. Handler Fabric sudah berjalan di thread game. */
public interface IPayloadContext {
    Player player();

    PacketFlow flow();

    /** Handler Fabric sudah dijalankan di thread utama, jadi pekerjaan langsung dikerjakan. */
    default void enqueueWork(Runnable work) {
        work.run();
    }

    /** Koneksi klien ke server (hanya untuk paket CLIENTBOUND); null untuk paket SERVERBOUND. */
    default Connection connection() {
        if (flow() == PacketFlow.CLIENTBOUND) {
            var listener = Minecraft.getInstance().getConnection();
            return listener == null ? null : listener.getConnection();
        }
        return null;
    }
}
