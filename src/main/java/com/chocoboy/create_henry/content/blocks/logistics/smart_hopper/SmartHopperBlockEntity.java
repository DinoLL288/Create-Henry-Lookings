package com.chocoboy.create_henry.content.blocks.logistics.smart_hopper;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.inventory.VersionedInventoryTrackerBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.HopperMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class SmartHopperBlockEntity extends SmartBlockEntity implements MenuProvider {

    HopperInventory inv;
    FilteringBehaviour filtering;
    VersionedInventoryTrackerBehaviour invVersionTracker;

    public SmartHopperBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        inv = new HopperInventory(5, this);
        setLazyTickRate(8);
    }

    public @NotNull IItemHandler getItemHandler(@Nullable Direction side) {
        return inv;
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (level == null) return;

        var state = getBlockState();
        var facing = state.getValue(SmartHopperBlock.FACING);
        var clientSide = level.isClientSide && !isVirtual();

        if (inv != null) {
            if (!clientSide) {
                suckInItems(state, level);
                handleInput(grabCapability(Direction.UP), state);
            }
            var output = grabCapability(facing);
            if (handleOutput(output, state, true)) handleOutput(output, state, clientSide);
        }
    }

    private void handleInput(@Nullable IItemHandler source, BlockState state) {
        if (source == null) return;
        if (cantActivate(state)) return;
        if (invVersionTracker.stillWaiting(source)) return;

        Predicate<ItemStack> canAccept = s -> !cantAcceptItem(s, state);
        int count = getExtractionAmount();
        var mode = getExtractionMode();

        if (mode == ItemHelper.ExtractionCountMode.UPTO || !ItemHelper.extract(source, canAccept, mode, count, true).isEmpty()) {
            var extracted = ItemHelper.extract(source, canAccept, mode, count, false);
            if (!extracted.isEmpty()) {
                insertItem(extracted, false);
                return;
            }
        }
        invVersionTracker.awaitNewVersion(source);
    }

    private boolean handleOutput(@Nullable IItemHandler target, BlockState state, boolean simulate) {
        assert level != null;
        if (cantActivate(state)) return false;
        if (target == null) return true;
        if (level.isClientSide && !isVirtual()) return false;
        if (invVersionTracker.stillWaiting(target)) return false;

        var extracted = ItemHelper.extract(inv, s -> filtering.test(s), simulate);
        if (extracted.isEmpty()) return false;

        var remainder = ItemHandlerHelper.insertItemStacked(target, extracted, simulate);
        if (!simulate) insertItem(remainder, false);
        if (remainder.getCount() != extracted.getCount()) return true;

        invVersionTracker.awaitNewVersion(target);
        return true;
    }

    public void insertItem(ItemStack stack, boolean simulate) {
        ItemHandlerHelper.insertItemStacked(inv, stack, simulate);
        invVersionTracker.reset();
        assert level != null;
        if (!level.isClientSide) notifyUpdate();
    }

    private @Nullable IItemHandler grabCapability(Direction side) {
        if (level == null) return null;
        return level.getCapability(Capabilities.ItemHandler.BLOCK, worldPosition.relative(side), side.getOpposite());
    }

    protected boolean cantAcceptItem(ItemStack stack, BlockState state) {
        return ItemStack.isSameItemSameComponents(ItemHandlerHelper.insertItem(inv, stack.copy(), true), stack)
                || cantActivate(state)
                || !filtering.test(stack);
    }

    protected boolean cantActivate(BlockState state) {
        return state.hasProperty(SmartHopperBlock.POWERED) && state.getValue(SmartHopperBlock.POWERED);
    }

    protected int getExtractionAmount() {
        return filtering.isCountVisible() && !filtering.anyAmount() ? filtering.getAmount() : 64;
    }

    protected ItemHelper.ExtractionCountMode getExtractionMode() {
        return filtering.isCountVisible() && !filtering.anyAmount() && !filtering.upTo
                ? ItemHelper.ExtractionCountMode.EXACTLY
                : ItemHelper.ExtractionCountMode.UPTO;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(filtering = new FilteringBehaviour(this, new SmartHopperFilterSlotPositioning())
                .showCount().withCallback($ -> invVersionTracker.reset()));
        behaviours.add(invVersionTracker = new VersionedInventoryTrackerBehaviour(this));
    }

    @Override
    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(level, worldPosition, inv);
    }

    public boolean isEmpty() {
        return inv.isEmpty();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        inv.setChanged();
    }

    @Override
    protected void write(CompoundTag tag, net.minecraft.core.HolderLookup.Provider pRegistries, boolean clientPacket) {
        super.write(tag, pRegistries, clientPacket);
        inv.save(tag, pRegistries);
    }

    @Override
    protected void read(CompoundTag tag, net.minecraft.core.HolderLookup.Provider pRegistries, boolean clientPacket) {
        super.read(tag, pRegistries, clientPacket);
        inv.load(tag, pRegistries);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return getBlockState().getBlock().getName();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
        if (inv == null) inv = new HopperInventory(5, this);
        return new HopperMenu(id, inventory, inv);
    }

    public void suckInItems(BlockState state, Level level) {
        if (cantActivate(state)) return;
        var blockAbove = BlockPos.containing(getLevelX(), getLevelY() + 1.0, getLevelZ());
        var stateAbove = level.getBlockState(blockAbove);
        var pos = getBlockPos();
        if (!stateAbove.isCollisionShapeFullBlock(level, blockAbove) || stateAbove.is(Blocks.HOPPER)) {
            for (var entity : getItemsAtAndAbove(level)) {
                var stack = entity.getItem().copy();
                if (stack.isEmpty()) continue;
                if (!entity.getBoundingBox().move(-pos.getX(), -pos.getY(), -pos.getZ()).intersects(SmartHopperBlock.SUCK_AABB)) continue;
                if (cantAcceptItem(stack, state)) continue;
                entity.setItem(handleExtracting(stack, state));
            }
        }
    }

    public ItemStack handleExtracting(ItemStack stack, BlockState state) {
        if (stack.isEmpty()) return stack;
        int count = getExtractionAmount();
        var mode = getExtractionMode();
        int amountExtract = Math.min(count, stack.getCount());
        var extracted = extractFromStack(stack, state, mode, count, amountExtract);
        if (extracted.isEmpty()) return stack;
        if (mode == ItemHelper.ExtractionCountMode.UPTO || !extracted.isEmpty()) {
            var leftOver = ItemHandlerHelper.insertItemStacked(inv, extracted, false);
            return stack.copyWithCount(stack.getCount() - amountExtract + leftOver.getCount());
        }
        return stack;
    }

    public ItemStack extractFromStack(ItemStack source, BlockState state, ItemHelper.ExtractionCountMode mode, int amount, int amountExtract) {
        var toExtract = source.copyWithCount(amountExtract);
        if (toExtract.isEmpty() || cantAcceptItem(toExtract, state)) return ItemStack.EMPTY;
        if (mode == ItemHelper.ExtractionCountMode.EXACTLY && toExtract.getCount() < amount) return ItemStack.EMPTY;
        return toExtract;
    }

    public List<ItemEntity> getItemsAtAndAbove(Level level) {
        return level.getEntitiesOfClass(ItemEntity.class,
                SmartHopperBlock.SUCK_AABB.move(getLevelX() - 0.5, getLevelY() - 0.5, getLevelZ() - 0.5),
                EntitySelector.ENTITY_STILL_ALIVE);
    }

    public double getLevelX() { return worldPosition.getX() + 0.5; }
    public double getLevelY() { return worldPosition.getY() + 0.5; }
    public double getLevelZ() { return worldPosition.getZ() + 0.5; }
}
