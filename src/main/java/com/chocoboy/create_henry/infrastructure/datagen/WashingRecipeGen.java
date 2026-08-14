package com.chocoboy.create_henry.infrastructure.datagen;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.api.data.recipe.BaseRecipeProvider.GeneratedRecipe;
import com.simibubi.create.content.kinetics.fan.processing.SplashingRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public final class WashingRecipeGen extends HenryProcessingRecipeGen<SplashingRecipe> {

	GeneratedRecipe

			EXPOSED_COPPER = convert(Blocks.COPPER_BLOCK, Blocks.EXPOSED_COPPER),
			WEATHERED_COPPER = convert(Blocks.EXPOSED_COPPER, Blocks.WEATHERED_COPPER),
			OXIDIZED_COPPER = convert(Blocks.WEATHERED_COPPER, Blocks.OXIDIZED_COPPER),

			EXPOSED_CUT_COPPER = convert(Blocks.CUT_COPPER, Blocks.EXPOSED_CUT_COPPER),
			WEATHERED_CUT_COPPER = convert(Blocks.EXPOSED_CUT_COPPER, Blocks.WEATHERED_CUT_COPPER),
			OXIDIZED_CUT_COPPER = convert(Blocks.WEATHERED_CUT_COPPER, Blocks.WEATHERED_CUT_COPPER),

			EXPOSED_CUT_COPPER_SLAB = convert(Blocks.CUT_COPPER_SLAB, Blocks.EXPOSED_CUT_COPPER_SLAB),
			WEATHERED_CUT_COPPER_SLAB = convert(Blocks.EXPOSED_CUT_COPPER_SLAB, Blocks.WEATHERED_CUT_COPPER_SLAB),
			OXIDIZED_CUT_COPPER_SLAB = convert(Blocks.WEATHERED_CUT_COPPER_SLAB, Blocks.WEATHERED_CUT_COPPER_SLAB),

			EXPOSED_CUT_COPPER_STAIRS = convert(Blocks.CUT_COPPER_STAIRS, Blocks.EXPOSED_CUT_COPPER_STAIRS),
			WEATHERED_CUT_COPPER_STAIRS = convert(Blocks.EXPOSED_CUT_COPPER_STAIRS, Blocks.WEATHERED_CUT_COPPER_STAIRS),
			OXIDIZED_CUT_COPPER_STAIRS = convert(Blocks.WEATHERED_CUT_COPPER_STAIRS, Blocks.WEATHERED_CUT_COPPER_STAIRS);

	public WashingRecipeGen(PackOutput dataGenerator, CompletableFuture<HolderLookup.Provider> lookups) {
		super(dataGenerator, lookups, SplashingRecipe::new);
	}

	@Override
	protected AllRecipeTypes getRecipeType() {
		return AllRecipeTypes.SPLASHING;
	}
}
