package com.chocoboy.create_henry.infrastructure.datagen;

import com.chocoboy.create_henry.HenryCreate;
import com.chocoboy.create_henry.content.recipes.SeethingRecipe;
import com.chocoboy.create_henry.registry.HenryItems;
import com.chocoboy.create_henry.registry.HenryRecipeTypes;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.simibubi.create.api.data.recipe.BaseRecipeProvider.GeneratedRecipe;
import com.simibubi.create.foundation.data.recipe.CommonMetal;
import com.simibubi.create.foundation.data.recipe.Mods;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class SeethingRecipeGen extends HenryProcessingRecipeGen<SeethingRecipe> {

	GeneratedRecipe

			ENDER_EYE = convert(Items.ENDER_PEARL, Items.ENDER_EYE),
			MAGMA_BLOCK = convert(Blocks.NETHERRACK, Blocks.MAGMA_BLOCK),
			MAGMA_CREAM = convert(Items.SLIME_BALL, Items.MAGMA_CREAM),
			BLAZE_ROD = convert(Items.END_ROD, Items.BLAZE_ROD),
			CRYING_OBSIDIAN = convert(Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN),
			COBBLED_DEEPSLATE = convert(Blocks.COBBLESTONE, Blocks.COBBLED_DEEPSLATE),

			LAPIS_LAZULI_SHARD_FROM_CALCITE = convertChanceRecipe(
					() -> Items.CALCITE,
					HenryItems.LAPIS_LAZULI_SHARD::get,
					0.75f
			),
			LAPIS_LAZULI_SHARD_FROM_LIMESTONE = convertChanceRecipe(
					() -> AllPaletteStoneTypes.LIMESTONE.baseBlock.get(),
					HenryItems.LAPIS_LAZULI_SHARD::get,
					0.05f
			),

			NETHERITE_SCRAP = secondaryRecipe(
					() -> Items.ANCIENT_DEBRIS,
					() -> Items.NETHERITE_SCRAP,
					() -> Items.NETHERITE_SCRAP,
					.35f
			),

			CRUSHED_COPPER = crushedOre(AllItems.CRUSHED_COPPER,
					() -> Items.COPPER_INGOT, () -> Items.COPPER_INGOT, .5f),
			CRUSHED_ZINC = crushedOre(AllItems.CRUSHED_ZINC,
					AllItems.ZINC_INGOT::get, AllItems.ZINC_INGOT::get, .25f),
			CRUSHED_GOLD = crushedOre(AllItems.CRUSHED_GOLD,
					() -> Items.GOLD_INGOT, () -> Items.GOLD_INGOT, .5f),
			CRUSHED_IRON = crushedOre(AllItems.CRUSHED_IRON,
					() -> Items.IRON_INGOT, () -> Items.IRON_INGOT, .75f),

			// Modded crushed ores — registers one recipe per compatible mod found at datagen time.
			// Returns null intentionally; the field is only used to trigger the side effects of create().
			CRUSHED_OSMIUM = moddedCrushedOre(AllItems.CRUSHED_OSMIUM, CommonMetal.OSMIUM),
			CRUSHED_PLATINUM = moddedCrushedOre(AllItems.CRUSHED_PLATINUM, CommonMetal.PLATINUM),
			CRUSHED_SILVER = moddedCrushedOre(AllItems.CRUSHED_SILVER, CommonMetal.SILVER),
			CRUSHED_TIN = moddedCrushedOre(AllItems.CRUSHED_TIN, CommonMetal.TIN),
			CRUSHED_LEAD = moddedCrushedOre(AllItems.CRUSHED_LEAD, CommonMetal.LEAD),
			CRUSHED_QUICKSILVER = moddedCrushedOre(AllItems.CRUSHED_QUICKSILVER, CommonMetal.QUICKSILVER),
			CRUSHED_BAUXITE = moddedCrushedOre(AllItems.CRUSHED_BAUXITE, CommonMetal.ALUMINUM),
			CRUSHED_URANIUM = moddedCrushedOre(AllItems.CRUSHED_URANIUM, CommonMetal.URANIUM),
			CRUSHED_NICKEL = moddedCrushedOre(AllItems.CRUSHED_NICKEL, CommonMetal.NICKEL);

	public SeethingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookups) {
		super(output, lookups, SeethingRecipe::new);
	}

	@Override
	protected HenryRecipeTypes getRecipeType() {
		return HenryRecipeTypes.SEETHING;
	}

	// --- Chance-based / secondary recipes ---

	private GeneratedRecipe secondaryRecipe(Supplier<ItemLike> item,
										   Supplier<ItemLike> first,
										   Supplier<ItemLike> secondary,
										   float secondaryChance) {
		return create(
				HenryCreate.asResource(getItemName(first.get()) + "_from_" + getItemName(item.get())),
				b -> b.withItemIngredients(Ingredient.of(item.get()))
						.output(first.get(), 1)
						.output(secondaryChance, secondary.get(), 1)
		);
	}

	private GeneratedRecipe convertChanceRecipe(Supplier<ItemLike> item,
											   Supplier<ItemLike> result,
											   float chance) {
		return create(
				HenryCreate.asResource(getItemName(result.get()) + "_from_" + getItemName(item.get())),
				b -> b.withItemIngredients(Ingredient.of(item.get()))
						.output(chance, result.get(), 1)
		);
	}

	// --- Vanilla crushed ores ---

	private GeneratedRecipe crushedOre(ItemEntry<Item> crushed,
									  Supplier<ItemLike> ingot,
									  Supplier<ItemLike> secondary,
									  float secondaryChance) {
		return create(crushed::get,
				b -> b.output(ingot.get(), 1)
						.output(secondaryChance, secondary.get(), 1)
		);
	}

	// --- Modded crushed ores using CommonMetal ---

	@SuppressWarnings("deprecation")
	private GeneratedRecipe moddedCrushedOre(ItemEntry<? extends Item> crushed, CommonMetal metal) {
		for (Mods mod : Mods.values()) {
			if (!CommonMetal.of(mod).contains(metal))
				continue;

			String metalName = metal.getName(mod);
			ResourceLocation ingotId = mod.ingotOf(metalName);

			create(mod.getId() + "/" + crushed.getId().getPath(),
					b -> b.withItemIngredients(Ingredient.of(crushed::get))
							.output(1, ingotId, 1)
							.output(0.5f, ingotId, 1)
							.whenModLoaded(mod.getId()));
		}
		// Returns null intentionally — this method registers recipes as a side effect.
		return null;
	}
}
