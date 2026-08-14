package com.chocoboy.create_henry.content.jei;

import com.chocoboy.create_henry.content.fans.processing.SandingType;
import com.chocoboy.create_henry.content.recipes.*;
import com.chocoboy.create_henry.registry.HenryBlocks;
import com.chocoboy.create_henry.registry.HenryRecipeTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.compat.jei.*;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.simibubi.create.foundation.utility.CreateLang;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.createmod.catnip.config.ConfigBase;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import com.chocoboy.create_henry.HenryCreate;
import com.chocoboy.create_henry.infrastructure.config.HenryConfigs;
import com.chocoboy.create_henry.infrastructure.config.HenryRecipesConfig;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.simibubi.create.compat.jei.CreateJEI.consumeTypedRecipes;

@JeiPlugin
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class HenryJEI implements IModPlugin {

    private static final ResourceLocation PLUGIN_ID = new ResourceLocation(HenryCreate.MOD_ID, "jei_plugin");

    @Override
    @Nonnull
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    private static final List<CreateRecipeCategory<?>> allCategories = new ArrayList<>();

    @SuppressWarnings("unchecked")
    private void loadCategories() {
        allCategories.clear();

        CreateRecipeCategory<?>
                sanding = builder(SandingRecipe.class)
                        .addTypedRecipes(HenryRecipeTypes.SANDING)
                        .addRecipeListConsumer(recipes -> consumeAllRecipes(recipe -> {
                            if (SandingType.isPolishProcessingRecipe(recipe)) {
                                SandingRecipe r = SandingType.toSandingRecipe((ProcessingRecipe<?, ?>) recipe);
                                if (r != null) recipes.add(r);
                            }
                        }))
                        .catalystStack(HenryFanProcessingCategory.getFan("fan_sanding"))
                        .doubleItemIcon(AllItems.PROPELLER.get(), Items.SAND)
                        .emptyBackground(178, 72)
                        .build("fan_sanding", info -> new HenryFanProcessingCategory.SimpleBlock<>(info, Blocks.SAND)),
                freezing = builder(FreezingRecipe.class)
                        .addTypedRecipes(HenryRecipeTypes.FREEZING)
                        .catalystStack(HenryFanProcessingCategory.getFan("fan_freezing"))
                        .doubleItemIcon(AllItems.PROPELLER.get(), Items.POWDER_SNOW_BUCKET)
                        .emptyBackground(178, 72)
                        .build("fan_freezing", info -> new HenryFanProcessingCategory.SimpleBlock<>(info, Blocks.POWDER_SNOW)),
                seething = builder(SeethingRecipe.class)
                        .addTypedRecipes(HenryRecipeTypes.SEETHING)
                        .catalystStack(HenryFanProcessingCategory.getFan("fan_seething"))
                        .doubleItemIcon(AllItems.PROPELLER.get(), AllItems.BLAZE_CAKE.get())
                        .emptyBackground(178, 72)
                        .build("fan_seething", FanSeethingCategory::new),
                withering = builder(WitheringRecipe.class)
                        .addTypedRecipes(HenryRecipeTypes.WITHERING)
                        .catalystStack(HenryFanProcessingCategory.getFan("fan_withering"))
                        .doubleItemIcon(AllItems.PROPELLER.get(), Items.WITHER_ROSE)
                        .emptyBackground(178, 72)
                        .build("fan_withering", info -> new HenryFanProcessingCategory.SimpleBlock<>(info, Blocks.WITHER_ROSE)),
                dragon_breathing = builder(DragonBreathingRecipe.class)
                        .addTypedRecipes(HenryRecipeTypes.DRAGON_BREATHING)
                        .catalystStack(HenryFanProcessingCategory.getFan("fan_dragon_breathing"))
                        .doubleItemIcon(AllItems.PROPELLER.get(), Items.DRAGON_HEAD)
                        .emptyBackground(178, 72)
                        .build("fan_dragon_breathing", info -> new HenryFanProcessingCategory.SimpleBlock<>(info, Blocks.DRAGON_HEAD)),
                hydraulic = builder(HydraulicRecipe.class)
                        .addTypedRecipes(HenryRecipeTypes.HYDRAULIC_COMPACTING)
                        .catalyst(() -> HenryBlocks.HYDRAULIC_PRESS)
                        .itemIcon(HenryBlocks.HYDRAULIC_PRESS.get())
                        .emptyBackground(185, 100)
                        .build("hydraulic_compacting", info -> (CreateRecipeCategory<HydraulicRecipe>) (Object)
                                new HydraulicCategory((CreateRecipeCategory.Info<BasinRecipe>) (Object) info));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        allCategories.forEach(c -> c.registerRecipes(registration));
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        loadCategories();
        registration.addRecipeCategories(allCategories.toArray(IRecipeCategory[]::new));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        allCategories.forEach(c -> c.registerCatalysts(registration));

        // Industrial fan also processes vanilla Create fan recipes
        for (String vanillaType : List.of("fan_washing", "fan_smoking", "fan_blasting", "fan_haunting")) {
            registration.getJeiHelpers()
                    .getRecipeType(new ResourceLocation("create", vanillaType))
                    .ifPresent(type -> registration.addRecipeCatalyst(
                            new ItemStack(HenryBlocks.INDUSTRIAL_FAN.get()), type));
        }

        // Encased fan also processes Henry fan recipes
        for (String henryType : List.of("fan_sanding", "fan_seething", "fan_freezing", "fan_withering", "fan_dragon_breathing")) {
            registration.getJeiHelpers()
                    .getRecipeType(new ResourceLocation(HenryCreate.MOD_ID, henryType))
                    .ifPresent(type -> registration.addRecipeCatalyst(
                            new ItemStack(AllBlocks.ENCASED_FAN.get()), type));
        }

        registration.getJeiHelpers()
                .getRecipeType(new ResourceLocation(HenryCreate.MOD_ID, "hydraulic_compacting"))
                .ifPresent(type -> registration.addRecipeCatalyst(
                        new ItemStack(HenryBlocks.HYDRAULIC_PRESS.get()), type));
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(new BlueprintTransferHandler(), RecipeTypes.CRAFTING);
    }

    private <T extends Recipe<?>> CategoryBuilder<T> builder(Class<? extends T> recipeClass) {
        return new CategoryBuilder<>(recipeClass);
    }

    private static class CategoryBuilder<T extends Recipe<?>> {
        private final Class<? extends T> recipeClass;
        private Predicate<HenryRecipesConfig> predicate = c -> true;

        private IDrawable background;
        private IDrawable icon;

        private final List<Consumer<List<T>>> recipeListConsumers = new ArrayList<>();
        private final List<Supplier<? extends ItemStack>> catalysts = new ArrayList<>();

        public CategoryBuilder(Class<? extends T> recipeClass) {
            this.recipeClass = recipeClass;
        }

        public CategoryBuilder<T> addRecipeListConsumer(Consumer<List<T>> consumer) {
            recipeListConsumers.add(consumer);
            return this;
        }

        public CategoryBuilder<T> addTypedRecipes(IRecipeTypeInfo recipeTypeEntry) {
            return addTypedRecipes(recipeTypeEntry::getType);
        }

        public CategoryBuilder<T> addTypedRecipes(Supplier<RecipeType<? extends T>> recipeType) {
            return addRecipeListConsumer(recipes -> CreateJEI.<T>consumeTypedRecipes(recipes::add, recipeType.get()));
        }

        public CategoryBuilder<T> catalystStack(Supplier<ItemStack> supplier) {
            catalysts.add(supplier);
            return this;
        }

        public CategoryBuilder<T> catalyst(Supplier<ItemLike> supplier) {
            return catalystStack(() -> new ItemStack(supplier.get().asItem()));
        }

        public CategoryBuilder<T> itemIcon(ItemLike item) {
            icon = new ItemIcon(() -> new ItemStack(item));
            return this;
        }

        public CategoryBuilder<T> doubleItemIcon(ItemLike item1, ItemLike item2) {
            icon = new DoubleItemIcon(() -> new ItemStack(item1), () -> new ItemStack(item2));
            return this;
        }

        public CategoryBuilder<T> emptyBackground(int width, int height) {
            background = new EmptyBackground(width, height);
            return this;
        }

        public CreateRecipeCategory<T> build(String name, CreateRecipeCategory.Factory<T> factory) {
            Supplier<List<T>> recipesSupplier;
            if (predicate.test(HenryConfigs.server().recipes)) {
                recipesSupplier = () -> {
                    List<T> recipes = new ArrayList<>();
                    for (Consumer<List<T>> consumer : recipeListConsumers)
                        consumer.accept(recipes);
                    return recipes;
                };
            } else {
                recipesSupplier = Collections::emptyList;
            }

            CreateRecipeCategory.Info<T> info = new CreateRecipeCategory.Info<>(
                    new mezz.jei.api.recipe.RecipeType<>(HenryCreate.asResource(name), recipeClass),
                    Component.translatable(HenryCreate.MOD_ID + ".recipe." + name),
                    background, icon, recipesSupplier, catalysts);
            CreateRecipeCategory<T> category = factory.create(info);
            allCategories.add(category);
            return category;
        }
    }

    public static void consumeAllRecipes(Consumer<Recipe<?>> consumer) {
        Objects.requireNonNull(Minecraft.getInstance().getConnection())
                .getRecipeManager()
                .getRecipes()
                .forEach(consumer);
    }
}
