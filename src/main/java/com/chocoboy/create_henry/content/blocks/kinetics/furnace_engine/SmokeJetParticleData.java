package com.chocoboy.create_henry.content.blocks.kinetics.furnace_engine;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.foundation.particle.ICustomParticleData;
import com.simibubi.create.foundation.particle.SpriteParticleData;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.Locale;

public class SmokeJetParticleData extends SpriteParticleData<SmokeJetParticleData> {

    public static final Codec<SmokeJetParticleData> CODEC = RecordCodecBuilder.create(i -> i
            .group(Codec.FLOAT.fieldOf("speed")
                    .forGetter(p -> p.speed))
            .apply(i, SmokeJetParticleData::new));

    float speed;

    public SmokeJetParticleData(float speed) {
        super(speed);
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
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeFloat(speed);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %f", HenryParticleTypes.SMOKE_JET.parameter(), speed);
    }

    @Override
    public Codec<SmokeJetParticleData> getCodec(ParticleType<SmokeJetParticleData> type) {
        return CODEC;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ParticleEngine.SpriteParticleRegistration<SmokeJetParticleData> getMetaFactory() {
        return SmokeJetParticle.Factory::new;
    }
}