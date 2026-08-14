package com.chocoboy.create_henry.infrastructure.datagen;

import com.chocoboy.create_henry.HenryCreate;
import com.chocoboy.create_henry.registry.HenryFluids;
import com.chocoboy.create_henry.registry.HenryItems;
import com.simibiy.create.api.data.recipe.BaseRecipeProvider.GeneratedRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

import static com.chocoboy.create_henry.registry.HenryTags.forgeItemTag;

@SuppressWarnings("unused")
public final class EmptyingRecipeGen extends com.simibubi.create.api.data.recipe.EmptyingRecipeGen {

    GeneratedRecipe

            SAP_LOGS = create("sap_from_logs", b -> b
                    .output(HenryFluids.SAP.get(), 100)
                    .output(Items.STICK)
                    .require(forgeItemTag("stripped_logs"))
            ),

            SAP_WOOD = create("sap_from_wood", b -> b
                    .output(HenryFluids.SAP.get(), 100)
                    .output(Items.STICK)
                    .require(forgeItemTag("stripped_wood"))
            ),

            CHOCOLATE = create("chocolate", b -> b
                    .output(HenryFluids.CHOCOLATE_MILKSHAKE.get(), 250)
                    .output(Items.GLASS_BOTTLE)
                    .require(HenryItems.CHOCOLATE_MILKSHAKE)
            ),

            VANILLA = create("vanilla", b -> b
                    .output(HenryFluids.VANILLA_MILKSHAKE.get(), 250)
                    .output(Items.GLASS_BOTTLE)
                    .require(HenryItems.VANILLA_MILKSHAKE)
            ),

            STRAWBERRY = create("strawberry", b -> b
                    .output(HenryFluids.STRAWBERRY_MILKSHAKE.get(), 250)
                    .output(Items.GLASS_BOTTLE)
                    .require(HenryItems.STRAWBERRY_MILKSHAKE)
            ),

            GLOWBERRY = create("glowberry", b -> b
                    .output(HenryFluids.GLOWBERRY_MILKSHAKE.get(), 250)
                    .output(Items.GLASS_BOTTLE)
                    .require(HenryItems.GLOWBERRY_MILKSHAKE)
            ),

            PUMPKIN = create("pumpkin", b -> b
                    .output(HenryFluids.PUMPKIN_MILKSHAKE.get(), 250)
                    .output(Items.GLASS_BOTTLE)
                    .require(HenryItems.PUMPKIN_MILKSHAKE)
            );

    public EmptyingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookups) {
        super(output, lookups, HenryCreate.MOD_ID);
    }
}
