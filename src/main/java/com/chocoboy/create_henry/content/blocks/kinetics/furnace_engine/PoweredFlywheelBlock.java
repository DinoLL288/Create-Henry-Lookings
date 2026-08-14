package com.chocoboy.create_henry.content.blocks.kinetics.furnace_engine;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.createmod.catnip.data.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import com.chocoboy.create_henry.registry.HenryBlockEntityTypes;
import com.chocoboy.create_henry.registry.HenryBlocks;

public class PoweredFlywheelBlock extends RotatedPillarKineticBlock implements IBE<PoweredFlywheelBlockEntity> {
    public PoweredFlywheelBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public Class<PoweredFlywheelBlockEntity> getBlockEntityClass() {
        return PoweredFlywheelBlockEntity.class;
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AllShapes.LARGE_GEAR.get(pState.getValue(AXIS));
    }

    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    public BlockEntityType<? extends PoweredFlywheelBlockEntity> getBlockEntityType() {
        return HenryBlockEntityTypes.POWERED_FLYWHEEL.get();
    }

    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == this.getRotationAxis(state);
    }

    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(AXIS);
    }

    public float getParticleTargetRadius() {
        return 2.0F;
    }

    public float getParticleInitialRadius() {
        return 1.75F;
    }

    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return AllBlocks.FLYWHEEL.asStack();
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!stillValid(pState, pLevel, pPos))
            pLevel.setBlock(pPos, AllBlocks.FLYWHEEL.getDefaultState()
                    .setValue(RotatedPillarKineticBlock.AXIS, pState.getValue(AXIS)), 3);
    }
    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return stillValid(pState, pLevel, pPos);
    }

    public static boolean stillValid(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        for (Direction d : Iterate.directions) {
            if (d.getAxis() == pState.getValue(AXIS))
                continue;
            BlockPos enginePos = pPos.relative(d, 2);
            BlockState engineState = pLevel.getBlockState(enginePos);
            if (!(engineState.getBlock() instanceof FurnaceEngineBlock engine))
                continue;
            if (!FurnaceEngineBlock.getFlywheelPos(engineState, enginePos)
                    .equals(pPos))
                continue;
            if (FurnaceEngineBlock.isFlywheelValid(engineState, pState))
                return true;
        }
        return false;
    }

    public static BlockState getEquivalent(BlockState stateForPlacement, Direction.Axis preferredAxis) {
        return HenryBlocks.POWERED_FLYWHEEL.getDefaultState().setValue(AXIS, preferredAxis);
    }

}
