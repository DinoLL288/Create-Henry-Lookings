package com.chocoboy.create_henry;

import com.chocoboy.create_henry.registry.*;
import com.chocoboy.create_henry.infrastructure.network.HenryPackets;
import com.mojang.logging.LogUtils;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import com.chocoboy.create_henry.content.blocks.kinetics.furnace_engine.FurnaceEngineBlock;
import com.chocoboy.create_henry.content.fans.processing.SandingType;
import com.chocoboy.create_henry.infrastructure.config.HenryConfigs;
import com.chocoboy.create_henry.infrastructure.datagen.HenryDatagen;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.Random;


@SuppressWarnings({"removal","all"})
@Mod(HenryCreate.MOD_ID)
public class HenryCreate
{
    public static final String NAME = "Create: Henry";
    public static final String MOD_ID = "create_henry";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final Random RANDOM = new Random();

    @Nullable
    public static KineticStats create(Item item) {
        if (item instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            if (block instanceof IRotate || block instanceof FurnaceEngineBlock) {
                return new KineticStats(block);
            }
        }
        return null;
    }

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID)
            .setTooltipModifierFactory(item ->
                    new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                            .andThen(TooltipModifier.mapNull(HenryCreate.create(item)))
            );

    public HenryCreate(IEventBus modEventBus, ModContainer modContainer) {
        REGISTRATE.registerEventListeners(modEventBus);

        HenryTags.init();
        HenryCreativeModeTabs.register(modEventBus);
        HenryDisplaySources.register();
        HenryBlocks.register();
        HenryItems.register();
        HenryFluids.register();
        HenryBlockEntityTypes.register();
        HenryRecipeTypes.register(modEventBus);
        HenryParticleTypes.register(modEventBus);
        HenryPackets.registerPackets();
        HenryDatagen.addExtraRegistrateData();

        // Must run after HenryBlocks.register() so its stress values are registered before the config spec is built
        HenryConfigs.register(modContainer);

        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.register(FurnaceEngineBlock.class);

        if (net.neoforged.fml.loading.FMLEnvironment.dist == net.neoforged.api.distmarker.Dist.CLIENT) {
            HenryClient.onCtorClient(modEventBus);
        }

        modEventBus.addListener(HenryCreate::init);
        modEventBus.addListener(HenryCreate::onRegister);
        modEventBus.addListener(com.chocoboy.create_henry.infrastructure.capabilities.HenryCapabilities::register);
        modEventBus.addListener(EventPriority.LOWEST, HenryDatagen::gatherData);
    }

    public static void init(final FMLCommonSetupEvent event) {
        event.enqueueWork(HenryFluids::registerFluidInteractions);
    }

    public static void onAddReloadListeners(final AddReloadListenerEvent event) {
        net.minecraft.world.item.crafting.RecipeManager recipeManager =
                event.getServerResources().getRecipeManager();
        event.addListener(new net.minecraft.server.packs.resources.SimplePreparableReloadListener<Void>() {
            @Override
            protected Void prepare(net.minecraft.server.packs.resources.ResourceManager mgr,
                                   net.minecraft.util.profiling.ProfilerFiller p) { return null; }
            @Override
            protected void apply(Void v, net.minecraft.server.packs.resources.ResourceManager mgr,
                                 net.minecraft.util.profiling.ProfilerFiller p) {
                SandingType.buildPolishCache(recipeManager);
            }
        });
    }


    public static void onRegister(final RegisterEvent event) {
        HenryFanProcessingTypes.init();
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }
}
