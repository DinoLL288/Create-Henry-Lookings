package com.chocoboy.create_henry.content.jei;

import com.chocoboy.create_henry.HenryCreate;
import com.chocoboy.create_henry.content.recipes.*;
import com.chocoboy.create_henry.registry.HenryBlocks;
import com.chocoboy.create_henry.registry.HenryRecipeTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.compat.jei.category.MixingCategory;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.fluids.potion.PotionMixingRecipes;
import com.simibubi.create.foundation.utility.RecipeGenericsUtil;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@JeiPlugin
@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
public class HenryJeiPlugin implements IModPlugin {

	private static final ResourceLocation ID = HenryCreate.asResource("jei_plugin");

	private final List<CreateRecipeCategory<?>> allCategories = new ArrayList<>();

	@Override
	public ResourceLocation getPluginUid() {
		return ID;
	}

	private void loadCategories() {
		allCategories.clear();

		CreateRecipeCategory<?>
			sanding = builder(SandingRecipe.class)
				.addTypedRecipes(HenryRecipeTypes.SANDING)
				.catalystStack(HenryFanProcessingCategory.getFan("fan_sanding"))
				.doubleItemIcon(HenryBlocks.INDUSTRIAL_FAN.get(), Blocks.SAND)
				.emptyBackground(178, 72)
				.build(HenryCreate.asResource("fan_sanding"), FanSandingCategory::new),

			freezing = builder(FreezingRecipe.class)
				.addTypedRecipes(HenryRecipeTypes.FREEZING)
				.catalystStack(HenryFanProcessingCategory.getFan("fan_freezing"))
				.doubleItemIcon(HenryBlocks.INDUSTRIAL_FAN.get(), Blocks.POWDER_SNOW)
				.emptyBackground(178, 72)
				.build(HenryCreate.asResource("fan_freezing"), FanFreezingCategory::new),

			seething = builder(SeethingRecipe.class)
				.addTypedRecipes(HenryRecipeTypes.SEETHING)
				.catalystStack(HenryFanProcessingCategory.getFan("fan_seething"))
				.doubleItemIcon(HenryBlocks.INDUSTRIAL_FAN.get(), AllBlocks.BLAZE_BURNER.get())
				.emptyBackground(178, 72)
				.build(HenryCreate.asResource("fan_seething"), FanSeethingCategory::new),

			withering = builder(WitheringRecipe.class)
				.addTypedRecipes(HenryRecipeTypes.WITHERING)
				.catalystStack(HenryFanProcessingCategory.getFan("fan_withering"))
				.doubleItemIcon(HenryBlocks.INDUSTRIAL_FAN.get(), Blocks.WITHER_ROSE)
				.emptyBackground(178, 72)
				.build(HenryCreate.asResource("fan_withering"), FanWitheringCategory::new),

			dragonBreathing = builder(DragonBreathingRecipe.class)
				.addTypedRecipes(HenryRecipeTypes.DRAGON_BREATHING)
				.catalystStack(HenryFanProcessingCategory.getFan("fan_dragon_breathing"))
				.doubleItemIcon(HenryBlocks.INDUSTRIAL_FAN.get(), Items.DRAGON_HEAD)
				.emptyBackground(178, 72)
				.build(HenryCreate.asResource("fan_dragon_breathing"), FanDragonBreathingCategory::new),

			hydraulic = builder(BasinRecipe.class)
				.addTypedRecipes(HenryRecipeTypes.HYDRAULIC_COMPACTING)
				.catalyst(HenryBlocks.HYDRAULIC_PRESS::get)
				.catalyst(AllBlocks.BASIN::get)
				.doubleItemIcon(HenryBlocks.HYDRAULIC_PRESS.get(), AllBlocks.BASIN.get())
				.emptyBackground(177, 103)
				.build(HenryCreate.asResource("hydraulic_compacting"), HydraulicCategory::new),

			goldenMixing = builder(BasinRecipe.class)
				.addTypedRecipes(AllRecipeTypes.MIXING)
				.catalyst(HenryBlocks.GOLDEN_MIXER::get)
				.catalyst(AllBlocks.BASIN::get)
				.doubleItemIcon(HenryBlocks.GOLDEN_MIXER.get(), AllBlocks.BASIN.get())
				.emptyBackground(177, 103)
				.build(HenryCreate.asResource("golden_mixing"), MixingCategory::standard),

			goldenAutoShapeless = builder(BasinRecipe.class)
				.addAllRecipesIf(r -> r.value() instanceof CraftingRecipe && !(r.value() instanceof ShapedRecipe) && r.value().getIngredients().size() > 1 && !AllRecipeTypes.shouldIgnoreInAutomation(r), BasinRecipe::convertShapeless)
				.catalyst(HenryBlocks.GOLDEN_MIXER::get)
				.catalyst(AllBlocks.BASIN::get)
				.doubleItemIcon(HenryBlocks.GOLDEN_MIXER.get(), Items.CRAFTING_TABLE)
				.emptyBackground(177, 85)
				.build(HenryCreate.asResource("golden_auto_shapeless"), MixingCategory::autoShapeless),

			goldenAutoBrewing = builder(BasinRecipe.class)
				.addRecipes(() -> RecipeGenericsUtil.cast(PotionMixingRecipes.createRecipes(Minecraft.getInstance().level)))
				.catalyst(HenryBlocks.GOLDEN_MIXER::get)
				.catalyst(AllBlocks.BASIN::get)
				.doubleItemIcon(HenryBlocks.GOLDEN_MIXER.get(), Blocks.BREWING_STAND)
				.emptyBackground(177, 103)
				.build(HenryCreate.asResource("golden_auto_brewing"), MixingCategory::autoBrewing);
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		loadCategories();
		registration.addRecipeCategories(allCategories.toArray(IRecipeCategory[]::new));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		allCategories.forEach(c -> c.registerRecipes(registration));
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		allCategories.forEach(c -> c.registerCatalysts(registration));
	}

	private <T extends Recipe<? extends RecipeInput>> CategoryBuilder<T> builder(Class<T> recipeClass) {
		return new CategoryBuilder<>(recipeClass);
	}

	private class CategoryBuilder<T extends Recipe<? extends RecipeInput>> extends CreateRecipeCategory.Builder<T> {

		public CategoryBuilder(Class<? extends T> recipeClass) {
			super(recipeClass);
		}

		@Override
		public CreateRecipeCategory<T> build(ResourceLocation id, CreateRecipeCategory.Factory<T> factory) {
			CreateRecipeCategory<T> category = super.build(id, factory);
			allCategories.add(category);
			return category;
		}
	}
}
