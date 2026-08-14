package com.chocoboy.create_henry.registry;

import com.chocoboy.create_henry.HenryCreate;
import com.chocoboy.create_henry.content.blocks.kinetics.multimeter.MultiMeterSpeedDisplaySource;
import com.chocoboy.create_henry.content.blocks.kinetics.multimeter.MultiMeterStressDisplaySource;
import com.simibubi.create.api.behaviour.display.DisplaySource;
import com.tterrag.registrate.util.entry.RegistryEntry;

public class HenryDisplaySources {

    private static final com.simibubi.create.foundation.data.CreateRegistrate REGISTRATE = HenryCreate.registrate();

    public static final RegistryEntry<DisplaySource, MultiMeterSpeedDisplaySource> MULTIMETER_SPEED =
            REGISTRATE.displaySource("multimeter_speed", MultiMeterSpeedDisplaySource::new).register();

    public static final RegistryEntry<DisplaySource, MultiMeterStressDisplaySource> MULTIMETER_STRESS =
            REGISTRATE.displaySource("multimeter_stress", MultiMeterStressDisplaySource::new).register();

    public static void register() {}
}
