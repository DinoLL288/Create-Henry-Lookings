package com.chocoboy.create_henry.content.blocks.kinetics.furnace_engine;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.content.kinetics.flywheel.FlywheelBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.BlockHelper;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.placement.PlacementHelpers;
import net.createmod.catnip.placement.PlacementOffset;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.util.TriState;
import org.jetbrains.annotations.NotNull;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.ItemInteractionResult;
import com.chocoboy.create_henry.registry.HenryBlockEntityTypes;
import com.chocoboy.create_henry.registry.HenryBlocks;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Predicate;
import java.util.stream.Stream;

@SuppressWarnings({"all"})
@ParametersAreNonnullByDefault
public class FurnaceEngineBlock extends FaceAttachedHorizontalDirectionalBlock implements SimpleWaterloggedBlock, IWrenchable, IBE<FurnaceEngineBlockEntity> {

    private static final int placementHelperId = PlacementHelpers.register(
            new FurnaceEngineBlock.PlacementHelper()
    );

    public FurnaceEngineBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(FACE, AttachFace.FLOOR)
                        .setValue(FACING, Direction.NORTH)
                        .setValue(BlockStateProperties.WATERLOGGED, false)
        );

    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return canAttach(pLevel, pPos,
                getConnectedDirection(pState).getOpposite()) &&
                isValidPosition(pLevel,pPos.relative(getConnectedDirection(pState).getOpposite()),getConnectedDirection(pState));
    }

    public static boolean canAttach(LevelReader pReader, BlockPos pPos, Direction pDirection) {
        BlockPos blockpos = pPos.relative(pDirection);
        return pReader.getBlockState(blockpos).getBlock() instanceof AbstractFurnaceBlock;
    }

    public static boolean isValidPosition(LevelReader world, BlockPos pos, Direction facing) {
        for (Direction otherFacing : Iterate.directions) {
            if (otherFacing != facing) {
                BlockPos otherPos = pos.relative(otherFacing);
                BlockState otherState = world.getBlockState(otherPos);
                if (otherState.getBlock() instanceof FurnaceEngineBlock)
                    return false;
            }
        }
        return true;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(FACE, FACING, BlockStateProperties.WATERLOGGED));
    }

    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    protected MapCodec<? extends FurnaceEngineBlock> codec() {
        return Block.simpleCodec(FurnaceEngineBlock::new);
    }

    @Override
    public @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos,
                                                    Player player, InteractionHand hand, BlockHitResult ray) {
        IPlacementHelper placementHelper = PlacementHelpers.get(placementHelperId);
        if (placementHelper.matchesItem(stack))
            return placementHelper.getOffset(player, world, state, pos, ray)
                    .placeInWorld(world, (BlockItem) stack.getItem(), player, hand, ray);
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        FluidState ifluidstate = level.getFluidState(pos);
        BlockState state = super.getStateForPlacement(context);
        return state == null ? null : state.setValue(BlockStateProperties.WATERLOGGED, ifluidstate.getType() == Fluids.WATER);
    }

    @SubscribeEvent
    public static void usingFurnaceEngineOnFurnacePreventsGUI(PlayerInteractEvent.RightClickBlock event) {
        BlockItem blockItem;
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();
        if (item instanceof BlockItem) {
            blockItem = (BlockItem) item;
        } else {
            return;
        }
        if (blockItem.getBlock() != HenryBlocks.FURNACE_ENGINE.get())
            return;
        BlockState state = event.getLevel().getBlockState(event.getPos());
        if (state.getBlock() instanceof AbstractFurnaceBlock)
            event.setUseBlock(TriState.FALSE);
    }

    @Override
    protected boolean isPathfindable(BlockState pState, PathComputationType pType) {
        return false;
    }

    public static Direction getFacing(BlockState sideState) {
        return getConnectedDirection(sideState);
    }

    public static BlockPos getFlywheelPos(BlockState sideState, BlockPos pos) {
        return pos.relative(getConnectedDirection(sideState), 2);
    }

    public static boolean isFlywheelValid(BlockState state, BlockState shaft) {
        return (AllBlocks.FLYWHEEL.has(shaft) || HenryBlocks.POWERED_FLYWHEEL.has(shaft)) && shaft.getValue(RotatedPillarKineticBlock.AXIS) != getFacing(state).getAxis();
    }

    @Override
    public Class<FurnaceEngineBlockEntity> getBlockEntityClass() {
        return FurnaceEngineBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends FurnaceEngineBlockEntity> getBlockEntityType() {
        return HenryBlockEntityTypes.FURNACE_ENGINE.get();
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        // FLOOR attachment, facing Z-axis (north/south)
        VoxelShape up_z = Stream.of(
                Block.box(1.5, 3, 3, 14.5, 14, 13),       // Main body
                Block.box(0, 6, 5, 1.5, 12, 11),          // Left boxe
                Block.box(-1, -5, -1, 17, 3, 17),         // Support brace
                Block.box(14.5, 6, 5, 16, 12, 11)         // Right boxe
        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

        // FLOOR attachment, facing X-axis (east/west)
        VoxelShape up_x = Stream.of(
                Block.box(  3,  3,  1.5,  13, 14, 14.5),  // Main body
                Block.box(  5,  6, 14.5,  11, 12,   16),  // Left boxe
                Block.box( -1, -5,  -1,  17,  3,   17),   // Support brace
                Block.box(  5,  6,   0,  11, 12,  1.5)    // Right boxe
        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

        // CEILING attachment, facing Z-axis (north/south)
        VoxelShape down_z = Stream.of(
                Block.box( 1.5, 2,  3,   14.5, 13, 13),   // Main body
                Block.box(   0, 4,  5,    1.5, 10, 11),   // Right boxe
                Block.box(  -1,13, -1,   17,  21, 17),    // Support brace
                Block.box(14.5, 4,  5,   16,  10, 11)     // Left boxe
        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

        // CEILING attachment, facing X-axis (east/west)
        VoxelShape down_x = Stream.of(
                Block.box(  3,  2,  1.5,  13, 13, 14.5),    // Main body
                Block.box(  5,  4, 14.5,  11, 10, 16),      // Left boxe
                Block.box( -1, 13,   -1,  17, 21, 17),      // Back support
                Block.box(  5,  4,   0,  11, 10,  1.5)      // Right boxe
        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

        // WALL attachment, facing NORTH
        VoxelShape wall_north = Stream.of(
                Block.box(1.5, 3, 2, 14.5, 13, 13),     // Main body
                Block.box(0, 5, 4, 1.5, 11, 10),        // Left boxe
                Block.box(-1, -1, 13, 17, 17, 21),      // Back support
                Block.box(14.5, 5, 4, 16, 11, 10)       // Right boxe
        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

        // WALL attachment, facing SOUTH
        VoxelShape wall_south = Stream.of(
                Block.box(  1.5, 3,   3,   14.5, 13, 14),   // Main body
                Block.box( 14.5, 5,   6,   16,   11, 12),   // Left boxe
                Block.box(  -1, -1, -5,   17,   17,  3),    // Back support
                Block.box(    0, 5,   6,    1.5, 11, 12)    // Right boxe
        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

        // WALL attachment, facing EAST
        VoxelShape wall_east = Stream.of(
                Block.box(3, 3, 1.5, 14, 13, 14.5),       // Main body
                Block.box(6, 5, 0, 12, 11, 1.5),          // Left boxe
                Block.box(-5, -1, -1, 3, 17, 17),         // Back support
                Block.box(6, 5, 14.5, 12, 11, 16)         // Right boxe
        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

        // WALL attachment, facing WEST
        VoxelShape wall_west = Stream.of(
                Block.box(2,  3,  1.5, 13, 13, 14.5),   // Main body
                Block.box(4,  5, 14.5, 10, 11, 16),     // Left box
                Block.box(13, -1,  -1, 21, 17, 17),     // Back support
                Block.box(4,  5,   0, 10, 11,  1.5)     // Right box
        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

        // Determine attachment face (ceiling, floor, or wall) and block facing direction
        AttachFace face = pState.getValue(FACE);
        Direction direction = pState.getValue(FACING);

        // Return the matching shape based on attachment + facing
        return face == AttachFace.CEILING
                ? (direction.getAxis() == Direction.Axis.X ? down_x : down_z)
                : face == AttachFace.FLOOR
                ? (direction.getAxis() == Direction.Axis.X ? up_x   : up_z)
                : direction == Direction.NORTH ? wall_north
                : direction == Direction.SOUTH ? wall_south
                : direction == Direction.EAST  ? wall_east
                : wall_west;
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        BlockPos shaftPos = getFlywheelPos(pState, pPos);
        BlockState shaftState = pLevel.getBlockState(shaftPos);
        if (isFlywheelValid(pState, shaftState))
            pLevel.setBlock(shaftPos, PoweredFlywheelBlock.getEquivalent(shaftState,shaftState.getValue(RotatedPillarKineticBlock.AXIS)), 3);
    }

    @MethodsReturnNonnullByDefault
    private static class PlacementHelper implements IPlacementHelper {
        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return AllBlocks.FLYWHEEL::isIn;
        }

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return s -> s.getBlock() instanceof FurnaceEngineBlock;
        }

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos,
                                         BlockHitResult ray) {
            BlockPos shaftPos = FurnaceEngineBlock.getFlywheelPos(state, pos);
            BlockState shaft = AllBlocks.FLYWHEEL.getDefaultState();
            for (Direction direction : Direction.orderedByNearest(player)) {
                shaft = shaft.setValue(FlywheelBlock.AXIS, direction.getAxis());
                if (isFlywheelValid(state, shaft))
                    break;
            }

            BlockState newState = world.getBlockState(shaftPos);
            if (!newState.canBeReplaced())
                return PlacementOffset.fail();

            Direction.Axis axis = shaft.getValue(FlywheelBlock.AXIS);
            return PlacementOffset.success(shaftPos,
                    s -> BlockHelper.copyProperties(s, HenryBlocks.POWERED_FLYWHEEL.getDefaultState())
                            .setValue(PoweredFlywheelBlock.AXIS, axis));
        }
    }
}
