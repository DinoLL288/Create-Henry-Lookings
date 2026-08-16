package com.chocoboy.create_henry;

import net.createmod.ponder.foundation.PonderIndex;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import com.chocoboy.create_henry.infrastructure.ponder.HenryPonderPlugin;
import com.chocoboy.create_henry.registry.HenryPartialModels;
import com.chocoboy.create_henry.registry.HenryParticleTypes;

public class HenryClient {

    public static void onCtorClient(IEventBus modEventBus) {
        modEventBus.addListener(HenryClient::clientInit);
        modEventBus.addListener(HenryParticleTypes::registerFactories);
    }

    public static void clientInit(final FMLClientSetupEvent event) {
        HenryPartialModels.init();
        PonderIndex.addPlugin(new HenryPonderPlugin());
    }
}
