package com.chocoboy.create_henry.content.blocks.kinetics.industrial_fan;

import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.fan.AirCurrent;
import com.simibubi.create.content.kinetics.fan.EncasedFanBlock;
import com.simibubi.create.content.logistics.chute.ChuteBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import com.chocoboy.create_henry.content.blocks.kinetics.industrial_fan.IAirCurrentSourceExtended;
import com.chocoboy.create_henry.registry.HenryBlocks;
import com.chocoboy.create_henry.registry.HenryTags;

import java.util.List;

@SuppressWarnings({"all"})
public class IndustrialFanBlockEntity extends GeneratingKineticBlockEntity implements IAirCurrentSourceExtended {

    protected boolean isGenerator;
    protected boolean updateGenerator;
    public AirCurrent airCurrent;
    protected int airCurrentUpdateCooldown;
    protected int entitySearchCooldown;
    protected boolean updateAirFlow;

    public IndustrialFanBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        isGenerator = false;
        updateGenerator = false;
        airCurrent = new IndustrialAirCurrent(this);
        updateAirFlow = true;
    }

    @Nullable
    @Override
    public AirCurrent getAirCurrent() {
        return airCurrent;
    }


    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        registerAwardables(behaviours, AllAdvancements.ENCASED_FAN, AllAdvancements.FAN_PROCESSING);
    }

    @Override
    protected void read(CompoundTag compound, net.minecraft.core.HolderLookup.Provider pRegistries, boolean clientPacket) {
        super.read(compound, pRegistries, clientPacket);
        if (!wasMoved)
            isGenerator = compound.getBoolean("Generating");
        if (clientPacket)
            airCurrent.rebuild();
    }

    @Override
    public void write(CompoundTag compound, net.minecraft.core.HolderLookup.Provider pRegistries, boolean clientPacket) {
        compound.putBoolean("Generating", isGenerator);
        super.write(compound, pRegistries, clientPacket);
    }

    @Override
    public float calculateAddedStressCapacity() {
        return isGenerator ? super.calculateAddedStressCapacity() : 0;
    }

    @Override
    public float calculateStressApplied() {
        return isGenerator ? 0 : super.calculateStressApplied();
    }

    @Override
    public float getGeneratedSpeed() {
        return isGenerator ? 64 : 0;
    }

    public void queueGeneratorUpdate() {
        updateGenerator = true;
    }

    @Nullable
    @Override
    public Level getAirCurrentWorld() {
        return level;
    }

    @Override
    public BlockPos getAirCurrentPos() {
        return worldPosition;
    }

    @Override
    public Direction getAirflowOriginSide() {
        return this.getBlockState()
                .getValue(EncasedFanBlock.FACING);
    }

    @Nullable
    @Override
    public Direction getAirFlowDirection() {
        float speed = getSpeed();
        if (speed == 0)
            return null;
        Direction facing = getBlockState().getValue(BlockStateProperties.FACING);
        speed = convertToDirection(speed, facing);
        return speed > 0 ? facing : facing.getOpposite();
    }

    @Override
    public void remove() {
        super.remove();
        updateChute();
    }

    @Override
    public boolean isSourceRemoved() {
        return remove;
    }

    @Override
    public void onSpeedChanged(float prevSpeed) {
        super.onSpeedChanged(prevSpeed);
        updateAirFlow = true;
        updateChute();
    }


    public void updateChute() {
        Direction direction = getBlockState().getValue(EncasedFanBlock.FACING);
        if (!direction.getAxis()
                .isVertical())
            return;
        BlockEntity poweredChute = level.getBlockEntity(worldPosition.relative(direction));
        if (!(poweredChute instanceof ChuteBlockEntity))
            return;
        ChuteBlockEntity chuteBE = (ChuteBlockEntity) poweredChute;
        if (direction == Direction.DOWN)
            chuteBE.updatePull();
        else
            chuteBE.updatePush(1);
    }

    public void blockInFrontChanged() {
        updateAirFlow = true;
    }



    @Override
    public void tick() {
        super.tick();

        boolean server = !level.isClientSide || isVirtual();

        if (server && airCurrentUpdateCooldown-- <= 0) {
            airCurrentUpdateCooldown = AllConfigs.server().kinetics.fanBlockCheckRate.get();
            updateAirFlow = true;
        }

        if (updateAirFlow) {
            updateAirFlow = false;
            airCurrent.rebuild();
            if (airCurrent.maxDistance > 0)
                award(AllAdvancements.ENCASED_FAN);
            sendData();
        }

        if (isGenerator || getSpeed() == 0)
            return;

        if (entitySearchCooldown-- <= 0) {
            entitySearchCooldown = 5;
            airCurrent.findEntities();
        }

        airCurrent.tick();
    }
}
