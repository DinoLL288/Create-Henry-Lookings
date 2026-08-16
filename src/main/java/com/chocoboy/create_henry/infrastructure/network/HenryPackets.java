package com.chocoboy.create_henry.infrastructure.network;

import com.chocoboy.create_henry.HenryCreate;
import net.createmod.catnip.net.base.BasePacketPayload;
import net.createmod.catnip.net.base.CatnipPacketRegistry;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.Locale;

public enum HenryPackets implements BasePacketPayload.PacketTypeProvider {

    GAUGE_OBSERVED;

    private final CustomPacketPayload.Type<?> type;

    <T extends net.createmod.catnip.net.base.ServerboundPacketPayload> HenryPackets() {
        String id = name().toLowerCase(Locale.ROOT);
        this.type = new CustomPacketPayload.Type<>(HenryCreate.asResource(id));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends CustomPacketPayload> CustomPacketPayload.Type<T> getType() {
        return (CustomPacketPayload.Type<T>) type;
    }

    public static void registerPackets() {
        CatnipPacketRegistry registry = new CatnipPacketRegistry(HenryCreate.MOD_ID, 1);
        registry.registerPacket(new CatnipPacketRegistry.PacketType<>(
                GAUGE_OBSERVED.getType(), GaugeObservedPacket.class, GaugeObservedPacket.STREAM_CODEC));
        CatnipServices.NETWORK.registerPackets(registry);
    }
}
