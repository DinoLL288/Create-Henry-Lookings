package com.chocoboy.create_henry.content.blocks.kinetics.multimeter;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.theme.Color;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import com.chocoboy.create_henry.infrastructure.network.GaugeObservedPacket;
import net.createmod.catnip.platform.CatnipServices;

import java.util.List;

@SuppressWarnings({"deprecation", "all"})
public class MultiMeterBlockEntity extends KineticBlockEntity implements IHaveGoggleInformation {

    public float dialTargetSpeed;
    public float dialStateSpeed;
    public float prevDialStateSpeed;

    public float dialTargetStress;
    public float dialStateStress;
    public float prevDialStateStress;

    public int color;

    static BlockPos lastSent;

    public MultiMeterBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        registerAwardables(behaviours, AllAdvancements.STRESSOMETER, AllAdvancements.STRESSOMETER_MAXED);
    }

    @Override
    public void updateFromNetwork(float maxStress, float currentStress, int networkSize) {
        super.updateFromNetwork(maxStress, currentStress, networkSize);

        if (!IRotate.StressImpact.isEnabled())
            dialTargetStress = 0;
        else if (isOverStressed())
            dialTargetStress = 1.125f;
        else if (maxStress == 0)
            dialTargetStress = 0;
        else
            dialTargetStress = currentStress / maxStress;

        if (dialTargetStress > 0) {
            if (dialTargetStress < .5f)
                color = Color.mixColors(0x00FF00, 0xFFFF00, dialTargetStress * 2);
            else if (dialTargetStress < 1)
                color = Color.mixColors(0xFFFF00, 0xFF0000, dialTargetStress * 2 - 1);
            else
                color = 0xFF0000;
        }

        sendData();
        setChanged();
    }

    @Override
    public void onSpeedChanged(float prevSpeed) {
        super.onSpeedChanged(prevSpeed);
        float speed = Math.abs(getSpeed());

        if (getSpeed() == 0) {
            dialTargetSpeed = 0;
            setChanged();
            return;
        }

        dialTargetSpeed = getDialTarget(speed);
        setChanged();
        updateFromNetwork(capacity, stress, getOrCreateNetwork().getSize());
    }

    public static float getDialTarget(float speed) {
        speed = Math.abs(speed);
        float medium = AllConfigs.server().kinetics.mediumSpeed.get()
                .floatValue();
        float fast = AllConfigs.server().kinetics.fastSpeed.get()
                .floatValue();
        float max = AllConfigs.server().kinetics.maxRotationSpeed.get()
                .floatValue();
        float target;
        if (speed == 0)
            target = 0;
        else if (speed < medium)
            target = Mth.lerp(speed / medium, 0, .45f);
        else if (speed < fast)
            target = Mth.lerp((speed - medium) / (fast - medium), .45f, .75f);
        else
            target = Mth.lerp((speed - fast) / (max - fast), .75f, 1.125f);
        return target;
    }

    @Override
    public void write(CompoundTag compound, net.minecraft.core.HolderLookup.Provider pRegistries, boolean clientPacket) {
        compound.putFloat("SpeedValue", dialTargetSpeed);
        compound.putFloat("StressValue", dialTargetStress);
        compound.putInt("Color", color);
        super.write(compound, pRegistries, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, net.minecraft.core.HolderLookup.Provider pRegistries, boolean clientPacket) {
        dialTargetSpeed = compound.getFloat("SpeedValue");
        dialTargetStress = compound.getFloat("StressValue");
        color = compound.getInt("Color");
        super.read(compound, pRegistries, clientPacket);

        if (clientPacket && worldPosition != null && worldPosition.equals(lastSent))
            lastSent = null;
    }

    @Override
    public void tick() {
        super.tick();
        prevDialStateSpeed  = dialStateSpeed;
        prevDialStateStress = dialStateStress;
        dialStateSpeed  += (dialTargetSpeed  - dialStateSpeed)  * .125f;
        dialStateStress += (dialTargetStress - dialStateStress) * .125f;
        if (dialStateStress > 1 && level.random.nextFloat() < .5f)
            dialStateStress -= (dialStateStress - 1) * level.random.nextFloat();
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if (!IRotate.StressImpact.isEnabled())
            return false;

        CreateLang.translate("gui.gauge.info_header")
                .forGoggles(tooltip);

        CreateLang.translate("gui.speedometer.title")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);
        IRotate.SpeedLevel.getFormattedSpeedText(speed, isOverStressed())
                .forGoggles(tooltip);


        double capacity = getNetworkCapacity();
        double stressFraction = getNetworkStress() / (capacity == 0 ? 1 : capacity);

        CreateLang.translate("gui.stressometer.title")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);

        if (getTheoreticalSpeed() == 0)
            CreateLang.text(TooltipHelper.makeProgressBar(3, 0))
                    .translate("gui.stressometer.no_rotation")
                    .style(ChatFormatting.DARK_GRAY)
                    .forGoggles(tooltip);
        else {
            IRotate.StressImpact.getFormattedStressText(stressFraction)
                    .forGoggles(tooltip);
            CreateLang.translate("gui.stressometer.capacity")
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip);

            double remainingCapacity = capacity - getNetworkStress();

            LangBuilder su = CreateLang.translate("generic.unit.stress");
            LangBuilder stressTip = CreateLang.number(remainingCapacity)
                    .add(su)
                    .style(IRotate.StressImpact.of(stressFraction)
                            .getRelativeColor());

            if (remainingCapacity != capacity)
                stressTip.text(ChatFormatting.GRAY, " / ")
                        .add(CreateLang.number(capacity)
                                .add(su)
                                .style(ChatFormatting.DARK_GRAY));

            stressTip.forGoggles(tooltip, 1);
        }

        if (!worldPosition.equals(lastSent))
            CatnipServices.NETWORK.sendToServer(new GaugeObservedPacket(lastSent = worldPosition));

        return true;
    }


    public float getNetworkStress() {
        return stress;
    }

    public float getNetworkCapacity() {
        return capacity;
    }

    //UNUSED
    public void onObserved() {
        award(AllAdvancements.STRESSOMETER);
        if (Mth.equal(dialTargetStress, 1))
            award(AllAdvancements.STRESSOMETER_MAXED);
    }
}