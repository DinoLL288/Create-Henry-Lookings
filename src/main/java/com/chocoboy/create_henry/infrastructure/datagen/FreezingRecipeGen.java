package com.chocoboy.create_henry.infrastructure.datagen;

import com.chocoboy.create_henry.content.recipes.FreezingRecipe;
import com.chocoboy.create_henry.registry.HenryRecipeTypes;
import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.BaseRecipeProvider.GeneratedRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class FreezingRecipeGen extends HenryProcessingRecipeGen<FreezingRecipe> {

	GeneratedRecipe

			BLAZE_CAKE = secondaryRecipe(AllItems.BLAZE_CAKE::get, AllItems.POWDERED_OBSIDIAN::get, AllItems.CINDER_FLOUR::get, .90f),
			PACKED_ICE = convert(Items.ICE, Items.PACKED_ICE),
			BLUE_ICE = convert(Items.PACKED_ICE, Items.BLUE_ICE),
			POWDER_SNOW_BUCKET = convert(Items.WATER_BUCKET, Items.POWDER_SNOW_BUCKET),
			SLIME_BALL = convert(Items.MAGMA_CREAM, Items.SLIME_BALL),
			SNOW = convert(Items.SNOWBALL, Items.SNOW),
			SNOW_BLOCK = convert(Items.SNOW, Items.SNOW_BLOCK),
			OBSIDIAN = convert(Items.CRYING_OBSIDIAN, Items.OBSIDIAN);

	private GeneratedRecipe secondaryRecipe(Supplier<ItemLike> item, Supplier<ItemLike> first, Supplier<ItemLike> secondary, float secondaryChance) {
		return create(item, b -> b.output(first.get(), 1)
				.output(secondaryChance, secondary.get(), 1));
	}

	public FreezingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookups) {
		super(output, lookups, FreezingRecipe::new);
	}

	@Override
	protected HenryRecipeTypes getRecipeType() {
		return HenryRecipeTypes.FREEZING;
	}
}
