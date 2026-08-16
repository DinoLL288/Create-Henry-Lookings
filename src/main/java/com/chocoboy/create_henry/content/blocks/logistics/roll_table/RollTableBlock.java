package com.chocoboy.create_henry.content.blocks.logistics.roll_table;

import com.simibubi.create.AllShapes;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.chocoboy.create_henry.registry.HenryBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class RollTableBlock extends Block implements IWrenchable, IBE<RollTableBlockEntity> {
    public static final MapCodec<RollTableBlock> CODEC = simpleCodec(RollTableBlock::new);

    public RollTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        InteractionResult result = onBlockEntityUse(level, pos, be -> {
            if (!stack.isEmpty()) {
                DirectBeltInputBehaviour inputBehaviour = BlockEntityBehaviour.get(level, pos, DirectBeltInputBehaviour.TYPE);
                if (inputBehaviour == null) return InteractionResult.PASS;
                ItemStack remainder = inputBehaviour.handleInsertion(stack, player.getDirection(), false);
                player.setItemInHand(hand, remainder);
                return InteractionResult.SUCCESS;
            }
            ItemStack heldItemStack = be.getHeldItemStack();
            if (!level.isClientSide && !heldItemStack.isEmpty()) {
                player.getInventory().placeItemBackInInventory(heldItemStack);
                be.heldItem = null;
                be.notifyUpdate();
            }
            return InteractionResult.SUCCESS;
        });
        return switch (result) {
            case SUCCESS, CONSUME, CONSUME_PARTIAL -> ItemInteractionResult.SUCCESS;
            case FAIL -> ItemInteractionResult.FAIL;
            default -> ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        };
    }


    @Override
    @ParametersAreNonnullByDefault
    public void updateEntityAfterFallOn(BlockGetter level, Entity entity) {
        super.updateEntityAfterFallOn(level, entity);
        if (!(entity instanceof ItemEntity itemEntity)) return;
        if (!entity.isAlive()) return;
        if (entity.level().isClientSide) return;

        DirectBeltInputBehaviour inputBehaviour = BlockEntityBehaviour.get(level, entity.blockPosition(), DirectBeltInputBehaviour.TYPE);
        if (inputBehaviour == null) return;
        Vec3 deltaMovement = entity.getDeltaMovement().multiply(1, 0, 1).normalize();
        Direction nearest = Direction.getNearest(deltaMovement.x, deltaMovement.y, deltaMovement.z);
        ItemStack remainder = inputBehaviour.handleInsertion(itemEntity.getItem(), nearest, false);
        itemEntity.setItem(remainder);
        if (remainder.isEmpty()) itemEntity.discard();
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.hasBlockEntity() || state.getBlock() == newState.getBlock())
            return;
        withBlockEntityDo(worldIn, pos, be -> {
            ItemStack heldItemStack = be.getHeldItemStack();
            if (!heldItemStack.isEmpty()) Containers.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), heldItemStack);
        });
        worldIn.removeBlockEntity(pos);
    }

    @Override
    public void setPlacedBy(@NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, LivingEntity pPlacer, @NotNull ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        AdvancementBehaviour.setPlacedBy(pLevel, pPos, pPlacer);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return AllShapes.CASING_13PX.get(Direction.UP);
    }

    @Override
    public Class<RollTableBlockEntity> getBlockEntityClass() {
        return RollTableBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends RollTableBlockEntity> getBlockEntityType() {
        return HenryBlockEntityTypes.ROLL_TABLE.get();
    }

    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }
}
