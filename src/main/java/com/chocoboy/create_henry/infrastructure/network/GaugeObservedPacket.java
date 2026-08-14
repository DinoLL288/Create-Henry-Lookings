package com.chocoboy.create_henry.infrastructure.network;

import com.chocoboy.create_henry.HenryCreate;
import com.chocoboy.create_henry.content.blocks.kinetics.multimeter.MultiMeterBlockEntity;
import net.createmod.catnip.net.base.BasePacketPayload;
import net.createmod.catnip.net.base.ServerboundPacketPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Locale;

public class GaugeObservedPacket implements ServerboundPacketPayload {

    public static final CustomPacketPayload.Type<GaugeObservedPacket> TYPE =
            new CustomPacketPayload.Type<>(HenryCreate.asResource("gauge_observed"));
    public static final StreamCodec<io.netty.buffer.ByteBuf, GaugeObservedPacket> STREAM_CODEC =
            StreamCodec.composite(BlockPos.STREAM_CODEC, p -> p.pos, GaugeObservedPacket::new);

    private final BlockPos pos;

    public GaugeObservedPacket(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public CustomPacketPayload.Type<GaugeObservedPacket> type() {
        return TYPE;
    }

    @Override
    public BasePacketPayload.PacketTypeProvider getTypeProvider() {
        return () -> TYPE;
    }

    @Override
    public void handle(ServerPlayer player) {
        if (player.level() == null)
            return;
        BlockEntity be = player.level().getBlockEntity(pos);
        if (be instanceof MultiMeterBlockEntity mbe)
            mbe.onObserved();
    }
}
