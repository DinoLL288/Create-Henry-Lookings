package com.chocoboy.create_henry.registry;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import com.chocoboy.create_henry.HenryCreate;

@SuppressWarnings({"all"})
public class HenryPartialModels {

	public static final PartialModel

		EMPTY = block("empty"),
		INDUSTRIAL_FAN_POWER = block("industrial_fan/cog"),
		INDUSTRIAL_FAN_INNER = block("industrial_fan/propeller"),
		HYDRAULIC_PRESS_HEAD = block("hydraulic_press/head"),
		ENGINE_PISTON = block("furnace_engine/piston"),
		ENGINE_LINKAGE = block("furnace_engine/linkage"),
		ENGINE_CONNECTOR = block("furnace_engine/shaft_connector"),
		GAUGE_SPEED_DIAL = block("gauge/speed_dial"),
		GAUGE_STRESS_DIAL = block("gauge/stress_dial"),
		GAUGE_HEAD = block("gauge/multimeter/head"),
		GOLDEN_MIXER_POLE = block("golden_mixer/pole"),
		GOLDEN_MIXER_HEAD = block("golden_mixer/head");

	private static PartialModel block(String path) {
		return PartialModel.of(HenryCreate.asResource("block/" + path));
	}


	public static void init() {
	}
}
