package com.chocoboy.create_henry.content.blocks.kinetics.furnace_engine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.chocoboy.create_henry.registry.HenryParticleTypes;
import com.simibubi.create.foundation.particle.ICustomParticleDataWithSprite;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.particle.ParticleEngine.SpriteParticleRegistration;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class SmokeJetParticleData implements ParticleOptions, ICustomParticleDataWithSprite<SmokeJetParticleData> {

    public static final MapCodec<SmokeJetParticleData> CODEC = RecordCodecBuilder.mapCodec(i -> i
            .group(Codec.FLOAT.fieldOf("speed")
                    .forGetter(p -> p.speed))
            .apply(i, SmokeJetParticleData::new));

    public static final StreamCodec<ByteBuf, SmokeJetParticleData> STREAM_CODEC = ByteBufCodecs.FLOAT.map(
            SmokeJetParticleData::new, p -> p.speed);

    float speed;

    public SmokeJetParticleData(float speed) {
        this.speed = speed;
    }

    public SmokeJetParticleData() {
        this(0);
    }

    @Override
    public ParticleType<?> getType() {
        return HenryParticleTypes.SMOKE_JET.get();
    }

    @Override
    public MapCodec<SmokeJetParticleData> getCodec(ParticleType<SmokeJetParticleData> type) {
        return CODEC;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public SpriteParticleRegistration<SmokeJetParticleData> getMetaFactory() {
        return SmokeJetParticle.Factory::new;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, SmokeJetParticleData> getStreamCodec() {
        return STREAM_CODEC;
    }
}
