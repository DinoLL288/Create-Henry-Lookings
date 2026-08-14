package com.chocoboy.create_henry.infrastructure.datagen;

import com.chocoboy.create_henry.registry.HenryFluids;
import com.chocoboy.create_henry.registry.HenryItems;
import com.simibubi.create.AllFluids;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.api.data.recipe.BaseRecipeProvider.GeneratedRecipe;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

import static com.tterrag.registrate.util.entry.DataIngredient.items;

@SuppressWarnings("unused")
public final class MixingRecipeGen extends HenryProcessingRecipeGen<MixingRecipe> {

	GeneratedRecipe

	// Fluids
	RAW_RUBBER = create("raw_rubber", b -> b
		.require(HenryFluids.SAP.get(), 500)
		.output(HenryItems.RAW_RUBBER.get(), 1)
	),

	CHOCOLATE_MILKSHAKE = create("chocolate_milkshake", b -> {
		b.require(AllFluids.CHOCOLATE.get(), 250);
		require(b, Items.SUGAR, 2);
		require(b, Items.SNOWBALL, 4);
		require(b, AllItems.BAR_OF_CHOCOLATE, 2);
		return b.output(HenryFluids.CHOCOLATE_MILKSHAKE.get(), 500);
	}),

	VANILLA_MILKSHAKE = milkshake(
			"vanilla_milkshake",
			items(Items.DANDELION, Items.BLUE_ORCHID),
			HenryFluids.VANILLA_MILKSHAKE
	),

	STRAWBERRY_MILKSHAKE = milkshake(
			"strawberry_milkshake",
			Items.SWEET_BERRIES,
			HenryFluids.STRAWBERRY_MILKSHAKE
	),

	GLOWBERRY_MILKSHAKE = milkshake(
			"glowberry_milkshake",
			Items.GLOW_BERRIES,
			HenryFluids.GLOWBERRY_MILKSHAKE
	),

	PUMPKIN_MILKSHAKE = milkshake(
			"pumpkin_milkshake",
			Items.PUMPKIN,
			HenryFluids.PUMPKIN_MILKSHAKE
	),

	// Blocks
	CALCITE = create("calcite", b -> b
		.require(Items.DIORITE)
		.require(Items.BONE_BLOCK)
		.output(Items.CALCITE, 2)
		.requiresHeat(HeatCondition.HEATED)
	);

	public MixingRecipeGen(PackOutput generator, CompletableFuture<HolderLookup.Provider> lookups) {
		super(generator, lookups, MixingRecipe::new);
	}

	@Override
	protected AllRecipeTypes getRecipeType() {
		return AllRecipeTypes.MIXING;
	}

	private GeneratedRecipe milkshake(String name, Ingredient flavor, FluidEntry<?> output) {
		return create(name, b -> {
			b.require(Tags.Fluids.MILK, 250);
			require(b, Items.SUGAR, 2);
			require(b, Items.SNOWBALL, 4);
			require(b, flavor, 2);
			return b.output(output.get(), 500);
		});
	}

	private GeneratedRecipe milkshake(String name, ItemLike flavor, FluidEntry<?> output) {
		return milkshake(name, Ingredient.of(flavor), output);
	}
}
