package com.chocoboy.create_henry.content.fans.processing;

import com.chocoboy.create_henry.HenryCreate;
import com.chocoboy.create_henry.content.recipes.HenryFanProcessingRecipe.Wrapper;
import com.chocoboy.create_henry.content.recipes.SandingRecipe;
import com.chocoboy.create_henry.registry.HenryRecipeTypes;
import com.chocoboy.create_henry.registry.HenryTags;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.recipe.RecipeApplier;
import net.createmod.catnip.theme.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class SandingType extends AbstractFanProcessingType {

    private static final int COLOR_LIGHT = 0xEDEBCB;
    private static final int COLOR_DARK  = 0xE7E4BB;

    @Nullable
    private static List<SandingRecipe> polishRecipes = null;
    private final Wrapper polishWrapper = new Wrapper();

    public SandingType() {
        super(HenryRecipeTypes.SANDING);
    }

    @Override
    public boolean isValidAt(Level level, BlockPos pos) {
        return isValidAtTags(level, pos,
                HenryTags.AllFluidTags.FAN_PROCESSING_CATALYSTS_SANDING,
                HenryTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_SANDING);
    }

    @Override
    public int getPriority() {
        return 691000;
    }

    // region Polish compat

    public static boolean isPolishProcessingRecipe(Recipe<?> recipe) {
        if (!(recipe instanceof ProcessingRecipe<?, ?>)) return false;
        ResourceLocation serializerId = BuiltInRegistries.RECIPE_SERIALIZER.getKey(recipe.getSerializer());
        if (serializerId == null) return false;
        return serializerId.getPath().equals("sandpaper_polishing");
    }

    @Nullable
    public static SandingRecipe toSandingRecipe(ProcessingRecipe<?, ?> processing) {
        List<Ingredient> ingredients = processing.getIngredients();
        if (ingredients.size() != 1) return null;
        List<ProcessingOutput> outputs = processing.getRollableResults();
        if (outputs.isEmpty()) return null;

        ResourceLocation id = HenryCreate.asResource(
                "compat/" + processing.getId().getNamespace() + "/" + processing.getId().getPath());
        ProcessingRecipeBuilder<SandingRecipe> builder =
                new ProcessingRecipeBuilder<>(SandingRecipe::new, id);
        builder.require(ingredients.get(0));
        for (ProcessingOutput output : outputs)
            builder.output(output.getChance(), output.getStack());
        return builder.build();
    }

    public static void buildPolishCache(RecipeManager manager) {
        List<SandingRecipe> list = new ArrayList<>();
        for (Recipe<?> recipe : manager.getRecipes()) {
            if (!isPolishProcessingRecipe(recipe)) continue;
            SandingRecipe converted = toSandingRecipe((ProcessingRecipe<?, ?>) recipe);
            if (converted != null) list.add(converted);
        }
        polishRecipes = list;
    }

    @Nullable
    public static List<SandingRecipe> getPolishRecipes() {
        return polishRecipes;
    }

    // endregion

    @Override
    public boolean canProcess(ItemStack stack, Level level) {
        if (super.canProcess(stack, level)) return true;
        if (polishRecipes == null) return false;
        polishWrapper.setItem(0, stack);
        for (SandingRecipe r : polishRecipes)
            if (r.matches(polishWrapper, level)) return true;
        return false;
    }

    @Override
    @Nullable
    public List<ItemStack> process(ItemStack stack, Level level) {
        List<ItemStack> result = super.process(stack, level);
        if (result != null) return result;
        if (polishRecipes == null) return null;
        polishWrapper.setItem(0, stack);
        for (SandingRecipe r : polishRecipes)
            if (r.matches(polishWrapper, level))
                return RecipeApplier.applyRecipeOn(level, stack, r, false);
        return null;
    }

    @Override
    public void spawnProcessingParticles(Level level, Vec3 pos) {
        if (level.random.nextInt(8) != 0) return;
        Vector3f color1 = new Color(COLOR_LIGHT).asVectorF();
        Vector3f color2 = new Color(COLOR_DARK).asVectorF();
        level.addParticle(new DustParticleOptions(color1, 1),
                pos.x + (level.random.nextFloat() - .5f) * .5f, pos.y + .5f,
                pos.z + (level.random.nextFloat() - .5f) * .5f, 0, 1 / 8f, 0);
        level.addParticle(new DustParticleOptions(color2, 1),
                pos.x + (level.random.nextFloat() - .5f) * .5f, pos.y + .5f,
                pos.z + (level.random.nextFloat() - .5f) * .5f, 0, 1 / 8f, 0);
        level.addParticle(new DustParticleOptions(color1, 1),
                pos.x + (level.random.nextFloat() - .5f) * .5f, pos.y + .5f,
                pos.z + (level.random.nextFloat() - .5f) * .5f, 0, 1 / 8f, 0);
        level.addParticle(ParticleTypes.CRIT,
                pos.x + (level.random.nextFloat() - .5f) * .5f, pos.y + .5f,
                pos.z + (level.random.nextFloat() - .5f) * .5f, 0, 1 / 8f, 0);
    }

    @Override
    public void morphAirFlow(FanProcessingType.AirFlowParticleAccess particleAccess, RandomSource random) {
        particleAccess.setColor(Color.mixColors(COLOR_DARK, COLOR_LIGHT, random.nextFloat()));
        particleAccess.setAlpha(1f);
        if (random.nextFloat() < 1 / 128f)
            particleAccess.spawnExtraParticle(ParticleTypes.CRIT, .125f);
        if (random.nextFloat() < 1 / 32f)
            particleAccess.spawnExtraParticle(ParticleTypes.WHITE_ASH, .125f);
    }

    @Override
    public void affectEntity(Entity entity, Level level) {
        if (entity instanceof Zombie zombie && !(entity instanceof Husk))
            transformEntity(zombie, EntityType.HUSK,
                    "CreateSanding",
                    SoundEvents.HUSK_AMBIENT, SoundEvents.HUSK_CONVERTED_TO_ZOMBIE, level);
    }
}
