package com.chocoboy.create_henry.infrastructure.ponder;

import com.chocoboy.create_henry.HenryCreate;
import com.chocoboy.create_henry.registry.HenryBlocks;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class HenryPonderTags {

	public static final ResourceLocation
			Henry = loc("create_henry");

	private static ResourceLocation loc(String id) {
		return HenryCreate.asResource(id);
	}

	public static void register(PonderTagRegistrationHelper<ResourceLocation> helper) {
		PonderTagRegistrationHelper<RegistryEntry<?, ?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

		helper.registerTag(Henry)
				.addToIndex()
				.item(HenryBlocks.MULTIMETER.get(), true, false)
				.title("Create: Henry Lookings")
				.description("Blocks added by the Create: Henry Lookings mod")
				.register();

		HELPER.addToTag(Henry)
				.add(HenryBlocks.MULTIMETER)
				.add(HenryBlocks.GOLDEN_MIXER)
				.add(HenryBlocks.KINETIC_MOTOR)
				.add(HenryBlocks.INDUSTRIAL_BRAKE)
				.add(HenryBlocks.FURNACE_ENGINE)
				.add(HenryBlocks.POWERED_FLYWHEEL)
				.add(HenryBlocks.HYDRAULIC_PRESS)
				.add(HenryBlocks.ROLL_TABLE)
				.add(HenryBlocks.INDUSTRIAL_FAN)
				.add(HenryBlocks.SMART_HOPPER)
				.add(HenryBlocks.INVERSE_BOX)
				.add(HenryBlocks.BORE_BLOCK)
				.add(HenryBlocks.REDSTONE_DIVIDER)
				.add(HenryBlocks.FLUID_HATCH)
				.add(HenryBlocks.HENRY_BLOCK)
				.add(HenryBlocks.HYDRAULIC_CASING)
				.add(HenryBlocks.INDUSTRIAL_CASING)
				.add(HenryBlocks.RUBBER_CASING)
				.add(HenryBlocks.RAW_RUBBER_BLOCK)
				.add(HenryBlocks.RUBBER_BLOCK);
	}

}
