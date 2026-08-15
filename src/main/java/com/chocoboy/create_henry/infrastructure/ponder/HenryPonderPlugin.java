package com.chocoboy.create_henry.infrastructure.ponder;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.crank.ValveHandleBlock;
import com.simibubi.create.content.logistics.packagePort.postbox.PostboxBlock;
import com.simibubi.create.content.logistics.tableCloth.TableClothBlock;

import net.createmod.ponder.api.level.PonderLevel;
import net.createmod.ponder.api.registration.IndexExclusionHelper;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.createmod.ponder.api.registration.SharedTextRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import com.chocoboy.create_henry.HenryCreate;

public class HenryPonderPlugin implements PonderPlugin {

    @Override
    public String getModId() {
        return HenryCreate.MOD_ID;
    }

    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        HenryPonderTags.register(helper);
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        HenryPonderScenes.register(helper);
    }

    @Override
    public void registerSharedText(SharedTextRegistrationHelper helper) {
        helper.registerSharedText("rpm8", "8 RPM");
        helper.registerSharedText("rpm16", "16 RPM");
        helper.registerSharedText("rpm16_source", "Source: 16 RPM");
        helper.registerSharedText("rpm32", "32 RPM");

        helper.registerSharedText("movement_anchors", "With the help of Super Glue, larger structures can be moved.");
        helper.registerSharedText("behaviour_modify_value_panel", "This behaviour can be modified using the value panel");
        helper.registerSharedText("storage_on_contraption", "Inventories attached to the Contraption will pick up their drops automatically");
    }

    @Override
    public void onPonderLevelRestore(PonderLevel ponderLevel) {
        HenryPonderWorldBlockEntityFix.fixControllerBlockEntities(ponderLevel);
    }

    @Override
    public void indexExclusions(IndexExclusionHelper helper) {
        helper.excludeBlockVariants(ValveHandleBlock.class, AllBlocks.COPPER_VALVE_HANDLE.get());
        helper.excludeBlockVariants(PostboxBlock.class, AllBlocks.PACKAGE_POSTBOXES.get(DyeColor.WHITE).get());
        helper.excludeBlockVariants(TableClothBlock.class, AllBlocks.TABLE_CLOTHS.get(DyeColor.WHITE).get());
    }
}