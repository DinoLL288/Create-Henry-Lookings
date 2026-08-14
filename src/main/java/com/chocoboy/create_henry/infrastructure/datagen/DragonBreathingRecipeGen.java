package com.chocoboy.create_henry.infrastructure.datagen;

import com.chocoboy.create_henry.content.recipes.DragonBreathingRecipe;
import com.chocoboy.create_henry.registry.HenryRecipeTypes;
import com.simibubi.create.api.data.recipe.BaseRecipeProvider.GeneratedRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public final class DragonBreathingRecipeGen extends HenryProcessingRecipeGen<DragonBreathingRecipe> {

	GeneratedRecipe

			CHORUS_FRUIT = create(() -> Items.APPLE, b -> b
					.output(.50f, Items.CHORUS_FRUIT)),
			ENDER_PEARL = create(() -> Items.MAGMA_CREAM, b -> b
					.output(.15f, Items.ENDER_PEARL)),
			GHAST_TEAR = create(() -> Items.BLUE_ICE, b -> b
					.output(.10f, Items.GHAST_TEAR)
					.output(0.05f, Items.GHAST_TEAR)), // two independent chance rolls

			DRAGON_BREATH = convert(Items.POTION, Items.DRAGON_BREATH),
			END_STONE = convert(Items.DEEPSLATE, Items.END_STONE),
			CRYING_OBSIDIAN = convert(Items.OBSIDIAN, Items.CRYING_OBSIDIAN),
			END_ROD = convert(Items.BLAZE_ROD, Items.END_ROD);

	public DragonBreathingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookups) {
		super(output, lookups, DragonBreathingRecipe::new);
	}

	@Override
	protected HenryRecipeTypes getRecipeType() {
		return HenryRecipeTypes.DRAGON_BREATHING;
	}
}
