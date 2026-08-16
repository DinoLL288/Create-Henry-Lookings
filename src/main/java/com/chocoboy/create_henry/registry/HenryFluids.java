package com.chocoboy.create_henry.registry;

import com.chocoboy.create_henry.HenryCreate;
import com.chocoboy.create_henry.content.fluids.SolidRenderedPlaceableFluidType;
import com.chocoboy.create_henry.infrastructure.config.HenryConfigs;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidInteractionRegistry;
import net.minecraft.world.level.material.Fluid;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class HenryFluids {

    private static final CreateRegistrate REGISTRATE = HenryCreate.registrate();

    static {
        REGISTRATE.setCreativeTab(HenryCreativeModeTabs.BASE_CREATIVE_TAB);
    }

    private static final float FOG_DISTANCE_SCALE = 0.25f;

    // Fluid registrations

    public static final FluidEntry<BaseFlowingFluid.Flowing> CHOCOLATE_MILKSHAKE = newMilkshake(
            "Chocolate Milkshake",
            () -> HenryConfigs.client().chocolateFogColor.get(),
            () -> HenryConfigs.client().chocolateTransparencyMultiplier.getF(),
            HenryTags.AllFluidTags.CHOCOLATE.tag).register();

    public static final FluidEntry<BaseFlowingFluid.Flowing> VANILLA_MILKSHAKE = newMilkshake(
            "Vanilla Milkshake",
            () -> HenryConfigs.client().vanillaFogColor.get(),
            () -> HenryConfigs.client().vanillaTransparencyMultiplier.getF(),
            HenryTags.AllFluidTags.VANILLA.tag).register();

    public static final FluidEntry<BaseFlowingFluid.Flowing> STRAWBERRY_MILKSHAKE = newMilkshake(
            "Strawberry Milkshake",
            () -> HenryConfigs.client().strawberryFogColor.get(),
            () -> HenryConfigs.client().strawberryTransparencyMultiplier.getF(),
            HenryTags.AllFluidTags.STRAWBERRY.tag).register();

    public static final FluidEntry<BaseFlowingFluid.Flowing> GLOWBERRY_MILKSHAKE = newMilkshake(
            "Glowberry Milkshake",
            () -> HenryConfigs.client().glowberryFogColor.get(),
            () -> HenryConfigs.client().glowberryTransparencyMultiplier.getF(),
            HenryTags.AllFluidTags.GLOWBERRY.tag).register();

    public static final FluidEntry<BaseFlowingFluid.Flowing> PUMPKIN_MILKSHAKE = newMilkshake(
            "Pumpkin Milkshake",
            () -> HenryConfigs.client().pumpkinFogColor.get(),
            () -> HenryConfigs.client().pumpkinTransparencyMultiplier.getF(),
            HenryTags.AllFluidTags.PUMPKIN.tag).register();

    public static final FluidEntry<BaseFlowingFluid.Flowing> SAP = newFluid(
            "Sap", 2000, 25,
            () -> HenryConfigs.client().sapFogColor.get(),
            () -> HenryConfigs.client().sapTransparencyMultiplier.getF(),
            HenryTags.AllFluidTags.SAP.tag).register();

    // Load this class
    public static void register() {}

    // Lava interactions

    private record LavaInteraction(BaseFlowingFluid fluid, Block oreStone, Block defaultStone) {}

    private static List<LavaInteraction> lavaInteractions() {
        return List.of(
            new LavaInteraction(CHOCOLATE_MILKSHAKE.get(), AllPaletteStoneTypes.VERIDIUM.getBaseBlock().get(), Blocks.GRANITE),
            new LavaInteraction(VANILLA_MILKSHAKE.get(), AllPaletteStoneTypes.ASURINE.getBaseBlock().get(), Blocks.SANDSTONE),
            new LavaInteraction(STRAWBERRY_MILKSHAKE.get(), AllPaletteStoneTypes.CRIMSITE.getBaseBlock().get(), Blocks.COBBLED_DEEPSLATE),
            new LavaInteraction(GLOWBERRY_MILKSHAKE.get(), AllPaletteStoneTypes.OCHRUM.getBaseBlock().get(), Blocks.TERRACOTTA),
            new LavaInteraction(PUMPKIN_MILKSHAKE.get(), AllPaletteStoneTypes.LIMESTONE.getBaseBlock().get(), AllPaletteStoneTypes.SCORCHIA.getBaseBlock().get())
        );
    }

    public static void registerFluidInteractions() {
        for (LavaInteraction interaction : lavaInteractions())
            registerLavaInteraction(interaction.fluid(), interaction.oreStone(), interaction.defaultStone());
    }

    @Nullable
    public static BlockState getLavaInteraction(BaseFlowingFluid fluidState, Level level, BlockPos pos) {
        for (LavaInteraction interaction : lavaInteractions()) {
            if (isAdjacentToFluid(interaction.fluid(), level, pos))
                return selectLavaResult(interaction.oreStone(), interaction.defaultStone(), level, pos);
        }
        return null;
    }

    // Registration helpers

    @SafeVarargs
    private static FluidBuilder<BaseFlowingFluid.Flowing, CreateRegistrate> newMilkshake(String name, Supplier<Integer> fogColor, Supplier<Float> transparency, TagKey<Fluid>... tags) {
        return newFluid(name, 1000, 10, fogColor, transparency, tags);
    }

    @SafeVarargs
    private static FluidBuilder<BaseFlowingFluid.Flowing, CreateRegistrate> newFluid(String name, int viscosity, int tickRate, Supplier<Integer> fogColor, Supplier<Float> transparency, TagKey<Fluid>... tags) {
        String id = name.toLowerCase().replace(" ", "_");
        ResourceLocation stillTex = ResourceLocation.fromNamespaceAndPath(HenryCreate.MOD_ID, "fluid/" + id + "_still");
        ResourceLocation flowTex = ResourceLocation.fromNamespaceAndPath(HenryCreate.MOD_ID, "fluid/" + id + "_flow");
        return REGISTRATE.standardFluid(id,
                        SolidRenderedPlaceableFluidType.create(fogColor, () -> FOG_DISTANCE_SCALE * transparency.get(), stillTex, flowTex))
                .lang(name)
                .properties(b -> b.viscosity(viscosity).density(1400))
                .fluidProperties(p -> p.levelDecreasePerBlock(2).tickRate(tickRate).slopeFindDistance(3).explosionResistance(100f))
                .tag(tags)
                .source(BaseFlowingFluid.Source::new)
                .block()
                .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models()
                        .getBuilder(ctx.getName())
                        .texture("particle", stillTex.toString())))
                .build()
                .bucket()
                .model((ctx, prov) -> prov.withExistingParent(ctx.getName(), ResourceLocation.fromNamespaceAndPath("minecraft", "item/generated"))
                        .texture("layer0", ResourceLocation.fromNamespaceAndPath(HenryCreate.MOD_ID, "item/" + ctx.getName())))
                .tag(HenryTags.forgeItemTag("buckets/" + id))
                .build();
    }

    // Lava interaction helpers

    private static void registerLavaInteraction(BaseFlowingFluid fluid, Block oreStone, Block defaultStone) {
        var lavaType = NeoForgeMod.LAVA_TYPE.value();

        // Source lava touching this fluid -> obsidian
        FluidInteractionRegistry.addInteraction(lavaType, new FluidInteractionRegistry.InteractionInformation(
                (level, currentPos, relativePos, lavaState) ->
                        lavaState.isSource() && level.getFluidState(relativePos).is(fluid),
                Blocks.OBSIDIAN.defaultBlockState()
        ));

        // Flowing lava on an ore generator block -> ore stone (random chance)
        FluidInteractionRegistry.addInteraction(lavaType, new FluidInteractionRegistry.InteractionInformation(
                (level, currentPos, relativePos, lavaState) ->
                        !lavaState.isSource() &&
                        level.getFluidState(relativePos).is(fluid) &&
                        level.getBlockState(currentPos.below()).is(HenryTags.AllBlockTags.ORE_GENERATOR.tag) &&
                        randomChance(HenryConfigs.server().chanceForOreStone.get(), level),
                oreStone.defaultBlockState()
        ));

        // Flowing lava on an artificial ore generator block -> ore stone (random chance)
        FluidInteractionRegistry.addInteraction(lavaType, new FluidInteractionRegistry.InteractionInformation(
                (level, currentPos, relativePos, lavaState) ->
                        !lavaState.isSource() &&
                        level.getFluidState(relativePos).is(fluid) &&
                        level.getBlockState(currentPos.below()).is(HenryTags.AllBlockTags.ARTIFICIAL_ORE_GENERATOR.tag) &&
                        randomChance(HenryConfigs.server().chanceForArtificialOreStone.get(), level),
                oreStone.defaultBlockState()
        ));

        // Flowing lava touching this fluid anywhere else -> default stone
        FluidInteractionRegistry.addInteraction(lavaType, new FluidInteractionRegistry.InteractionInformation(
                (level, currentPos, relativePos, lavaState) ->
                        !lavaState.isSource() && level.getFluidState(relativePos).is(fluid),
                defaultStone.defaultBlockState()
        ));
    }

    private static boolean isAdjacentToFluid(BaseFlowingFluid fluid, Level level, BlockPos pos) {
        if (level.getFluidState(pos).is(fluid)) return true;
        boolean adjacentFluid = false;
        for (Direction direction : Direction.values()) {
            if (level.getFluidState(pos.relative(direction)).is(fluid)) {
                adjacentFluid = true;
                break;
            }
        }
        boolean replaceable = level.getBlockState(pos).isAir() || level.getBlockState(pos).canBeReplaced();
        return adjacentFluid && replaceable && level.getFluidState(pos).isEmpty();
    }

    private static BlockState selectLavaResult(Block oreStone, Block defaultStone, Level level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        if (below.is(HenryTags.AllBlockTags.ORE_GENERATOR.tag) && randomChance(HenryConfigs.server().chanceForOreStone.get(), level))
            return oreStone.defaultBlockState();
        if (below.is(HenryTags.AllBlockTags.ARTIFICIAL_ORE_GENERATOR.tag) && randomChance(HenryConfigs.server().chanceForArtificialOreStone.get(), level))
            return oreStone.defaultBlockState();
        return defaultStone.defaultBlockState();
    }

    private static boolean randomChance(int chance, Level level) {
        return level.getRandom().nextInt(100) < chance;
    }
}
