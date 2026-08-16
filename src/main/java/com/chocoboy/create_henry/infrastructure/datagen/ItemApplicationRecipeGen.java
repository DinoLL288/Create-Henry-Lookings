package com.chocoboy.create_henry.infrastructure.datagen;

import com.chocoboy.create_henry.HenryCreate;
import com.chocoboy.create_henry.registry.HenryBlocks;
import com.chocoboy.create_henry.registry.HenryItems;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.api.data.recipe.BaseRecipeProvider.GeneratedRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;

import java.util.concurrent.CompletableFuture;

import static com.chocoboy.create_henry.registry.HenryTags.forgeItemTag;

@SuppressWarnings("unused")
public final class ItemApplicationRecipeGen extends com.simibubi.create.api.data.recipe.ItemApplicationRecipeGen {

	GeneratedRecipe

			INDUSTRIAL_CASING = create("industrial_casing", b -> b
					.require(AllBlocks.INDUSTRIAL_IRON_BLOCK.get())
					.require(AllItems.ZINC_INGOT.get())
					.output(HenryBlocks.INDUSTRIAL_CASING.get())),

			HYDRAULIC_CASING = create("hydraulic_casing", b -> b
					.require(AllBlocks.COPPER_CASING.get())
					.require(AllItems.COPPER_SHEET.get())
					.output(HenryBlocks.HYDRAULIC_CASING.get())),

			RUBBER_CASING = create("rubber_casing", b -> b
					.require(forgeItemTag("stripped_logs"))
					.require(HenryItems.RUBBER.get())
					.output(HenryBlocks.RUBBER_CASING.get())),

			COGWHEEL = create("cogwheel", b -> b
					.require(AllBlocks.SHAFT.get())
					.require(ItemTags.PLANKS)
					.output(AllBlocks.COGWHEEL.get())),

			LARGE_COGWHEEL = create("large_cogwheel", b -> b
					.require(AllBlocks.COGWHEEL.get())
					.require(ItemTags.PLANKS)
					.output(AllBlocks.LARGE_COGWHEEL.get())),

			DOWNGRADE_COGWHEEL = create("downgrade_cogwheel", b -> b
					.require(AllBlocks.COGWHEEL.get())
					.require(forgeItemTag("nuggets/coal"))
					.output(AllBlocks.SHAFT.get())),

			DOWNGRADE_LARGE_COGWHEEL = create("downgrade_large_cogwheel", b -> b
					.require(AllBlocks.LARGE_COGWHEEL.get())
					.require(forgeItemTag("nuggets/coal"))
					.output(AllBlocks.COGWHEEL.get()));

	public ItemApplicationRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookups) {
		super(output, lookups, HenryCreate.MOD_ID);
	}

	@Override
	protected AllRecipeTypes getRecipeType() {
		return AllRecipeTypes.ITEM_APPLICATION;
	}
}
