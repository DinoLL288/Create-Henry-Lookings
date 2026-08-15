package com.chocoboy.create_henry.infrastructure.datagen;

import com.chocoboy.create_henry.HenryCreate;
import com.chocoboy.create_henry.infrastructure.ponder.HenryPonderPlugin;
import com.chocoboy.create_henry.registry.HenryLangPartial;
import com.tterrag.registrate.providers.ProviderType;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class HenryDatagen {

	public static void gatherData(GatherDataEvent event) {
		addExtraRegistrateData();

		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		if (event.includeServer()) {
			generator.addProvider(true, new AdvancedCraftingRecipeGen(output, lookupProvider));

			generator.addProvider(true, new WashingRecipeGen(output, lookupProvider));
			generator.addProvider(true, new SandingRecipeGen(output, lookupProvider));
			generator.addProvider(true, new FreezingRecipeGen(output, lookupProvider));
			generator.addProvider(true, new SeethingRecipeGen(output, lookupProvider));
			generator.addProvider(true, new WitheringRecipeGen(output, lookupProvider));
			generator.addProvider(true, new DragonBreathingRecipeGen(output, lookupProvider));
			generator.addProvider(true, new ItemApplicationRecipeGen(output, lookupProvider));
			generator.addProvider(true, new MixingRecipeGen(output, lookupProvider));
			generator.addProvider(true, new EmptyingRecipeGen(output, lookupProvider));
			generator.addProvider(true, new FillingRecipeGen(output, lookupProvider));
			generator.addProvider(true, new HydraulicRecipeGen(output, lookupProvider));
		}
	}

	private static void addExtraRegistrateData() {
		HenryRegistrateTags.addGenerators();

		HenryCreate.registrate().addDataGenerator(ProviderType.LANG, provider -> {
			BiConsumer<String, String> langConsumer = provider::add;
			HenryLangPartial.provideLang(langConsumer);
			PonderIndex.addPlugin(new HenryPonderPlugin());
			PonderIndex.getLangAccess().provideLang(HenryCreate.MOD_ID, langConsumer);
		});
	}
}
