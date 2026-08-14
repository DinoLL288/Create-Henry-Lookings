package com.chocoboy.create_henry.registry;

import com.simibubi.create.foundation.particle.ICustomParticleData;
import com.chocoboy.create_henry.util.Lang;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import com.chocoboy.create_henry.HenryCreate;
import com.chocoboy.create_henry.content.blocks.kinetics.furnace_engine.SmokeJetParticleData;

import java.util.function.Supplier;

@SuppressWarnings({"all"})
public enum HenryParticleTypes {

    SMOKE_JET(SmokeJetParticleData::new);

    private final ParticleEntry<?> entry;

    <D extends ParticleOptions> HenryParticleTypes(Supplier<? extends ICustomParticleData<D>> typeFactory) {
        String name = Lang.asId(name());
        entry = new ParticleEntry<>(name, typeFactory);
    }

    public static void register(IEventBus modEventBus) {
        ParticleEntry.REGISTER.register(modEventBus);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerFactories(RegisterParticleProvidersEvent event) {
        for (HenryParticleTypes particle : values())
            particle.entry.registerFactory(event);
    }

    public ParticleType<?> get() {
        return entry.object.get();
    }

    public String parameter() {
        return entry.name;
    }

    private static class ParticleEntry<D extends ParticleOptions> {
        private static final DeferredRegister<ParticleType<?>> REGISTER = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPES, HenryCreate.MOD_ID);

        private final String name;
        private final Supplier<? extends ICustomParticleData<D>> typeFactory;
        private final DeferredHolder<ParticleType<?>, ParticleType<D>> object;

        public ParticleEntry(String name, Supplier<? extends ICustomParticleData<D>> typeFactory) {
            this.name = name;
            this.typeFactory = typeFactory;

            object = REGISTER.register(name, () -> this.typeFactory.get().createType());
        }

        @OnlyIn(Dist.CLIENT)
        public void registerFactory(RegisterParticleProvidersEvent event) {
            typeFactory.get()
                    .register(object.get(), event);
        }

    }

}