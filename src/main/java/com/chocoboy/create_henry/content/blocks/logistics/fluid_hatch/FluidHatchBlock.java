package com.chocoboy.create_henry.content.blocks.logistics.fluid_hatch;

import com.chocoboy.create_henry.registry.HenryBlockEntityTypes;
import com.chocoboy.create_henry.registry.HenrySoundEvents;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.fluids.tank.CreativeFluidTankBlockEntity;
import com.simibubi.create.content.fluids.transfer.GenericItemEmptying;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class FluidHatchBlock extends HorizontalDirectionalBlock implements IBE<FluidHatchBlockEntity>, IWrenchable, ProperWaterloggedBlock {

    public static final BooleanProperty OPEN = BooleanProperty.create("open");

    public FluidHatchBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(OPEN, false).setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(OPEN, FACING, WATERLOGGED));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        var state = super.getStateForPlacement(context);
        if (state == null) return null;
        if (context.getClickedFace().getAxis().isVertical()) return null;
        return withWater(state.setValue(FACING, context.getClickedFace().getOpposite()).setValue(OPEN, false), context);
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        return fluidState(state);
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        updateWater(level, state, pos);
        return state;
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        if (player instanceof FakePlayer) return InteractionResult.SUCCESS;

        var stack = player.getItemInHand(hand);
        var facing = state.getValue(FACING);
        var neighborPos = pos.relative(facing);
        var neighborBE = level.getBlockEntity(neighborPos);
        if (neighborBE == null) return InteractionResult.FAIL;

        var targetInv = level.getCapability(Capabilities.FluidHandler.BLOCK, neighborPos, facing.getOpposite());
        if (targetInv == null) return InteractionResult.FAIL;

        var filter = BlockEntityBehaviour.get(level, pos, FilteringBehaviour.TYPE);
        if (filter == null) return InteractionResult.FAIL;

        var inventory = player.getInventory();
        var depositItemInHand = !player.isShiftKeyDown();
        var anyInserted = false;

        for (int i = 0; i < inventory.items.size(); i++) {
            if (Inventory.isHotbarSlot(i) != depositItemInHand) continue;
            if (depositItemInHand && i != inventory.selected) continue;

            var item = inventory.getItem(i);
            if (item.isEmpty()) continue;
            if (!GenericItemEmptying.canItemBeEmptied(level, item)) continue;

            var emptyingResult = GenericItemEmptying.emptyItem(level, item, true);
            var fluidStack = emptyingResult.getFirst();

            if (fluidStack.isEmpty() || !filter.test(fluidStack)) continue;
            if (fluidStack.getAmount() != targetInv.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE)) continue;

            var copyOfItem = item.copy();
            emptyingResult = GenericItemEmptying.emptyItem(level, copyOfItem, false);
            targetInv.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);

            if (!player.isCreative() && !(neighborBE instanceof CreativeFluidTankBlockEntity)) {
                if (copyOfItem.isEmpty()) inventory.setItem(i, emptyingResult.getSecond());
                else {
                    inventory.setItem(i, copyOfItem);
                    inventory.placeItemBackInInventory(emptyingResult.getSecond());
                }
            }
            anyInserted = true;
        }

        if (!anyInserted) return InteractionResult.SUCCESS;

        HenrySoundEvents.FLUID_HATCH.playOnServer(level, pos);
        level.setBlockAndUpdate(pos, state.setValue(OPEN, true));
        level.scheduleTick(pos, this, 10);

        CreateLang.translate(depositItemInHand ? "item_hatch.deposit_item" : "item_hatch.deposit_inventory").sendStatus(player);
        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return AllShapes.ITEM_HATCH.get(state.getValue(FACING).getOpposite());
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(OPEN)) level.setBlockAndUpdate(pos, state.setValue(OPEN, false));
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        IBE.onRemove(state, level, pos, newState);
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public Class<FluidHatchBlockEntity> getBlockEntityClass() {
        return FluidHatchBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends FluidHatchBlockEntity> getBlockEntityType() {
        return HenryBlockEntityTypes.FLUID_HATCH.get();
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }
}
