package com.chocoboy.create_henry.registry;

import com.chocoboy.create_henry.HenryCreate;
import com.google.gson.JsonObject;
import com.simibubi.create.AllSoundEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@SuppressWarnings("SameParameterValue")
public class HenrySoundEvents {
    public static final Map<ResourceLocation, AllSoundEvents.SoundEntry> ALL = new HashMap<>();

    public static final AllSoundEvents.SoundEntry FLUID_HATCH = create("fluid_hatch")
            .subtitle("Fluid Hatch opens")
            .playExisting(SoundEvents.IRON_DOOR_OPEN, .25f, .7f)
            .playExisting(SoundEvents.COPPER_PLACE, .75f, 1.15f)
            .category(SoundSource.BLOCKS)
            .build();

    private static HenrySoundEntryBuilder create(String name) {
        return create(HenryCreate.asResource(name));
    }

    public static HenrySoundEntryBuilder create(ResourceLocation id) {
        return new HenrySoundEntryBuilder(id);
    }

    public static void prepare() {
        for (var entry : ALL.values()) entry.prepare();
    }

    public static void register(RegisterEvent event) {
        event.register(Registries.SOUND_EVENT, helper -> ALL.values().forEach(entry -> entry.register(helper)));
    }

    public static void provideLang(BiConsumer<String, String> consumer) {
        for (var entry : ALL.values())
            if (entry.hasSubtitle()) consumer.accept(entry.getSubtitleKey(), entry.getSubtitle());
    }

    public static SoundEntryProvider provider(DataGenerator generator) {
        return new SoundEntryProvider(generator);
    }

    public static class SoundEntryProvider implements DataProvider {
        private final PackOutput output;

        public SoundEntryProvider(DataGenerator generator) {
            output = generator.getPackOutput();
        }

        @Override
        public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cache) {
            return generate(output.getOutputFolder(), cache);
        }

        @Override
        public @NotNull String getName() {
            return "Create Henry's Custom Sounds";
        }

        public CompletableFuture<?> generate(Path path, CachedOutput cache) {
            path = path.resolve("assets/create_henry");
            var json = new JsonObject();
            ALL.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> entry.getValue().write(json));
            return DataProvider.saveStable(cache, json, path.resolve("sounds.json"));
        }
    }

    public static class HenrySoundEntryBuilder extends AllSoundEvents.SoundEntryBuilder {
        public HenrySoundEntryBuilder(ResourceLocation id) {
            super(id);
        }

        @Override
        public HenrySoundEntryBuilder subtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        @Override
        public HenrySoundEntryBuilder attenuationDistance(int distance) {
            this.attenuationDistance = distance;
            return this;
        }

        @Override
        public HenrySoundEntryBuilder noSubtitle() {
            this.subtitle = null;
            return this;
        }

        @Override
        public HenrySoundEntryBuilder category(SoundSource category) {
            this.category = category;
            return this;
        }

        @Override
        public HenrySoundEntryBuilder addVariant(String name) {
            return addVariant(HenryCreate.asResource(name));
        }

        @Override
        public HenrySoundEntryBuilder addVariant(ResourceLocation id) {
            variants.add(id);
            return this;
        }

        @Override
        public HenrySoundEntryBuilder playExisting(Supplier<SoundEvent> event, float volume, float pitch) {
            wrappedEvents.add(new AllSoundEvents.ConfiguredSoundEvent(event, volume, pitch));
            return this;
        }

        @Override
        public HenrySoundEntryBuilder playExisting(SoundEvent event, float volume, float pitch) {
            return playExisting(() -> event, volume, pitch);
        }

        @Override
        public HenrySoundEntryBuilder playExisting(SoundEvent event) {
            return playExisting(event, 1, 1);
        }

        @Override
        public HenrySoundEntryBuilder playExisting(Holder<SoundEvent> event) {
            return playExisting(event::value, 1, 1);
        }

        @Override
        public AllSoundEvents.SoundEntry build() {
            var entry = super.build();
            ALL.put(entry.getId(), entry);
            AllSoundEvents.ALL.remove(entry.getId());
            return entry;
        }
    }
}
