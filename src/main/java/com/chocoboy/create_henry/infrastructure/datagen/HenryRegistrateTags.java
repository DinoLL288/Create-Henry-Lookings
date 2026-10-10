package com.chocoboy.create_henry.infrastructure.datagen;

import com.chocoboy.create_henry.HenryCreate;
import com.chocoboy.create_henry.compat.HenryMods;
import com.chocoboy.create_henry.registry.HenryBlocks;
import com.chocoboy.create_henry.registry.HenryTags;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import static com.chocoboy.create_henry.registry.HenryTags.commonItemTag;

@SuppressWarnings({"deprecation"})
public class HenryRegistrateTags {

    public static void addGenerators() {
        HenryCreate.REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, HenryRegistrateTags::genBlockTags);
        HenryCreate.REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, HenryRegistrateTags::genItemTags);
    }
    private static void genItemTags(RegistrateTagsProvider<Item> provIn) {
        TagGen.CreateTagsProvider<Item> prov = new TagGen.CreateTagsProvider<>(provIn, Item::builtInRegistryHolder);

        prov.tag(commonItemTag("dusts/obsidian"))
                .add(AllItems.POWDERED_OBSIDIAN.get())
        ;
        prov.tag(commonItemTag("dusts"))
                .add(AllItems.POWDERED_OBSIDIAN.get())
        ;
        prov.tag(commonItemTag("stripped_logs"))
                .add(Items.STRIPPED_OAK_LOG, Items.STRIPPED_BIRCH_LOG, Items.STRIPPED_SPRUCE_LOG, Items.STRIPPED_JUNGLE_LOG, Items.STRIPPED_ACACIA_LOG, Items.STRIPPED_DARK_OAK_LOG, Items.STRIPPED_MANGROVE_LOG, Items.STRIPPED_CHERRY_LOG, Items.STRIPPED_CRIMSON_STEM, Items.STRIPPED_WARPED_STEM)
        ;
        prov.tag(commonItemTag("stripped_wood"))
                .add(Items.STRIPPED_OAK_WOOD, Items.STRIPPED_BIRCH_WOOD, Items.STRIPPED_SPRUCE_WOOD, Items.STRIPPED_JUNGLE_WOOD, Items.STRIPPED_ACACIA_WOOD, Items.STRIPPED_DARK_OAK_WOOD, Items.STRIPPED_MANGROVE_WOOD, Items.STRIPPED_CHERRY_WOOD, Items.STRIPPED_CRIMSON_HYPHAE, Items.STRIPPED_WARPED_HYPHAE)
        ;
        prov.tag(HenryTags.AllItemTags.SEETHABLE.tag)
                .add(Items.ENDER_PEARL)
                .add(Items.NETHERRACK)
                .add(Items.SLIME_BALL)
                .add(Items.OBSIDIAN)
                .add(Items.COBBLESTONE)
                .add(Items.CALCITE)
                .add(Items.COAL_BLOCK)
                .add(Items.DEEPSLATE_COAL_ORE)
                .add(Items.ANCIENT_DEBRIS)
                .add(AllItems.CRUSHED_COPPER.get())
                .add(AllItems.CRUSHED_ZINC.get())
                .add(AllItems.CRUSHED_GOLD.get())
                .add(AllItems.CRUSHED_IRON.get())
                .add(AllItems.CRUSHED_OSMIUM.get())
                .add(AllItems.CRUSHED_PLATINUM.get())
                .add(AllItems.CRUSHED_SILVER.get())
                .add(AllItems.CRUSHED_TIN.get())
                .add(AllItems.CRUSHED_LEAD.get())
                .add(AllItems.CRUSHED_QUICKSILVER.get())
                .add(AllItems.CRUSHED_BAUXITE.get())
                .add(AllItems.CRUSHED_URANIUM.get())
                .add(AllItems.CRUSHED_NICKEL.get())
        ;
        prov.tag(HenryTags.AllItemTags.FREEZABLE.tag)
                .add(AllItems.BLAZE_CAKE.get())
                .add(Items.ICE)
                .add(Items.PACKED_ICE)
                .add(Items.WATER_BUCKET)
                .add(Items.MAGMA_CREAM)
                .add(Items.SNOWBALL)
                .add(Items.SNOW)
                .add(Items.CRYING_OBSIDIAN)
        ;
        prov.tag(HenryTags.AllItemTags.SANDABLE.tag)
                .add(Items.EXPOSED_COPPER)
                .add(Items.WEATHERED_COPPER)
                .add(Items.OXIDIZED_COPPER)
                .add(Items.EXPOSED_CUT_COPPER)
                .add(Items.WEATHERED_CUT_COPPER)
                .add(Items.OXIDIZED_CUT_COPPER)
                .add(Items.EXPOSED_CUT_COPPER_SLAB)
                .add(Items.WEATHERED_CUT_COPPER_SLAB)
                .add(Items.OXIDIZED_CUT_COPPER_SLAB)
                .add(Items.EXPOSED_CUT_COPPER_STAIRS)
                .add(Items.WEATHERED_CUT_COPPER_STAIRS)
                .add(Items.OXIDIZED_CUT_COPPER_STAIRS)
                .add(Items.POLISHED_ANDESITE)
                .add(Items.POLISHED_ANDESITE_SLAB)
                .add(Items.POLISHED_ANDESITE_STAIRS)
                .add(Items.POLISHED_GRANITE)
                .add(Items.POLISHED_GRANITE_SLAB)
                .add(Items.POLISHED_GRANITE_STAIRS)
                .add(Items.POLISHED_DIORITE)
                .add(Items.POLISHED_DIORITE_SLAB)
                .add(Items.POLISHED_DIORITE_STAIRS)
                .add(Items.POLISHED_DEEPSLATE)
                .add(Items.POLISHED_DEEPSLATE_SLAB)
                .add(Items.POLISHED_DEEPSLATE_STAIRS)
                .add(Items.POLISHED_DEEPSLATE_WALL)
                .add(Items.POLISHED_BASALT)
                .add(Items.MUD)
                .add(Items.WARPED_NYLIUM)
                .add(Items.CRIMSON_NYLIUM)
                .add(Items.MAGMA_BLOCK)
                .add(AllItems.ROSE_QUARTZ.get())
        ;

        prov.tag(HenryTags.AllItemTags.WITHERABLE.tag)
                .add(Items.CHARCOAL)
                .add(Items.COBBLESTONE)
                .add(Items.STONE)
                .add(Items.SLIME_BALL)
                .add(Items.GRASS_BLOCK)
        ;

        prov.tag(HenryTags.AllItemTags.MEAT.tag)
                .add(Items.BEEF, Items.PORKCHOP, Items.CHICKEN, Items.MUTTON, Items.COD, Items.SALMON, Items.RABBIT)
        ;

    }

    private static void genBlockTags(RegistrateTagsProvider<Block> provIn) {
        TagGen.CreateTagsProvider<Block> prov = new TagGen.CreateTagsProvider<>(provIn, Block::builtInRegistryHolder);

        prov.tag(HenryTags.AllBlockTags.ORE_GENERATOR.tag)
                .add(Blocks.BEDROCK);
        prov.tag(HenryTags.AllBlockTags.ARTIFICIAL_ORE_GENERATOR.tag)
                .add(Blocks.NETHERITE_BLOCK, Blocks.REINFORCED_DEEPSLATE, Blocks.DRAGON_EGG, Blocks.END_PORTAL, Blocks.BEACON);

        prov.tag(HenryTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_SANDING.tag)
                .add(Blocks.SAND, Blocks.RED_SAND);

        prov.tag(HenryTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_FREEZING.tag)
                .add(Blocks.POWDER_SNOW);

        var seething = prov
                .tag(HenryTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_SEETHING.tag)
                .add(AllBlocks.BLAZE_BURNER.get());

        // Compat
        // Add the createaddition blaze burner if the mod is loaded
        if (HenryMods.CREATEADDITION.isLoaded()) {
            Block compat = HenryMods.CREATEADDITION.getBlock("liquid_blaze_burner");
            if (compat != null)
                seething.add(compat);
        }

        prov.tag(HenryTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_WITHERING.tag)
                .add(Blocks.WITHER_ROSE);

        prov.tag(HenryTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_DRAGON_BREATHING.tag)
                .add(Blocks.DRAGON_HEAD)
                .add(Blocks.DRAGON_WALL_HEAD);

        prov.tag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag)
                .addTag(HenryTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_SANDING.tag)
                .addTag(HenryTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_FREEZING.tag)
                .addTag(HenryTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_SEETHING.tag)
                .addTag(HenryTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_WITHERING.tag)
                .addTag(HenryTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_DRAGON_BREATHING.tag);

        prov.tag(HenryTags.AllBlockTags.INDUSTRIAL_FAN_TRANSPARENT.tag)
                .addTag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag);

    }

}
