package com.chocoboy.create_henry.content.blocks.kinetics.furnace_engine;

import java.util.List;

import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;

import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class PoweredFlywheelBlockEntity extends GeneratingKineticBlockEntity {

	public BlockPos enginePos;
	public float engineEfficiency;
	public int movementDirection;
	public int initialTicks;
	public Block capacityKey;
	LerpedFloat visualSpeed = LerpedFloat.linear();
	float angle;

	public PoweredFlywheelBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
		super(typeIn, pos, state);
		movementDirection = 1;
		initialTicks = 3;
	}

	@Override
	public void tick() {
		super.tick();
		if (initialTicks > 0)
			initialTicks--;
		if (this.level.isClientSide) {
			float targetSpeed = this.getSpeed();
			this.visualSpeed.updateChaseTarget(targetSpeed);
			this.visualSpeed.tickChaser();
			this.angle += this.visualSpeed.getValue() * 3.0F / 10.0F;
			this.angle %= 360.0F;
		} else {
			getBlockState().tick((ServerLevel) level,getBlockPos(),level.getRandom());
		}
	}

	public void update(BlockPos sourcePos, int direction, float efficiency) {
		BlockPos key = worldPosition.subtract(sourcePos);
		enginePos = key;
		float prev = engineEfficiency;
		engineEfficiency = efficiency;
		int prevDirection = this.movementDirection;
		if (Mth.equal(efficiency, prev) && prevDirection == direction)
			return;

		capacityKey = level.getBlockState(sourcePos)
			.getBlock();
		this.movementDirection = direction;
		updateGeneratedRotation();
	}

	public void remove(BlockPos sourcePos) {
		if (!isPoweredBy(sourcePos))
			return;

		enginePos = null;
		engineEfficiency = 0;
		movementDirection = 0;
		capacityKey = null;
		updateGeneratedRotation();
	}

	public boolean canBePoweredBy(BlockPos globalPos) {
		return initialTicks == 0 && (enginePos == null || isPoweredBy(globalPos));
	}

	public boolean isPoweredBy(BlockPos globalPos) {
		BlockPos key = worldPosition.subtract(globalPos);
		return key.equals(enginePos);
	}

	@Override
	protected void write(CompoundTag compound, HolderLookup.Provider pRegistries, boolean clientPacket) {
		compound.putInt("Direction", movementDirection);
		if (initialTicks > 0)
			compound.putInt("Warmup", initialTicks);
		if (enginePos != null && capacityKey != null) {
			compound.put("EnginePos", NbtUtils.writeBlockPos(enginePos));
			compound.putFloat("EnginePower", engineEfficiency);
			compound.putString("EngineType", BuiltInRegistries.BLOCK.getKey(capacityKey).toString());
		}
		super.write(compound, pRegistries, clientPacket);
	}

	@Override
	protected void read(CompoundTag compound, HolderLookup.Provider pRegistries, boolean clientPacket) {
		super.read(compound, pRegistries, clientPacket);
		movementDirection = compound.getInt("Direction");
		initialTicks = compound.getInt("Warmup");
		enginePos = null;
		engineEfficiency = 0;
		if (compound.contains("EnginePos")) {
			enginePos = NbtUtils.readBlockPos(compound, "EnginePos").orElse(null);
			engineEfficiency = compound.getFloat("EnginePower");
			capacityKey = BuiltInRegistries.BLOCK.getValue(ResourceLocation.parse(compound.getString("EngineType")));
	}

		if (clientPacket) {
			this.visualSpeed.chase(this.getGeneratedSpeed(), 0.015625, LerpedFloat.Chaser.EXP);
		}
	}

	@Override
	public float getGeneratedSpeed() {
		return getCombinedCapacity() > 0 ? movementDirection * 8 * getSpeedModifier() : 0;
	}

	private float getCombinedCapacity() {
		return capacityKey == null ? 0 : (float) (engineEfficiency * BlockStressValues.getCapacity(capacityKey));
	}

	private int getSpeedModifier() {
		return (int) (1 + (engineEfficiency >= 1 ? 3 : Math.min(2, Math.floor(engineEfficiency * 4))));
	}

	@Override
	public float calculateAddedStressCapacity() {
		float capacity = getCombinedCapacity() / getSpeedModifier();
		this.lastCapacityProvided = capacity;
		return capacity;
	}

	@Override
	public int getRotationAngleOffset(Axis axis) {
		int combinedCoords = axis.choose(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ());
		return super.getRotationAngleOffset(axis) + (combinedCoords % 2 == 0 ? 180 : 0);
	}

	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		return false;
	}

	public boolean addToEngineTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		return super.addToGoggleTooltip(tooltip, isPlayerSneaking);
	}

}
