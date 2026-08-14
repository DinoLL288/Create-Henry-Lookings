package com.chocoboy.create_henry.infrastructure.datagen;

import com.chocoboy.create_henry.content.recipes.WitheringRecipe;
import com.chocoboy.create_henry.registry.HenryRecipeTypes;
import com.chocoboy.create_henry.registry.HenryTags;
import com.simibubi.create.api.data.recipe.BaseRecipeProvider.GeneratedRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

import static com.chocoboy.create_henry.registry.HenryTags.minecraftItemTag;

@SuppressWarnings("unused")
public final class WitheringRecipeGen extends HenryProcessingRecipeGen<WitheringRecipe> {

	GeneratedRecipe

			ROTTEN_FLESH = create("meat_to_rotten_flesh", b -> b
					.require(HenryTags.AllItemTags.MEAT.tag)
					.output(Items.ROTTEN_FLESH)
			),

			WITHER_ROSE = create("small_flowers_to_wither_rose", b -> b
					.require(minecraftItemTag("small_flowers"))
					.output(Items.WITHER_ROSE)
			),

			DEAD_BUSH = create("tall_flowers_to_dead_bush", b -> b
					.require(minecraftItemTag("tall_flowers"))
					.output(Items.DEAD_BUSH)
			),

			COAL = convert(Items.CHARCOAL, Items.COAL),
			POISONOUS_POTATO = convert(Items.POTATO, Items.POISONOUS_POTATO),
			COBBLED_DEEPSLATE = convert(Items.COBBLESTONE, Items.COBBLED_DEEPSLATE),
			DEEPSLATE = convert(Items.STONE, Items.DEEPSLATE),
			CLAY = convert(Items.SLIME_BALL, Items.CLAY_BALL),
			MYCELIUM = convert(Items.GRASS_BLOCK, Items.MYCELIUM);

	public WitheringRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookups) {
		super(output, lookups, WitheringRecipe::new);
	}

	@Override
	protected HenryRecipeTypes getRecipeType() {
		return HenryRecipeTypes.WITHERING;
	}
}
