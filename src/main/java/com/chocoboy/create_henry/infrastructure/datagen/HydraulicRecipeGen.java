package com.chocoboy.create_henry.infrastructure.datagen;

import com.chocoboy.create_henry.content.recipes.HydraulicRecipe;
import com.chocoboy.create_henry.registry.HenryFluids;
import com.chocoboy.create_henry.registry.HenryRecipeTypes;
import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.BaseRecipeProvider.GeneratedRecipe;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public final class HydraulicRecipeGen extends HenryProcessingRecipeGen<HydraulicRecipe> {

	GeneratedRecipe

			TUFF = create("tuff", b -> b
				.requiresHeat(HeatCondition.SUPERHEATED)
				.require(Items.GRAVEL)
				.require(FluidTags.LAVA, 500)
				.require(Items.DEEPSLATE)
				.require(Items.ANDESITE)
				.output(Items.TUFF, 4)
			),

			CALCITE = create("calcite", b -> b
				.requiresHeat(HeatCondition.HEATED)
				.require(Items.BONE_BLOCK)
				.require(Items.DIORITE)
				.output(Items.CALCITE, 2)
			),

			NETHERRACK = create("netherrack", b -> b
				.requiresHeat(HeatCondition.HEATED)
				.require(AllItems.CINDER_FLOUR)
				.require(AllItems.CINDER_FLOUR)
				.require(AllPaletteStoneTypes.SCORIA.getBaseBlock().get())
				.require(AllPaletteStoneTypes.SCORIA.getBaseBlock().get())
				.output(Items.NETHERRACK, 4)
			),

			COBBLE_GEN = create("cobblestone_gen", b -> b
				.require(FluidTags.LAVA, 100)
				.require(FluidTags.WATER, 500)
				.output(Items.COBBLESTONE, 10)
			),

			STONE_GEN = create("stone_gen", b -> b
				.requiresHeat(HeatCondition.HEATED)
				.require(FluidTags.LAVA, 100)
				.require(FluidTags.WATER, 500)
				.output(Items.STONE, 10)
			),

			BASALT_GEN = create("basalt_gen", b -> b
				.requiresHeat(HeatCondition.HEATED)
				.require(FluidTags.LAVA, 100)
				.require(Items.BLUE_ICE)
				.require(Items.SOUL_SOIL)
				.output(Items.BASALT, 10)
				.output(Items.BLUE_ICE, 1)
				.output(Items.SOUL_SOIL, 1)
			),

			VERIDIUM_GEN = create("veridium_gen", b -> b
				.requiresHeat(HeatCondition.SUPERHEATED)
				.require(FluidTags.LAVA, 500)
				.require(HenryFluids.CHOCOLATE_MILKSHAKE.get(), 100)
				.require(Items.GRANITE)
				.require(Items.GRANITE)
				.output(AllPaletteStoneTypes.VERIDIUM.getBaseBlock().get(), 2)
			),

			ASURINE_GEN = create("asurine_gen", b -> b
				.requiresHeat(HeatCondition.SUPERHEATED)
				.require(FluidTags.LAVA, 500)
				.require(HenryFluids.VANILLA_MILKSHAKE.get(), 100)
				.require(Items.SANDSTONE)
				.require(Items.SANDSTONE)
				.output(AllPaletteStoneTypes.ASURINE.getBaseBlock().get(), 2)
			),

			CRIMSITE_GEN = create("crimsite_gen", b -> b
				.requiresHeat(HeatCondition.SUPERHEATED)
				.require(FluidTags.LAVA, 500)
				.require(HenryFluids.STRAWBERRY_MILKSHAKE.get(), 100)
				.require(Items.COBBLED_DEEPSLATE)
				.require(Items.COBBLED_DEEPSLATE)
				.output(AllPaletteStoneTypes.CRIMSITE.getBaseBlock().get(), 2)
			),

			OCHRUM_GEN = create("ochrum_gen", b -> b
				.requiresHeat(HeatCondition.SUPERHEATED)
				.require(FluidTags.LAVA, 500)
				.require(HenryFluids.GLOWBERRY_MILKSHAKE.get(), 100)
				.require(Items.TERRACOTTA)
				.require(Items.TERRACOTTA)
				.output(AllPaletteStoneTypes.OCHRUM.getBaseBlock().get(), 2)
			);

	public HydraulicRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookups) {
		super(output, lookups, HydraulicRecipe::new);
	}

	@Override
	protected IRecipeTypeInfo getRecipeType() {
		return HenryRecipeTypes.HYDRAULIC_COMPACTING;
	}
}
