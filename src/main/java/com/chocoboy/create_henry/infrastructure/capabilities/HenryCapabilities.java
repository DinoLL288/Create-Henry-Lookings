package com.chocoboy.create_henry.infrastructure.capabilities;

import com.chocoboy.create_henry.content.blocks.kinetics.hydraulic_press.HydraulicPressBlockEntity;
import com.chocoboy.create_henry.content.blocks.logistics.roll_table.RollTableBlockEntity;
import com.chocoboy.create_henry.content.blocks.logistics.smart_hopper.SmartHopperBlockEntity;
import com.chocoboy.create_henry.registry.HenryBlockEntityTypes;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class HenryCapabilities {

    public static void register(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(HenryBlockEntityTypes.HYDRAULIC_PRESS.get(), Capabilities.FluidHandler.BLOCK,
                HydraulicPressBlockEntity::getFluidHandler);
        event.registerBlockEntity(HenryBlockEntityTypes.ROLL_TABLE.get(), Capabilities.ItemHandler.BLOCK,
                RollTableBlockEntity::getItemHandler);
        event.registerBlockEntity(HenryBlockEntityTypes.SMART_HOPPER.get(), Capabilities.ItemHandler.BLOCK,
                SmartHopperBlockEntity::getItemHandler);
    }
}
