package com.chocoboy.create_henry.registry;

import java.util.function.BiConsumer;

@SuppressWarnings({"unused"})
public class HenryLangPartial {
    public static void provideLang(BiConsumer<String, String> consumer) {
        consume(consumer, "block.create_henry.fluid_hatch.tooltip.summary", "_Quickly empties_ your _fluid containers_ into nearby storage.");
        consume(consumer, "block.create_henry.fluid_hatch.tooltip.condition1", "When R-Clicked");
        consume(consumer, "block.create_henry.fluid_hatch.tooltip.behaviour1", "_Empties_ your _held fluid container_ into the _tank_ it faces. _Sneak-click_ to _empty_ _everything_ except Hotbar items.");
        consume(consumer, "create_henry.recipe.fan_sanding.fan", "Fan behind Sand");
        consume(consumer, "create_henry.recipe.fan_freezing.fan", "Fan behind Powdered Snow");
        consume(consumer, "create_henry.recipe.fan_seething.fan", "Fan behind Super Heated Blaze Burner");
        consume(consumer, "create_henry.recipe.fan_withering.fan", "Fan behind Wither Rose");
        consume(consumer, "create_henry.recipe.fan_dragon_breathing.fan", "Fan behind Dragon Head");
        consume(consumer, "create_henry.recipe.fan_sanding", "Bulk Sanding");
        consume(consumer, "create_henry.recipe.fan_freezing", "Bulk Freezing");
        consume(consumer, "create_henry.recipe.fan_seething", "Bulk Seething");
        consume(consumer, "create_henry.recipe.fan_withering", "Bulk Withering");
        consume(consumer, "create_henry.recipe.fan_dragon_breathing", "Bulk Dragon Breathing");
        consume(consumer, "create_henry.recipe.hydraulic_compacting", "Hydraulic Compacting");
        consume(consumer, "create_henry.recipe.golden_mixing", "Golden Mixer Mixing");
        consume(consumer, "create_henry.recipe.golden_auto_shapeless", "Golden Mixer Automatic Shapeless Crafting");
        consume(consumer, "create_henry.recipe.golden_auto_brewing", "Golden Mixer Automatic Brewing");
        consume(consumer, "itemGroup.create_henry.base", "Create: Henry");
        // Multimeter display link: speed source
        consume(consumer, "create_henry.display_source.multimeter_speed", "Multimeter Speed (RPM)");
        consume(consumer, "create_henry.display_source.multimeter_speed.absolute", "Ignore Direction");
        consume(consumer, "create_henry.display_source.multimeter_speed.directional", "Include Direction");
        // Multimeter display link: stress source
        consume(consumer, "create_henry.display_source.multimeter_stress", "Multimeter Network Stress");
        consume(consumer, "create_henry.display_source.multimeter_stress.display", "Displayed Info");
        consume(consumer, "create_henry.display_source.multimeter_stress.percent", "Percentage of Capacity");
        consume(consumer, "create_henry.display_source.multimeter_stress.progress_bar", "Progress Bar");
        consume(consumer, "create_henry.display_source.multimeter_stress.current", "Current Stress (SU Used)");
        consume(consumer, "create_henry.display_source.multimeter_stress.remaining", "Remaining SU");
        consume(consumer, "create_henry.display_source.multimeter_stress.max", "Total SU Capacity");

        // EMI/recipe-viewer display names for Henry's tags (EMI key format: tag.<registry>.<namespace>.<path>).
        // Fluid tags (conventional namespace).
        consume(consumer, "tag.fluid.c.sap", "Sap");
        consume(consumer, "tag.fluid.c.vanilla", "Vanilla Milkshake");
        consume(consumer, "tag.fluid.c.strawberry", "Strawberry Milkshake");
        consume(consumer, "tag.fluid.c.glowberry", "Glowberry Milkshake");
        consume(consumer, "tag.fluid.c.pumpkin", "Pumpkin Milkshake");
        // Bucket item tags (conventional namespace).
        consume(consumer, "tag.item.c.buckets.sap", "Sap Bucket");
        consume(consumer, "tag.item.c.buckets.chocolate_milkshake", "Chocolate Milkshake Bucket");
        consume(consumer, "tag.item.c.buckets.vanilla_milkshake", "Vanilla Milkshake Bucket");
        consume(consumer, "tag.item.c.buckets.strawberry_milkshake", "Strawberry Milkshake Bucket");
        consume(consumer, "tag.item.c.buckets.glowberry_milkshake", "Glowberry Milkshake Bucket");
        consume(consumer, "tag.item.c.buckets.pumpkin_milkshake", "Pumpkin Milkshake Bucket");
        // Material item tags (conventional namespace).
        consume(consumer, "tag.item.c.crude_rubbers", "Crude Rubber");
        consume(consumer, "tag.item.c.raw_rubbers", "Raw Rubber");
        consume(consumer, "tag.item.c.rubbers", "Rubber");
        consume(consumer, "tag.item.c.nuggets.coal", "Coal Nuggets");
        consume(consumer, "tag.item.c.nuggets.lapis", "Lapis Nuggets");
        consume(consumer, "tag.item.c.dusts.obsidian", "Obsidian Dust");
        // Fan-processing item tags (mod namespace).
        consume(consumer, "tag.item.create_henry.freezable", "Freezable");
        consume(consumer, "tag.item.create_henry.meat", "Meat");
        consume(consumer, "tag.item.create_henry.sandable", "Sandable");
        consume(consumer, "tag.item.create_henry.seethable", "Seethable");
        consume(consumer, "tag.item.create_henry.witherable", "Witherable");
    }

    private static void consume(BiConsumer<String, String> consumer, String key, String enUS) {
        consumer.accept(key, enUS);
    }
}
