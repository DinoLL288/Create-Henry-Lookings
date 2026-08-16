package com.chocoboy.create_henry.infrastructure.ponder.scenes;

import com.chocoboy.create_henry.content.blocks.kinetics.golden_mixer.GoldenMixerBlockEntity;
import com.chocoboy.create_henry.content.blocks.kinetics.hydraulic_press.HydraulicPressBlockEntity;
import com.chocoboy.create_henry.content.blocks.logistics.smart_hopper.SmartHopperBlockEntity;
import com.chocoboy.create_henry.registry.HenryBlocks;
import com.google.common.collect.ImmutableList;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.kinetics.press.PressingBehaviour;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import com.simibubi.create.foundation.ponder.element.BeltItemElement;
import net.createmod.catnip.data.IntAttached;
import net.createmod.catnip.nbt.NBTHelper;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.EntityElement;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.minecraft.world.entity.Entity;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

public class ProcessingScenes {

    public static void goldenMixing(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("golden_mixer", "Processing Items with the Golden Mixer");
        scene.configureBasePlate(0, 0, 5);
        scene.world().setBlock(util.grid().at(1, 1, 2), AllBlocks.ANDESITE_CASING.getDefaultState(), false);
        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.idle(5);
        scene.world().showSection(util.select().fromTo(3, 1, 4, 3, 1, 2), Direction.EAST);
        scene.idle(5);
        scene.world().showSection(util.select().position(1, 1, 2), Direction.DOWN);
        scene.idle(5);
        scene.world().showSection(util.select().position(1, 2, 2), Direction.DOWN);
        scene.idle(5);
        scene.world().showSection(util.select().position(1, 4, 2), Direction.DOWN);
        scene.idle(5);
        scene.world().showSection(util.select().position(1, 1, 3), Direction.DOWN);
        scene.idle(5);
        scene.world().showSection(util.select().fromTo(0, 1, 3, 0, 4, 3), Direction.WEST);
        scene.idle(5);
        scene.world().showSection(util.select().position(2, 1, 4), Direction.SOUTH);
        scene.idle(5);
        scene.world().showSection(util.select().fromTo(3, 1, 1, 1, 1, 1), Direction.DOWN);
        scene.idle(20);

        BlockPos basin = util.grid().at(1, 2, 2);
        BlockPos mixerPos = util.grid().at(1, 4, 2);
        Vec3 basinSide = util.vector().blockSurface(basin, Direction.WEST);

        scene.overlay().showText(70)
                .pointAt(basinSide)
                .placeNearTarget()
                .attachKeyFrame()
                .text("The Golden Mixer is an upgraded Mechanical Mixer that operates 2.5x times faster, processing recipes much faster");
        scene.idle(70);

        ItemStack blue = new ItemStack(Items.BLUE_DYE);
        ItemStack red = new ItemStack(Items.RED_DYE);
        ItemStack purple = new ItemStack(Items.PURPLE_DYE);

        scene.overlay().showControls(util.vector().topOf(basin), Pointing.LEFT, 30).withItem(blue);
        scene.overlay().showControls(util.vector().topOf(basin), Pointing.RIGHT, 30).withItem(red);
        scene.idle(30);
        scene.world().modifyBlockEntity(mixerPos, GoldenMixerBlockEntity.class, pte -> pte.startProcessingBasin());
        scene.world().createItemOnBeltLike(basin, Direction.UP, red);
        scene.world().createItemOnBeltLike(basin, Direction.UP, blue);
        scene.idle(80);
        scene.world().modifyBlockEntityNBT(util.select().position(basin), BasinBlockEntity.class, nbt -> {
            nbt.put("VisualizedItems",
                    NBTHelper.writeCompoundList(ImmutableList.of(IntAttached.with(1, purple)), ia -> (CompoundTag) ia.getValue()
                            .saveOptional(scene.world().getHolderLookupProvider())));
        });
        scene.idle(4);
        scene.world().createItemOnBelt(util.grid().at(1, 1, 1), Direction.UP, purple);
        scene.idle(30);

        scene.overlay().showText(80)
                .pointAt(basinSide)
                .placeNearTarget()
                .attachKeyFrame()
                .text("It handles Shapeless Crafting and Mixing recipes, just like the Mechanical Mixer");
        scene.idle(80);

        ItemStack waterBottle = new ItemStack(Items.POTION);
        ItemStack netherWart = new ItemStack(Items.NETHER_WART);

        scene.overlay().showControls(util.vector().topOf(basin), Pointing.LEFT, 40).withItem(waterBottle);
        scene.overlay().showControls(util.vector().topOf(basin), Pointing.RIGHT, 40).withItem(netherWart);
        scene.idle(40);

        scene.overlay().showText(80)
                .pointAt(basinSide)
                .placeNearTarget()
                .attachKeyFrame()
                .text("It can also brew Potions");
        scene.idle(80);

        scene.rotateCameraY(-30);
        scene.idle(10);
        scene.world().setBlock(util.grid().at(1, 1, 2), AllBlocks.BLAZE_BURNER.getDefaultState()
                .setValue(BlazeBurnerBlock.HEAT_LEVEL, HeatLevel.KINDLED), true);
        scene.idle(10);

        scene.overlay().showText(70)
                .pointAt(basinSide.subtract(0, 1, 0))
                .placeNearTarget()
                .text("Some recipes require the heat of a Blaze Burner placed below the Basin");
        scene.idle(40);

        scene.rotateCameraY(30);
        scene.idle(60);

        Vec3 filterPos = util.vector().of(1, 2.75f, 2.5f);
        scene.overlay().showFilterSlotInput(filterPos, Direction.WEST, 100);
        scene.overlay().showText(100)
                .pointAt(filterPos)
                .placeNearTarget()
                .attachKeyFrame()
                .text("The filter slot can be used to resolve conflicts when multiple recipes are compatible");
        scene.idle(80);
    }

    public static void bulkPressing(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("hydraulic_press", "Bulk Processing Items with the Hydraulic Press");
        scene.configureBasePlate(0, 0, 5);
        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.idle(5);

        ElementLink<WorldSectionElement> depot =
                scene.world().showIndependentSection(util.select().position(2, 1, 1), Direction.DOWN);
        scene.world().moveSection(depot, util.vector().of(0, 0, 1), 0);
        scene.idle(10);

        BlockPos pressPos = util.grid().at(2, 3, 2);
        BlockPos depotPos = util.grid().at(2, 1, 1);

        scene.world().setKineticSpeed(util.select().position(pressPos), 0);
        scene.world().showSection(util.select().position(pressPos), Direction.DOWN);
        scene.idle(10);

        scene.world().showSection(util.select().fromTo(2, 1, 3, 2, 1, 5), Direction.NORTH);
        scene.idle(3);
        scene.world().showSection(util.select().position(2, 2, 3), Direction.SOUTH);
        scene.idle(3);
        scene.world().showSection(util.select().position(2, 3, 3), Direction.NORTH);
        scene.world().setKineticSpeed(util.select().position(pressPos), -32);
        scene.effects().indicateSuccess(pressPos);
        scene.idle(10);

        scene.world().showSection(util.select().fromTo(1, 1, 3, 1, 1, 5), Direction.DOWN);
        scene.idle(3);
        scene.world().showSection(util.select().position(1, 2, 3), Direction.DOWN);
        scene.idle(3);
        scene.world().showSection(util.select().fromTo(1, 3, 2, 1, 3, 3), Direction.DOWN);
        scene.world().setKineticSpeed(util.select().position(1, 1, 4), -32);
        scene.idle(10);

        Vec3 pressSide = util.vector().blockSurface(pressPos, Direction.WEST);
        scene.overlay().showText(60)
                .pointAt(pressSide)
                .placeNearTarget()
                .attachKeyFrame()
                .text("The Hydraulic Press can process items provided beneath it");
        scene.idle(70);
        scene.overlay().showText(60)
                .pointAt(pressSide.subtract(0, 2, 0))
                .placeNearTarget()
                .text("Items can be dropped or placed on a Depot under the Press");
        scene.idle(50);

        ItemStack copper = new ItemStack(Items.COPPER_INGOT, 64);
        scene.world().createItemOnBeltLike(depotPos, Direction.NORTH, copper);
        Vec3 depotCenter = util.vector().centerOf(depotPos.south());
        scene.overlay().showControls(depotCenter, Pointing.UP, 30).withItem(copper);
        scene.idle(10);

        Class<HydraulicPressBlockEntity> type = HydraulicPressBlockEntity.class;
        scene.world().modifyBlockEntity(pressPos, type, pte -> pte.getPressingBehaviour()
                .start(PressingBehaviour.Mode.BELT));
        scene.idle(30);
        scene.world().modifyBlockEntity(pressPos, type, pte -> pte.getPressingBehaviour()
                .makePressingParticleEffect(depotCenter.add(0, 8 / 16f, 0), copper));
        scene.world().removeItemsFromBelt(depotPos);
        ItemStack sheet = new ItemStack(AllItems.COPPER_SHEET.asStack().getItem(), 64);
        scene.world().createItemOnBeltLike(depotPos, Direction.UP, sheet);
        scene.idle(10);
        scene.overlay().showControls(depotCenter, Pointing.UP, 50).withItem(sheet);
        scene.idle(60);

        scene.world().hideIndependentSection(depot, Direction.NORTH);
        scene.idle(5);
        scene.world().showSection(util.select().fromTo(0, 1, 3, 0, 2, 3), Direction.DOWN);
        scene.idle(10);
        scene.world().showSection(util.select().fromTo(4, 1, 2, 0, 2, 2), Direction.SOUTH);
        scene.idle(20);

        BlockPos beltPos = util.grid().at(0, 1, 2);
        scene.overlay().showText(40)
                .pointAt(util.vector().blockSurface(beltPos, Direction.WEST))
                .placeNearTarget()
                .attachKeyFrame()
                .text("When items are provided on a belt...");
        scene.idle(30);

        ElementLink<BeltItemElement> ingot = scene.world().createItemOnBelt(beltPos, Direction.SOUTH, copper);
        scene.idle(15);
        ElementLink<BeltItemElement> ingot2 = scene.world().createItemOnBelt(beltPos, Direction.SOUTH, copper);
        scene.idle(15);
        scene.world().stallBeltItem(ingot, true);
        scene.world().modifyBlockEntity(pressPos, type, pte -> pte.getPressingBehaviour()
                .start(PressingBehaviour.Mode.BELT));

        scene.overlay().showText(50)
                .pointAt(pressSide)
                .placeNearTarget()
                .attachKeyFrame()
                .text("The Press will hold and process them automatically");

        scene.idle(30);
        scene.world().modifyBlockEntity(pressPos, type, pte -> pte.getPressingBehaviour()
                .makePressingParticleEffect(depotCenter.add(0, 8 / 16f, 0), copper));
        scene.world().removeItemsFromBelt(pressPos.below(2));
        ingot = scene.world().createItemOnBelt(pressPos.below(2), Direction.UP, sheet);
        scene.world().stallBeltItem(ingot, true);
        scene.idle(15);
        scene.world().stallBeltItem(ingot, false);
        scene.idle(15);
        scene.world().stallBeltItem(ingot2, true);
        scene.world().modifyBlockEntity(pressPos, type, pte -> pte.getPressingBehaviour()
                .start(PressingBehaviour.Mode.BELT));
        scene.idle(30);
        scene.world().modifyBlockEntity(pressPos, type, pte -> pte.getPressingBehaviour()
                .makePressingParticleEffect(depotCenter.add(0, 8 / 16f, 0), copper));
        scene.world().removeItemsFromBelt(pressPos.below(2));
        ingot2 = scene.world().createItemOnBelt(pressPos.below(2), Direction.UP, sheet);
        scene.world().stallBeltItem(ingot2, true);
        scene.idle(15);
        scene.world().stallBeltItem(ingot2, false);

        scene.markAsFinished();
    }

    public static void rollTable(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("roll_table", "Transporting Items with the Roll Table");
        scene.configureBasePlate(3, 0, 4);

        BlockPos soloPos = util.grid().at(5, 1, 1);
        BlockPos centerPos = util.grid().at(3, 1, 3);

        scene.world().showSection(util.select().fromTo(4, 0, 0, 6, 0, 2), Direction.UP);
        scene.idle(5);
        scene.world().showSection(util.select().position(soloPos), Direction.DOWN);
        scene.idle(10);

        scene.overlay().showText(60)
                .pointAt(util.vector().topOf(soloPos))
                .placeNearTarget()
                .attachKeyFrame()
                .text("The Roll Table accepts items and rolls them across its surface to the opposite side");
        scene.idle(70);

        ItemStack henry = new ItemStack(HenryBlocks.HENRY_BLOCK.get());
        scene.overlay().showControls(util.vector().of(5.5, 2.5, 1.5), Pointing.DOWN, 40).withItem(henry);
        scene.idle(5);
        scene.world().createItemOnBeltLike(soloPos, Direction.SOUTH, henry);
        scene.idle(60);

        scene.overlay().showText(60)
                .pointAt(util.vector().blockSurface(soloPos, Direction.NORTH))
                .placeNearTarget()
                .attachKeyFrame()
                .text("Items exit on the opposite side and fall if nothing is there to receive them");
        scene.idle(70);

        scene.world().hideSection(util.select().position(soloPos), Direction.UP);
        scene.idle(3);
        scene.world().hideSection(util.select().fromTo(4, 0, 0, 6, 0, 2), Direction.DOWN);
        scene.idle(15);

        scene.configureBasePlate(0, 0, 7);

        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.idle(3);
        scene.world().showSection(util.select().position(centerPos), Direction.DOWN);
        scene.idle(3);
        scene.world().showSection(util.select().fromTo(3, 1, 0, 3, 1, 2), Direction.DOWN);
        scene.idle(3);
        scene.world().showSection(util.select().fromTo(3, 1, 4, 3, 1, 6), Direction.DOWN);
        scene.idle(3);
        scene.world().showSection(util.select().fromTo(0, 1, 3, 2, 1, 3), Direction.DOWN);
        scene.idle(3);
        scene.world().showSection(util.select().fromTo(4, 1, 3, 6, 1, 3), Direction.DOWN);
        scene.idle(10);

        scene.world().setKineticSpeed(util.select().fromTo(3, 1, 4, 3, 1, 6), -32);
        scene.world().setKineticSpeed(util.select().fromTo(3, 1, 0, 3, 1, 2), -32);
        scene.world().setKineticSpeed(util.select().fromTo(0, 1, 3, 2, 1, 3), -32);
        scene.world().setKineticSpeed(util.select().fromTo(4, 1, 3, 6, 1, 3), -32);

        scene.overlay().showText(60)
                .pointAt(util.vector().topOf(centerPos))
                .placeNearTarget()
                .attachKeyFrame()
                .text("Belts on any side can deliver items to the Roll Table and collect its output on the other side");
        scene.idle(70);

        scene.world().createItemOnBelt(util.grid().at(3, 1, 6), Direction.SOUTH, henry);
        scene.world().createItemOnBelt(util.grid().at(0, 1, 3), Direction.WEST, henry);
        scene.idle(80);

        scene.markAsFinished();
    }

    public static void smartHopper(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("smart_hopper", "Filtering Items with the Smart Hopper");
        scene.configureBasePlate(0, 0, 5);

        BlockPos hopperPos = util.grid().at(3, 1, 2);
        BlockPos outputPos = util.grid().at(2, 1, 2);
        BlockPos barrelPos = util.grid().at(3, 2, 2);
        BlockPos leverPos  = util.grid().at(3, 1, 1);
        Vec3 hopperWest  = util.vector().blockSurface(hopperPos, Direction.WEST);
        Vec3 hopperNorth = util.vector().blockSurface(hopperPos, Direction.NORTH);
        Vec3 aboveHopper = util.vector().topOf(hopperPos);
        Vec3 barrelTop   = util.vector().topOf(barrelPos);

        //region Setup
        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.idle(5);
        scene.world().showSection(util.select().position(outputPos), Direction.DOWN);
        scene.idle(5);
        scene.world().showSection(util.select().position(hopperPos), Direction.DOWN);
        scene.idle(5);
        scene.world().showSection(util.select().position(barrelPos), Direction.DOWN);
        scene.idle(15);
        //endregion

        ItemStack iron = new ItemStack(Items.IRON_INGOT);
        ItemStack dirt = new ItemStack(Items.DIRT);

        // Barrel contents: iron disappears before rejection demo so it's clear only dirt remains unfiltered
        scene.overlay().showControls(barrelTop.add(-0.4, 0.15, 0), Pointing.DOWN, 360).withItem(new ItemStack(Items.IRON_INGOT, 16));
        scene.overlay().showControls(barrelTop.add( 0.4, 0.15, 0), Pointing.DOWN, 500).withItem(new ItemStack(Items.DIRT, 8));

        //region Orientation
        scene.overlay().showText(80)
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(aboveHopper)
                .text("The Smart Hopper pulls items from above");
        for (int i = 0; i < 2; i++) {
            var dropped = scene.world().createItemEntity(aboveHopper, Vec3.ZERO, iron);
            scene.idle(35);
            scene.world().modifyEntity(dropped, Entity::discard);
            scene.idle(10);
        }
        scene.idle(15);

        scene.overlay().showText(60)
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(hopperWest)
                .text("And pushes them in the direction its spout faces");
        scene.overlay().showControls(util.vector().topOf(outputPos), Pointing.DOWN, 65).withItem(new ItemStack(Items.IRON_INGOT, 8));
        for (int i = 0; i < 3; i++) {
            scene.idle(15);
            scene.world().createItemOnBeltLike(hopperPos, Direction.WEST, iron);
        }
        scene.idle(20);
        //endregion

        //region Filtering
        Vec3 filter = hopperNorth.add(0, 3 / 16f, 0);

        scene.overlay().showFilterSlotInput(filter, Direction.NORTH, 80);
        scene.idle(10);
        scene.rotateCameraY(20);
        scene.overlay().showText(70)
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(filter)
                .text("Items in the filter slot specify what to extract and transfer");
        scene.idle(10);
        scene.world().setFilterData(util.select().position(hopperPos), SmartHopperBlockEntity.class, iron);
        scene.idle(60);
        scene.rotateCameraY(-20);

        scene.overlay().showText(80)
                .colored(PonderPalette.GREEN)
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(aboveHopper)
                .text("Matching items are transferred to the output");
        for (int i = 0; i < 3; i++) {
            var dropped = scene.world().createItemEntity(aboveHopper, Vec3.ZERO, iron);
            scene.idle(30);
            scene.world().modifyEntity(dropped, Entity::discard);
            scene.idle(10);
        }
        scene.idle(10);
        // Iron tooltip has now expired — only dirt remains visible on the barrel

        scene.overlay().showText(70)
                .colored(PonderPalette.RED)
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(aboveHopper)
                .text("Non-matching items are left on top");
        var rejected = scene.world().createItemEntity(aboveHopper, Vec3.ZERO, dirt);
        scene.idle(70);
        scene.world().modifyEntity(rejected, Entity::discard);
        scene.idle(15);

        scene.overlay().showFilterSlotInput(filter, Direction.NORTH, 65);
        scene.overlay().showControls(filter.add(0, 0.125, 0), Pointing.DOWN, 65).rightClick();
        scene.overlay().showText(60)
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(filter)
                .text("Use the value panel to specify the transferred stack size");
        scene.idle(65);
        //endregion

        //region Redstone
        scene.world().showSection(util.select().position(leverPos), Direction.DOWN);
        scene.idle(10);

        scene.overlay().showText(70)
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector().centerOf(leverPos))
                .text("A Redstone signal disables the Smart Hopper");
        scene.idle(30);
        scene.world().toggleRedstonePower(util.select().fromTo(leverPos, hopperPos));
        scene.effects().indicateRedstone(leverPos);

        var blocked = scene.world().createItemEntity(aboveHopper, Vec3.ZERO, iron);
        scene.idle(60);
        scene.world().modifyEntity(blocked, Entity::discard);
        scene.idle(15);

        scene.world().toggleRedstonePower(util.select().fromTo(leverPos, hopperPos));
        scene.markAsFinished();
        for (int i = 0; i < 2; i++) {
            var dropped = scene.world().createItemEntity(aboveHopper, Vec3.ZERO, iron);
            scene.idle(30);
            scene.world().modifyEntity(dropped, Entity::discard);
            scene.idle(10);
        }
        //endregion
    }

}