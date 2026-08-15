package com.chocoboy.create_henry.infrastructure.datagen;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.BaseRecipeProvider;
import com.simibubi.create.api.data.recipe.MechanicalCraftingRecipeBuilder;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
import com.chocoboy.create_henry.HenryCreate;
import com.chocoboy.create_henry.registry.HenryBlocks;
import com.chocoboy.create_henry.registry.HenryItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.concurrent.CompletableFuture;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@ParametersAreNonnullByDefault
@SuppressWarnings("unused")
public final class AdvancedCraftingRecipeGen extends BaseRecipeProvider {

    GeneratedRecipe
            // SEQUENCED ASSEMBLY RECIPE
            KINETIC_MECHANISM = createSequencedAssembly("kinetic_mechanism", b -> b
            .require(AllItems.IRON_SHEET.get())
            .transitionTo(HenryItems.INCOMPLETE_KINETIC_MECHANISM.get())
            .addOutput(HenryItems.KINETIC_MECHANISM.get(), 480)
            .addOutput(AllItems.ANDESITE_ALLOY.get(), 16)
            .addOutput(AllItems.IRON_SHEET.get(), 8)
            .addOutput(AllBlocks.COGWHEEL.get(), 5)
            .addOutput(AllItems.SUPER_GLUE.asStack(), 4)
            .addOutput(AllItems.ZINC_NUGGET.get(), 3)
            .addOutput(AllBlocks.SHAFT.get(), 2)
            .addOutput(AllItems.CRUSHED_IRON.get(), 2)
            .addOutput(Items.IRON_INGOT, 1)
            .loops(2)
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(AllItems.ANDESITE_ALLOY.get()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(AllBlocks.COGWHEEL.get()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(Items.SLIME_BALL))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(AllItems.ZINC_NUGGET.get()))
    ),

    // MECHANICAL CRAFTING RECIPE
    HYDRAULIC_PRESS = createMechanicalCrafting(HenryBlocks.HYDRAULIC_PRESS::get).returns(1)
            .recipe(b -> b
                    .key('P', AllBlocks.FLUID_PIPE.get())
                    .key('M', AllBlocks.MECHANICAL_PRESS.get())
                    .key('H', HenryBlocks.HYDRAULIC_CASING.get())
                    .key('C', Items.COPPER_BLOCK)
                    .patternLine(" P ")
                    .patternLine(" H ")
                    .patternLine("CMC")
            );

    public AdvancedCraftingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, HenryCreate.MOD_ID);
    }

    private GeneratedRecipe createSequencedAssembly(String name, UnaryOperator<SequencedAssemblyRecipeBuilder> transform) {
        GeneratedRecipe recipe =
                c -> transform.apply(new SequencedAssemblyRecipeBuilder(HenryCreate.asResource(name)))
                        .build(c);
        all.add(recipe);
        return recipe;
    }

    private GeneratedRecipeBuilder createMechanicalCrafting(Supplier<ItemLike> result) {
        return new GeneratedRecipeBuilder(result);
    }

    class GeneratedRecipeBuilder {
        private final Supplier<ItemLike> result;
        private int amount;

        private GeneratedRecipeBuilder(Supplier<ItemLike> result) {
            this.result = result;
            this.amount = 1;
        }

        GeneratedRecipeBuilder returns(int amount) {
            this.amount = amount;
            return this;
        }

        GeneratedRecipe recipe(UnaryOperator<MechanicalCraftingRecipeBuilder> builder) {
            return register(consumer -> {
                MechanicalCraftingRecipeBuilder b =
                        builder.apply(MechanicalCraftingRecipeBuilder.shapedRecipe(result.get(), amount));
                ResourceLocation location = HenryCreate.asResource(
                        "mechanical_crafting/" +
                                java.util.Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(result.get().asItem())).getPath());
                b.build(consumer, location);
            });
        }
    }
}
