package com.chocoboy.create_henry.registry;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;

import com.chocoboy.create_henry.HenryCreate;

public class HenryCreativeModeTabs {

    public static final RegistryEntry<CreativeModeTab, CreativeModeTab> BASE_CREATIVE_TAB = registrate()
            .defaultCreativeTab(HenryCreate.MOD_ID, builder -> builder
                    .title(Component.translatable("itemGroup.create_henry"))
                    .icon(() -> new ItemStack(HenryBlocks.INDUSTRIAL_FAN.get())))
            .register();

    public static void register(IEventBus modEventBus) {
        // Tabs are auto-registered via Registrate.defaultCreativeTab()
    }

    private static Registrate registrate() {
        return HenryCreate.registrate();
    }
}