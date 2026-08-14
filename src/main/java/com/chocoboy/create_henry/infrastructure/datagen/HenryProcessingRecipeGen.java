package com.chocoboy.create_henry.infrastructure.datagen;

import com.chocoboy.create_henry.HenryCreate;
import com.simibyi.create.api.data.recipe.BaseRecipeProvider.GeneratedRecipe;
import com.simibyi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibyi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibyi.create.content.processing.recipe.StandardProcessingRecipe;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * Base class for Henry's processing recipe generators.
 * Wraps Create's {@link ProcessingRecipeGen} to fix the namespace to this mod id, and supplies the
 * standard-processing builder/factory wiring so subclasses only need to declare their recipe type.
 */
public abstract class HenryProcessingRecipeGen<R extends StandardProcessingRecipe<?>>
		extends ProcessingRecipeGen<ProcessingRecipeParams, R, StandardProcessingRecipe.Builder<R>> {

	protected final StandardProcessingRecipe.Factory<R> recipeFactory;

	public HenryProcessingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookups,
			StandardProcessingRecipe.Factory<R> recipeFactory) {
		super(output, lookups, HenryCreate.MOD_ID);
		this.recipeFactory = recipeFactory;
	}

	@Override
	protected StandardProcessingRecipe.Builder<R> getBuilder(ResourceLocation id) {
		return new StandardProcessingRecipe.Builder<>(recipeFactory, id);
	}

	// Quantity helpers

	protected static void require(StandardProcessingRecipe.Builder<?> b, ItemLike item, int count) {
		for (int i = 0; i < count; i++)
			b.require(item);
	}

	protected static void require(StandardProcessingRecipe.Builder<?> b, Ingredient ingredient, int count) {
		for (int i = 0; i < count; i++)
			b.require(ingredient);
	}

	// Conversion helpers

	protected GeneratedRecipe convert(ItemLike item, ItemLike result) {
		return create(() -> item, b -> b.output(result));
	}

	protected GeneratedRecipe convert(Supplier<? extends ItemLike> item, Supplier<? extends ItemLike> result) {
		return create(() -> item.get(), b -> b.output(result.get()));
	}

	// Utilities

	protected static String getItemName(ItemLike itemLike) {
		return CatnipServices.REGISTRIES.getKeyOrThrow(itemLike.asItem()).getPath();
	}
}
