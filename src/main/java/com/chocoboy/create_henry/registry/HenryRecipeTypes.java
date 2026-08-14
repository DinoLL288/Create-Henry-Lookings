package com.chocoboy.create_henry.registry;

import com.chocoboy.create_henry.HenryCreate;
import com.chocoboy.create_henry.content.recipes.*;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

import net.createmod.catnip.lang.Lang;

import java.util.Optional;
import java.util.function.Supplier;

public enum HenryRecipeTypes implements IRecipeTypeInfo {

	SANDING(() -> new StandardProcessingRecipe.Serializer<>(SandingRecipe::new)),
	FREEZING(() -> new StandardProcessingRecipe.Serializer<>(FreezingRecipe::new)),
	SEETHING(() -> new StandardProcessingRecipe.Serializer<>(SeethingRecipe::new)),
	WITHERING(() -> new StandardProcessingRecipe.Serializer<>(WitheringRecipe::new)),
	DRAGON_BREATHING(() -> new StandardProcessingRecipe.Serializer<>(DragonBreathingRecipe::new)),
	HYDRAULIC_COMPACTING(() -> new StandardProcessingRecipe.Serializer<>(HydraulicRecipe::new));

	private final ResourceLocation id;
	private final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> serializerObject;
	@Nullable
	private final DeferredHolder<RecipeType<?>, RecipeType<?>> typeObject;
	private final Supplier<RecipeType<?>> type;

	HenryRecipeTypes(Supplier<RecipeSerializer<?>> serializerSupplier) {
		String name = Lang.asId(name());
		id = HenryCreate.asResource(name);
		serializerObject = Registers.SERIALIZER_REGISTER.register(name, serializerSupplier);
		typeObject = Registers.TYPE_REGISTER.register(name, () -> RecipeType.simple(id));
		type = typeObject;
	}

	HenryRecipeTypes(StandardProcessingRecipe.Factory<?> processingFactory) {
		this(() -> new StandardProcessingRecipe.Serializer<>(processingFactory));
	}

	public static void register(IEventBus modEventBus) {
		Registers.SERIALIZER_REGISTER.register(modEventBus);
		Registers.TYPE_REGISTER.register(modEventBus);
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends RecipeSerializer<?>> T getSerializer() {
		return (T) serializerObject.get();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <I extends RecipeInput, R extends Recipe<I>> RecipeType<R> getType() {
		return (RecipeType<R>) type.get();
	}

	public <C extends RecipeInput, T extends Recipe<C>> Optional<T> find(C inv, Level world) {
		return world.getRecipeManager()
				.getRecipeFor(getType(), inv, world)
				.map(RecipeHolder::value);
	}

	private static class Registers {
		private static final DeferredRegister<RecipeSerializer<?>> SERIALIZER_REGISTER = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, HenryCreate.MOD_ID);
		private static final DeferredRegister<RecipeType<?>> TYPE_REGISTER = DeferredRegister.create(Registries.RECIPE_TYPE, HenryCreate.MOD_ID);
	}

}
