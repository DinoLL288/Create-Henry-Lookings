package com.chocoboy.create_henry.infrastructure.capabilities;

import com.chocoboy.create_henry.content.blocks.kinetics.hydraulic_press.HydraulicPressBlockEntity;
import com.chocoboy.create_henry.content.blocks.logistics.roll_table.RollTableBlockEntity;
import com.chocoboy.create_henry.content.blocks.logistics.smart_hopper.SmartHopperBlockEntity;
import com.chocoboy.create_henry.registry.HenryBlockEntityTypes;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class HenryCapabilities {

    public static void register(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, HenryBlockEntityTypes.HYDRAULIC_PRESS.get(),
                HydraulicPressBlockEntity::getFluidHandler);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, HenryBlockEntityTypes.ROLL_TABLE.get(),
                RollTableBlockEntity::getItemHandler);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, HenryBlockEntityTypes.SMART_HOPPER.get(),
                SmartHopperBlockEntity::getItemHandler);
    }
}
