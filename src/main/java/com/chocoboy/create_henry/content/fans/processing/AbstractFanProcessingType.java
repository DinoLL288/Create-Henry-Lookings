package com.chocoboy.create_henry.content.fans.processing;

import com.chocoboy.create_henry.content.recipes.HenryFanProcessingRecipe.Wrapper;
import com.chocoboy.create_henry.registry.HenryRecipeTypes;
import com.chocoboy.create_henry.registry.HenryTags;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.foundation.recipe.RecipeApplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractFanProcessingType implements FanProcessingType {

    private final Wrapper wrapper = new Wrapper();
    private final HenryRecipeTypes recipeType;

    @Nullable
    private List<RecipeType<?>> peerRecipeTypes;

    protected AbstractFanProcessingType(HenryRecipeTypes recipeType) {
        this.recipeType = recipeType;
    }

    @Override
    public boolean canProcess(ItemStack stack, Level level) {
        wrapper.setItem(0, stack);
        return recipeType.find(wrapper, level).isPresent() || findPeerRecipe(level).isPresent();
    }

    @Override
    @Nullable
    public List<ItemStack> process(ItemStack stack, Level level) {
        wrapper.setItem(0, stack);
        Optional<? extends Recipe<?>> recipe = recipeType.find(wrapper, level);
        if (recipe.isEmpty())
            recipe = findPeerRecipe(level);
        return recipe.map(r -> RecipeApplier.applyRecipeOn(level, stack, r, false)).orElse(null);
    }

    // Create runs only the highest-priority type valid at a catalyst, so when this type wins a
    // shared catalyst it also serves same-named recipes from other addons (e.g. garnished:freezing).
    @SuppressWarnings({"unchecked", "rawtypes"})
    private Optional<? extends ProcessingRecipe<?, ?>> findPeerRecipe(Level level) {
        for (RecipeType<?> type : peerRecipeTypes()) {
            Optional<?> found = level.getRecipeManager().getRecipeFor((RecipeType) type, wrapper, level);
            if (found.isPresent() && found.get() instanceof ProcessingRecipe<?, ?> recipe)
                return Optional.of(recipe);
        }
        return Optional.empty();
    }

    private List<RecipeType<?>> peerRecipeTypes() {
        if (peerRecipeTypes == null) {
            ResourceLocation ownId = recipeType.getId();
            List<RecipeType<?>> peers = new ArrayList<>();
            for (RecipeType<?> type : BuiltInRegistries.RECIPE_TYPE) {
                ResourceLocation key = BuiltInRegistries.RECIPE_TYPE.getKey(type);
                if (key != null && !key.equals(ownId) && key.getPath().equals(ownId.getPath()))
                    peers.add(type);
            }
            peerRecipeTypes = List.copyOf(peers);
        }
        return peerRecipeTypes;
    }

    protected boolean isValidAtTags(Level level, BlockPos pos,
            HenryTags.AllFluidTags fluidTag, HenryTags.AllBlockTags blockTag) {
        return fluidTag.matches(level.getFluidState(pos))
                || blockTag.matches(level.getBlockState(pos));
    }

    /**
     * Progressively converts {@code from} into a {@code toType} entity over 50 ticks.
     * Progress is stored in the entity's persistent data under {@code nbtKey}.
     * Every 10 ticks, {@code progressSound} is played at an increasing pitch.
     * On completion, {@code finalSound} is played and the entity is replaced.
     */
    protected static <F extends Mob, T extends Mob> void transformEntity(
            F from, EntityType<T> toType, String nbtKey,
            SoundEvent progressSound, SoundEvent finalSound,
            Level level) {

        int progress = from.getPersistentData().getInt(nbtKey);
        if (progress < 50) {
            if (progress % 10 == 0)
                level.playSound(null, from.blockPosition(), progressSound,
                        SoundSource.NEUTRAL, 1f, 1.5f * progress / 50f);
            from.getPersistentData().putInt(nbtKey, progress + 1);
            return;
        }

        level.playSound(null, from.blockPosition(), finalSound, SoundSource.NEUTRAL, 1.25f, 0.65f);
        T to = toType.create(level);
        if (to == null) return;
        CompoundTag tag = from.saveWithoutId(new CompoundTag());
        tag.remove("UUID");
        to.deserializeNBT(level.registryAccess(), tag);
        to.setPos(from.getPosition(0));
        level.addFreshEntity(to);
        from.discard();
    }
}
