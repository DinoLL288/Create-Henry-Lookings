package com.chocoboy.create_henry.infrastructure.ponder;

import com.chocoboy.create_henry.infrastructure.ponder.scenes.FanScenes;
import com.chocoboy.create_henry.infrastructure.ponder.scenes.KineticsScenes;
import com.chocoboy.create_henry.infrastructure.ponder.scenes.ProcessingScenes;

import com.chocoboy.create_henry.registry.HenryBlocks;
import com.simibubi.create.infrastructure.ponder.AllCreatePonderTags;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings({"deprecation", "all"})
public class HenryPonderScenes {

    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?, ?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.forComponents(HenryBlocks.MULTIMETER)
                .addStoryBoard("multimeter", KineticsScenes::multimeter, HenryPonderTags.Henry);

        HELPER.forComponents(HenryBlocks.GOLDEN_MIXER)
                .addStoryBoard("golden_mixing", ProcessingScenes::goldenMixing, HenryPonderTags.Henry);

        HELPER.forComponents(HenryBlocks.KINETIC_MOTOR)
                .addStoryBoard("kinetic_motor", KineticsScenes::kineticMotor, HenryPonderTags.Henry);

        HELPER.forComponents(HenryBlocks.INDUSTRIAL_BRAKE)
                .addStoryBoard("industrial_brake", KineticsScenes::industrialBrake, HenryPonderTags.Henry);

        HELPER.forComponents(HenryBlocks.FURNACE_ENGINE)
                .addStoryBoard("furnace_engine", KineticsScenes::furnaceEngine, HenryPonderTags.Henry);

        HELPER.forComponents(HenryBlocks.POWERED_FLYWHEEL)
                .addStoryBoard("furnace_engine", KineticsScenes::flywheel, HenryPonderTags.Henry);

        HELPER.forComponents(HenryBlocks.HYDRAULIC_PRESS)
                .addStoryBoard("hydraulic_press", ProcessingScenes::bulkPressing, HenryPonderTags.Henry);

        HELPER.forComponents(HenryBlocks.ROLL_TABLE)
                .addStoryBoard("roll_table", ProcessingScenes::rollTable, HenryPonderTags.Henry);

        HELPER.forComponents(HenryBlocks.INDUSTRIAL_FAN)
                .addStoryBoard("industrial_fan/direction", FanScenes::direction, AllCreatePonderTags.KINETIC_APPLIANCES)
                .addStoryBoard("industrial_fan/processing", FanScenes::processing);

        HELPER.forComponents(HenryBlocks.SMART_HOPPER)
                .addStoryBoard("smart_hopper/smart_hopper", ProcessingScenes::smartHopper, HenryPonderTags.Henry);

        HELPER.forComponents(HenryBlocks.INVERSE_BOX)
                .addStoryBoard("inverse_box/inverse_box", KineticsScenes::inverseBox, HenryPonderTags.Henry);

        HELPER.forComponents(HenryBlocks.BORE_BLOCK)
                .addStoryBoard("bore_block/bore_block", KineticsScenes::boreBlock, HenryPonderTags.Henry);

        HELPER.forComponents(HenryBlocks.REDSTONE_DIVIDER)
                .addStoryBoard("redstone_divider/redstone_divider", KineticsScenes::redstoneDivider, HenryPonderTags.Henry);
    }
}