package com.chocoboy.create_henry.content.blocks.logistics.smart_hopper;

import com.chocoboy.create_henry.registry.HenryBlockEntityTypes;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SmartHopperBlock extends Block implements IWrenchable, IBE<SmartHopperBlockEntity> {
    public static final MapCodec<SmartHopperBlock> CODEC = simpleCodec(SmartHopperBlock::new);

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final DirectionProperty FACING = BlockStateProperties.FACING_HOPPER;
    public static final AABB SUCK_AABB = Block.box(0, 11, 0, 16, 32, 16).toAabbs().get(0);

    public SmartHopperBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.DOWN).setValue(POWERED, false));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    // region Block state

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED, FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        var direction = context.getClickedFace().getOpposite();
        return defaultBlockState().setValue(FACING, direction.getAxis() == Direction.Axis.Y ? Direction.DOWN : direction);
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    // endregion

    // region Interaction

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide) {
            if (level.getBlockEntity(pos) instanceof SmartHopperBlockEntity be) player.openMenu(be);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        var face = context.getClickedFace();
        if (face.getAxis() != Direction.Axis.Y) return InteractionResult.PASS;
        var level = context.getLevel();
        var pos = context.getClickedPos();
        var rotated = getRotatedBlockState(state, face);
        if (!rotated.canSurvive(level, pos)) return InteractionResult.PASS;
        KineticBlockEntity.switchToBlockState(level, pos, updateAfterWrenched(rotated, context));
        if (level.getBlockState(pos) != state) AllSoundEvents.WRENCH_ROTATE.playOnServer(level, pos, 1, level.random.nextFloat() + .5f);
        return InteractionResult.SUCCESS;
    }

    // endregion

    // region Redstone

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        checkPoweredState(level, pos, state);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!oldState.is(state.getBlock())) checkPoweredState(level, pos, state);
    }

    private void checkPoweredState(Level level, BlockPos pos, BlockState state) {
        var powered = level.hasNeighborSignal(pos);
        if (powered == state.getValue(POWERED)) return;
        level.setBlock(pos, state.setValue(POWERED, powered), 2);
        if (level.getBlockEntity(pos) instanceof SmartHopperBlockEntity be) {
            be.invVersionTracker.reset();
            be.notifyUpdate();
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof SmartHopperBlockEntity be
                ? AbstractContainerMenu.getRedstoneSignalFromContainer(be.inv) : 0;
    }

    // endregion

    // region Block entity

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == HenryBlockEntityTypes.SMART_HOPPER.get()
                ? (BlockEntityTicker<T>) (BlockEntityTicker<SmartHopperBlockEntity>) (l, p, s, be) -> be.tick()
                : null;
    }

    @Override
    public Class<SmartHopperBlockEntity> getBlockEntityClass() {
        return SmartHopperBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends SmartHopperBlockEntity> getBlockEntityType() {
        return HenryBlockEntityTypes.SMART_HOPPER.get();
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        IBE.onRemove(state, level, pos, newState);
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!(entity instanceof ItemEntity itemEntity)) return;
        if (!(level.getBlockEntity(pos) instanceof SmartHopperBlockEntity be)) return;
        if (be.inv == null) return;
        var stack = itemEntity.getItem();
        if (!stack.isEmpty() && entity.getBoundingBox().move(-pos.getX(), -pos.getY(), -pos.getZ()).intersects(SUCK_AABB)) {
            if (!be.cantAcceptItem(stack, state)) itemEntity.setItem(be.handleExtracting(stack, state));
        }
    }

    // endregion

    // region Shapes

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST  -> WEST_SHAPE;
            case EAST  -> EAST_SHAPE;
            default    -> DOWN_SHAPE;
        };
    }

    @Override
    public @NotNull VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return switch (state.getValue(FACING)) {
            case NORTH -> NORTH_INTERACTION_SHAPE;
            case SOUTH -> SOUTH_INTERACTION_SHAPE;
            case WEST  -> WEST_INTERACTION_SHAPE;
            case EAST  -> EAST_INTERACTION_SHAPE;
            default    -> DOWN_INTERACTION_SHAPE;
        };
    }

    private static final VoxelShape TOP          = Block.box(0, 10, 0, 16, 16, 16);
    private static final VoxelShape FUNNEL       = Block.box(3,  4, 3, 13, 10, 13);
    private static final VoxelShape CONVEX_BASE  = Shapes.or(FUNNEL, TOP);
    private static final VoxelShape INSIDE       = box(3, 10, 3, 13, 16, 13);
    private static final VoxelShape BASE         = Shapes.join(CONVEX_BASE, INSIDE, BooleanOp.ONLY_FIRST);

    private static final VoxelShape DOWN_SHAPE  = Shapes.or(BASE, Block.box( 5, 0,  5, 11,  4, 11));
    private static final VoxelShape EAST_SHAPE  = Shapes.or(BASE, Block.box(13, 4,  5, 18, 10, 11));
    private static final VoxelShape NORTH_SHAPE = Shapes.or(BASE, Block.box( 5, 4, -2, 11, 10,  2));
    private static final VoxelShape SOUTH_SHAPE = Shapes.or(BASE, Block.box( 5, 4, 14, 11, 10, 18));
    private static final VoxelShape WEST_SHAPE  = Shapes.or(BASE, Block.box(-2, 4,  5,  2, 10, 11));

    private static final VoxelShape DOWN_INTERACTION_SHAPE  = INSIDE;
    private static final VoxelShape EAST_INTERACTION_SHAPE  = Shapes.or(INSIDE, Block.box(12, 8,  6, 16, 10, 10));
    private static final VoxelShape NORTH_INTERACTION_SHAPE = Shapes.or(INSIDE, Block.box( 6, 8,  0, 10, 10,  4));
    private static final VoxelShape SOUTH_INTERACTION_SHAPE = Shapes.or(INSIDE, Block.box( 6, 8, 12, 10, 10, 16));
    private static final VoxelShape WEST_INTERACTION_SHAPE  = Shapes.or(INSIDE, Block.box( 0, 8,  6,  4, 10, 10));

    // endregion
}
