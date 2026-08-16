package com.chocoboy.create_henry.infrastructure.ponder.scenes;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.logistics.depot.DepotBlockEntity;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import com.simibubi.create.foundation.ponder.element.BeltItemElement;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.EntityElement;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class FanScenes {

    public static void direction(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("industrial_fan_direction", "Powering the Industrial Fan");
        scene.configureBasePlate(0, 0, 6);

        BlockPos fanPos = util.grid().at(1, 1, 4);
        BlockPos sideCogPos = util.grid().at(2, 1, 4);

        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.idle(5);
        scene.world().showSection(util.select().fromTo(3, 1, 0, 3, 1, 5), Direction.DOWN);
        scene.idle(3);
        scene.world().showSection(util.select().position(fanPos), Direction.DOWN);
        scene.idle(3);
        scene.world().showSection(util.select().fromTo(1, 1, 5, 2, 1, 5), Direction.DOWN);
        scene.idle(10);

        scene.world().setKineticSpeed(util.select().fromTo(1, 1, 4, 3, 1, 5), -32);
        scene.effects().rotationDirectionIndicator(fanPos.south());

        scene.overlay().showText(60)
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(fanPos, Direction.SOUTH))
                .text("The Industrial Fan accepts rotational input from the back, like any kinetic block");
        scene.idle(70);

        scene.world().hideSection(util.select().position(1, 1, 5), Direction.UP);
        scene.idle(5);
        scene.world().showSection(util.select().position(sideCogPos), Direction.DOWN);
        scene.idle(10);

        scene.world().setKineticSpeed(util.select().position(sideCogPos), 32);
        scene.effects().rotationDirectionIndicator(sideCogPos);

        scene.overlay().showText(60)
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(fanPos, Direction.EAST))
                .text("As a cogwheel itself, it also accepts drive from a cogwheel meshing on its side");
        scene.idle(70);

        scene.markAsFinished();
    }

    public static void processing(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("industrial_fan_processing", "Processing Items with the Industrial Fan");
        scene.configureBasePlate(0, 0, 5);

        BlockPos fanPos = util.grid().at(4, 1, 2);
        BlockPos processPos = util.grid().at(3, 1, 2);
        Vec3 itemPos = util.vector().topOf(processPos.west(2));
        Vec3 particlePos = itemPos.add(0, -0.5, 0);
        ElementLink<WorldSectionElement> blockInFront;

        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.world().setBlock(util.grid().at(0, 0, 0), Blocks.WHITE_CONCRETE.defaultBlockState(), false);
        scene.idle(5);
        scene.world().showSection(util.select().position(4, 1, 2), Direction.DOWN);
        scene.idle(15);

        scene.world().setKineticSpeed(util.select().position(fanPos), -16);
        scene.idle(10);

        scene.overlay().showText(80)
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector().topOf(fanPos))
                .text("The Industrial Fan generates a longer airstream than a standard Encased Fan");
        scene.idle(90);

        // region Blasting
        blockInFront = scene.world().showIndependentSection(util.select().position(2, 1, 0), Direction.NORTH);
        scene.world().moveSection(blockInFront, util.vector().of(1, 0, 2), 0);
        scene.world().setBlock(processPos, Blocks.LAVA.defaultBlockState(), false);
        scene.idle(10);

        scene.overlay().showOutlineWithText(util.select().fromTo(processPos, processPos.west(1)), 80)
                .colored(PonderPalette.RED)
                .text("When passing through lava, the Air Flow becomes Heated");
        scene.idle(80);

        showCatalystDemo(scene, itemPos, particlePos,
                new ItemStack(Items.GOLD_ORE), new ItemStack(Items.GOLD_INGOT),
                ParticleTypes.LARGE_SMOKE, ParticleTypes.FLAME,
                "Items caught in the area will be smelted");

        // endregion

        // region Smoking
        scene.world().setBlock(processPos, Blocks.AIR.defaultBlockState(), false);
        scene.world().hideIndependentSection(blockInFront, Direction.NORTH);
        scene.idle(15);
        blockInFront = scene.world().showIndependentSection(util.select().position(3, 1, 0), Direction.NORTH);
        scene.world().moveSection(blockInFront, util.vector().of(0, 0, 2), 0);
        scene.world().setBlock(processPos, Blocks.CAMPFIRE.defaultBlockState(), false);
        scene.idle(10);

        scene.overlay().showOutlineWithText(util.select().fromTo(processPos.west(1), processPos.west(2)), 80)
                .colored(PonderPalette.BLACK)
                .attachKeyFrame()
                .text("A lit Campfire creates a Smoking zone for food-related recipes");
        scene.idle(80);

        showCatalystDemo(scene, itemPos, particlePos,
                new ItemStack(Items.PORKCHOP), new ItemStack(Items.COOKED_PORKCHOP),
                ParticleTypes.SMOKE, ParticleTypes.CAMPFIRE_COSY_SMOKE,
                "Items caught in the area will be smoked");

        // endregion

        // region Washing
        scene.world().setBlock(processPos, Blocks.AIR.defaultBlockState(), false);
        scene.world().hideIndependentSection(blockInFront, Direction.NORTH);
        scene.idle(15);
        blockInFront = scene.world().showIndependentSection(util.select().position(4, 1, 0), Direction.NORTH);
        scene.world().moveSection(blockInFront, util.vector().of(-1, 0, 2), 0);
        scene.world().setBlock(processPos, Blocks.WATER.defaultBlockState(), false);
        scene.idle(10);

        scene.overlay().showOutlineWithText(util.select().fromTo(processPos.west(1), processPos.west(2)), 80)
                .colored(PonderPalette.MEDIUM)
                .attachKeyFrame()
                .text("Air Flows passing through water create a Washing zone");
        scene.idle(80);

        showCatalystDemo(scene, itemPos, particlePos,
                new ItemStack(Items.RED_SAND, 16), new ItemStack(Items.GOLD_NUGGET, 16),
                ParticleTypes.BUBBLE, ParticleTypes.BUBBLE,
                "Items caught in the area will be washed");

        scene.idle(20);
        BlockPos depos = util.grid().at(3, 4, 2);
        ElementLink<WorldSectionElement> depot =
                scene.world().showIndependentSection(util.select().position(depos), Direction.DOWN);
        scene.world().moveSection(depot, util.vector().of(-1, -3, 0), 0);
        scene.world().createItemOnBeltLike(depos, Direction.NORTH, new ItemStack(Items.SAND));
        scene.idle(10);
        Vec3 washDepotTop = util.vector().topOf(2, 1, 2).add(0, 0.25, 0);
        scene.effects().emitParticles(washDepotTop, scene.effects().simpleParticleEmitter(ParticleTypes.SPIT, Vec3.ZERO), .5f, 30);
        scene.idle(30);
        scene.world().modifyBlockEntityNBT(util.select().position(depos), DepotBlockEntity.class,
                nbt -> nbt.put("HeldItem", new TransportedItemStack(new ItemStack(Items.CLAY_BALL)).serializeNBT(scene.world().getHolderLookupProvider())));
        scene.effects().emitParticles(washDepotTop, scene.effects().simpleParticleEmitter(ParticleTypes.SPIT, Vec3.ZERO), .5f, 30);

        // endregion

        // region Henry Processing Types
        scene.world().setBlock(processPos, Blocks.AIR.defaultBlockState(), false);
        scene.world().hideIndependentSection(blockInFront, Direction.NORTH);
        scene.idle(20);

        scene.overlay().showText(60)
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector().topOf(fanPos))
                .text("New processing types are also added");
        scene.idle(70);

        // region Sanding
        blockInFront = scene.world().showIndependentSection(util.select().position(1, 1, 0), Direction.NORTH);
        scene.world().moveSection(blockInFront, util.vector().of(2, 0, 2), 0);
        scene.world().setBlock(processPos, Blocks.SAND.defaultBlockState(), false);
        scene.idle(10);

        scene.overlay().showOutlineWithText(util.select().fromTo(processPos.west(1), processPos.west(2)), 80)
                .colored(PonderPalette.WHITE)
                .attachKeyFrame()
                .text("Sand performs all Sand Paper Polishing recipes, and more");
        scene.idle(80);

        showCatalystDemo(scene, itemPos, particlePos,
                AllItems.ROSE_QUARTZ.asStack(), AllItems.POLISHED_ROSE_QUARTZ.asStack(),
                ParticleTypes.CRIT, ParticleTypes.WHITE_ASH,
                "Items caught in the area will be sanded");

        // endregion

        // region Withering
        scene.world().setBlock(processPos, Blocks.AIR.defaultBlockState(), false);
        scene.world().hideIndependentSection(blockInFront, Direction.NORTH);
        scene.idle(15);
        scene.world().setBlock(util.grid().at(3, 0, 2), Blocks.DIRT.defaultBlockState(), false);
        blockInFront = scene.world().showIndependentSection(util.select().position(0, 1, 0), Direction.NORTH);
        scene.world().moveSection(blockInFront, util.vector().of(3, 0, 2), 0);
        scene.world().setBlock(processPos, Blocks.WITHER_ROSE.defaultBlockState(), false);
        scene.idle(10);

        scene.overlay().showOutlineWithText(util.select().fromTo(processPos.west(1), processPos.west(2)), 80)
                .colored(PonderPalette.BLACK)
                .attachKeyFrame()
                .text("A Wither Rose withers items passing through the airstream");
        scene.idle(80);

        showCatalystDemo(scene, itemPos, particlePos,
                new ItemStack(Items.CHARCOAL), new ItemStack(Items.COAL),
                ParticleTypes.SOUL, ParticleTypes.ASH,
                "Items caught in the area will be withered");

        // endregion

        // region Freezing
        scene.world().setBlock(processPos, Blocks.AIR.defaultBlockState(), false);
        scene.world().hideIndependentSection(blockInFront, Direction.NORTH);
        scene.idle(15);
        scene.world().setBlock(util.grid().at(3, 0, 2), Blocks.SNOW_BLOCK.defaultBlockState(), false);
        blockInFront = scene.world().showIndependentSection(util.select().position(2, 1, 4), Direction.SOUTH);
        scene.world().moveSection(blockInFront, util.vector().of(1, 0, -2), 0);
        scene.world().setBlock(processPos, Blocks.POWDER_SNOW.defaultBlockState(), false);
        scene.idle(10);

        scene.overlay().showOutlineWithText(util.select().fromTo(processPos.west(1), processPos.west(2)), 80)
                .colored(PonderPalette.MEDIUM)
                .attachKeyFrame()
                .text("Powder Snow freezes items as they pass through");
        scene.idle(80);

        showCatalystDemo(scene, itemPos, particlePos,
                new ItemStack(Items.ICE), new ItemStack(Items.PACKED_ICE),
                ParticleTypes.SNOWFLAKE, ParticleTypes.SNOWFLAKE,
                "Items caught in the area will be frozen");

        // endregion

        // region Seething
        scene.world().setBlock(processPos, Blocks.AIR.defaultBlockState(), false);
        scene.world().setBlock(util.grid().at(3, 0, 2), Blocks.AIR.defaultBlockState(), false);
        scene.world().hideIndependentSection(blockInFront, Direction.SOUTH);
        scene.idle(15);
        blockInFront = scene.world().showIndependentSection(util.select().position(3, 1, 4), Direction.SOUTH);
        scene.world().moveSection(blockInFront, util.vector().of(0, 0, -2), 0);
        scene.world().setBlock(processPos, AllBlocks.BLAZE_BURNER.get().defaultBlockState()
                .setValue(BlazeBurnerBlock.HEAT_LEVEL, HeatLevel.SEETHING), false);
        scene.idle(10);

        scene.overlay().showOutlineWithText(util.select().fromTo(processPos.west(1), processPos.west(2)), 80)
                .colored(PonderPalette.RED)
                .attachKeyFrame()
                .text("A Seething Blaze Burner unlocks powerful heat processing");
        scene.idle(80);

        showCatalystDemo(scene, itemPos, particlePos,
                new ItemStack(Items.ENDER_PEARL), new ItemStack(Items.ENDER_EYE),
                ParticleTypes.SOUL_FIRE_FLAME, ParticleTypes.SOUL_FIRE_FLAME,
                "Items caught in the area will be processed by intense heat");

        // endregion

        // region Dragon Breathing
        scene.world().setBlock(processPos, Blocks.AIR.defaultBlockState(), false);
        scene.world().hideIndependentSection(blockInFront, Direction.SOUTH);
        scene.idle(15);
        blockInFront = scene.world().showIndependentSection(util.select().position(4, 1, 4), Direction.SOUTH);
        scene.world().moveSection(blockInFront, util.vector().of(-1, 0, -2), 0);
        scene.world().setBlock(processPos, Blocks.DRAGON_HEAD.defaultBlockState(), false);
        scene.idle(10);

        scene.overlay().showOutlineWithText(util.select().fromTo(processPos.west(1), processPos.west(2)), 80)
                .colored(PonderPalette.GREEN)
                .attachKeyFrame()
                .text("A Dragon Head fills the stream with dragon's breath for End-related recipes");
        scene.idle(80);

        showCatalystDemo(scene, itemPos, particlePos,
                new ItemStack(Items.DEEPSLATE), new ItemStack(Items.END_STONE),
                ParticleTypes.DRAGON_BREATH, ParticleTypes.DRAGON_BREATH,
                "Items caught in the area will be infused with dragon's breath");

        // endregion
        // endregion

        // region Belt and Depot
        scene.world().setBlock(processPos, Blocks.AIR.defaultBlockState(), false);
        scene.world().hideIndependentSection(blockInFront, Direction.SOUTH);
        scene.idle(20);

        scene.world().hideSection(util.select().position(4, 1, 2), Direction.EAST);
        scene.idle(10);
        scene.world().showSection(util.select().fromTo(3, 1, 1, 4, 1, 1), Direction.DOWN); // depot casings
        scene.world().showSection(util.select().fromTo(3, 1, 3, 4, 1, 3), Direction.DOWN); // belt casings
        scene.world().showSection(util.select().position(4, 2, 1), Direction.DOWN);         // depot fan
        scene.world().showSection(util.select().position(4, 2, 3), Direction.DOWN);         // belt fan
        scene.idle(5);

        scene.world().showIndependentSection(util.select().fromTo(0, 1, 3, 2, 1, 3), Direction.DOWN);
        scene.idle(5);

        scene.world().showIndependentSection(util.select().position(2, 1, 1), Direction.DOWN);
        scene.idle(10);

        scene.world().showSection(util.select().position(3, 2, 1), Direction.UP);
        scene.world().showSection(util.select().position(3, 2, 3), Direction.UP);
        scene.world().setBlock(util.grid().at(3, 2, 1), Blocks.WATER.defaultBlockState(), false);
        scene.world().setBlock(util.grid().at(3, 2, 3), Blocks.WATER.defaultBlockState(), false);

        scene.world().setKineticSpeed(util.select().fromTo(0, 1, 3, 2, 1, 3), 8);
        scene.world().setKineticSpeed(util.select().position(4, 2, 1), -64);
        scene.world().setKineticSpeed(util.select().position(4, 2, 3), -64);
        scene.idle(10);

        ItemStack redSand = new ItemStack(Items.RED_SAND);
        ItemStack goldNugget = new ItemStack(Items.GOLD_NUGGET);
        Vec3 depotTop = util.vector().topOf(util.grid().at(2, 1, 1)).add(0, 0.25, 0);
        Vec3 beltTop  = util.vector().topOf(util.grid().at(2, 1, 3)).add(0, 0.25, 0);

        scene.overlay().showText(100)
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(depotTop)
                .text("Fan Processing can also be applied to Items on Depots and Belts");
        scene.idle(20);

        scene.world().createItemOnBeltLike(util.grid().at(2, 1, 1), Direction.NORTH, redSand);
        scene.effects().emitParticles(depotTop, scene.effects().simpleParticleEmitter(ParticleTypes.SPIT, Vec3.ZERO), 0.5f, 30);
        scene.idle(30);
        scene.world().modifyBlockEntityNBT(util.select().position(util.grid().at(2, 1, 1)), DepotBlockEntity.class,
                nbt -> nbt.put("HeldItem", new TransportedItemStack(goldNugget).serializeNBT(scene.world().getHolderLookupProvider())));
        scene.effects().emitParticles(depotTop, scene.effects().simpleParticleEmitter(ParticleTypes.SPIT, Vec3.ZERO), 0.5f, 30);
        scene.idle(40);

        ElementLink<BeltItemElement> beltItem = scene.world().createItemOnBelt(util.grid().at(1, 1, 3), Direction.EAST, redSand);
        scene.idle(60);
        scene.effects().emitParticles(beltTop, scene.effects().simpleParticleEmitter(ParticleTypes.SPIT, Vec3.ZERO), 0.5f, 25);
        scene.idle(25);
        scene.world().changeBeltItemTo(beltItem, goldNugget);
        scene.effects().emitParticles(beltTop, scene.effects().simpleParticleEmitter(ParticleTypes.SPIT, Vec3.ZERO), 0.5f, 25);
        scene.idle(60);
        // endregion

        scene.markAsFinished();
    }

    private static void showCatalystDemo(
            CreateSceneBuilder scene,
            Vec3 itemPos, Vec3 particlePos,
            ItemStack input, ItemStack output,
            ParticleOptions catalyst, ParticleOptions processing,
            String description) {
        ElementLink<EntityElement> item = scene.world().createItemEntity(itemPos, Vec3.ZERO, input);
        scene.idle(15);
        scene.overlay().showControls(itemPos, Pointing.DOWN, 40).withItem(input);
        scene.idle(20);
        scene.effects().emitParticles(particlePos, scene.effects().simpleParticleEmitter(catalyst, Vec3.ZERO), 1f, 110);
        scene.overlay().showText(100)
                .colored(PonderPalette.WHITE)
                .pointAt(itemPos)
                .placeNearTarget()
                .attachKeyFrame()
                .text(description);
        scene.effects().emitParticles(particlePos, scene.effects().simpleParticleEmitter(processing, Vec3.ZERO), 1, 110);
        scene.idle(110);
        scene.world().modifyEntity(item, e -> ((ItemEntity) e).setItem(output));
        scene.idle(40);
        scene.overlay().showControls(itemPos, Pointing.DOWN, 40).withItem(output);
        scene.idle(40);
        scene.world().modifyEntity(item, Entity::discard);
    }

}
