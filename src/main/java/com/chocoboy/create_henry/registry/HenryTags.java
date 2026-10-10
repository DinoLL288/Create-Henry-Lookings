package com.chocoboy.create_henry.registry;

import com.chocoboy.create_henry.HenryCreate;
import com.simibubi.create.Create;
import net.createmod.catnip.lang.Lang;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

import static com.chocoboy.create_henry.registry.HenryTags.NameSpace.COMMON;

@SuppressWarnings({"all"})
public class HenryTags {
	public static <T> TagKey<T> optionalTag(Registry<T> registry,
											ResourceLocation id) {
		return TagKey.create(registry.key(), id);
	}

	public static <T> TagKey<T> commonTag(Registry<T> registry, String path) {
		return optionalTag(registry, ResourceLocation.fromNamespaceAndPath("c", path));
	}

	public static TagKey<Block> commonBlockTag(String path) {
		return commonTag(BuiltInRegistries.BLOCK, path);
	}

	public static TagKey<Item> commonItemTag(String path) {
		return commonTag(BuiltInRegistries.ITEM, path);
	}

	public static TagKey<Fluid> commonFluidTag(String path) {
		return commonTag(BuiltInRegistries.FLUID, path);
	}

	public static TagKey<Item> minecraftItemTag(String path) {
		return ItemTags.create(ResourceLocation.fromNamespaceAndPath("minecraft", path));
	}

	public enum NameSpace {

		MOD(HenryCreate.MOD_ID, false, true),
		CREATE("create"),
		FORGE("forge"),
		COMMON("c"),

		;

		public final String id;
		public final boolean optionalDefault;
		public final boolean alwaysDatagenDefault;

		NameSpace(String id) {
			this(id, true, false);
		}

		NameSpace(String id, boolean optionalDefault, boolean alwaysDatagenDefault) {
			this.id = id;
			this.optionalDefault = optionalDefault;
			this.alwaysDatagenDefault = alwaysDatagenDefault;
		}
	}

	public enum AllBlockTags {

		FAN_PROCESSING_CATALYSTS_SANDING(NameSpace.MOD, "fan_processing_catalysts/sanding"),
		FAN_PROCESSING_CATALYSTS_FREEZING(NameSpace.MOD, "fan_processing_catalysts/freezing"),
		FAN_PROCESSING_CATALYSTS_SEETHING(NameSpace.MOD, "fan_processing_catalysts/seething"),
		FAN_PROCESSING_CATALYSTS_WITHERING(NameSpace.MOD, "fan_processing_catalysts/withering"),
		FAN_PROCESSING_CATALYSTS_DRAGON_BREATHING(NameSpace.MOD, "fan_processing_catalysts/dragon_breathing"),
		INDUSTRIAL_FAN_TRANSPARENT,
		ORE_GENERATOR,
		ARTIFICIAL_ORE_GENERATOR

		;

		public final TagKey<Block> tag;
		public final boolean alwaysDatagen;

		AllBlockTags() {
			this(NameSpace.MOD);
		}

		AllBlockTags(NameSpace namespace) {
			this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		AllBlockTags(NameSpace namespace, String path) {
			this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		AllBlockTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
			this(namespace, null, optional, alwaysDatagen);
		}

		AllBlockTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
			ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace.id, path == null ? Lang.asId(name()) : path);
			if (optional) {
				tag = optionalTag(BuiltInRegistries.BLOCK, id);
			} else {
				tag = BlockTags.create(id);
			}
			this.alwaysDatagen = alwaysDatagen;
		}

		@SuppressWarnings("deprecation")
		public boolean matches(Block block) {
			return block.builtInRegistryHolder()
					.is(tag);
		}

		public boolean matches(ItemStack stack) {
			return stack != null && stack.getItem() instanceof BlockItem blockItem && matches(blockItem.getBlock());
		}

		public boolean matches(BlockState state) {
			return state.is(tag);
		}

		private static void init() {}

	}

	public enum AllItemTags {

		SEETHABLE,
		SANDABLE,
		FREEZABLE,
		WITHERABLE,
		DRAGON_BREATHABLE,

		MEAT(NameSpace.MOD, "meat")
		;

		public final TagKey<Item> tag;
		public final boolean alwaysDatagen;

		AllItemTags() {
			this(NameSpace.MOD);
		}

		AllItemTags(NameSpace namespace) {
			this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		AllItemTags(NameSpace namespace, String path) {
			this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		AllItemTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
			this(namespace, null, optional, alwaysDatagen);
		}

		AllItemTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
			ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace.id, path == null ? Lang.asId(name()) : path);
			if (optional) {
				tag = optionalTag(BuiltInRegistries.ITEM, id);
			} else {
				tag = ItemTags.create(id);
			}
			this.alwaysDatagen = alwaysDatagen;
		}

		@SuppressWarnings("deprecation")
		public boolean matches(Item item) {
			return item.builtInRegistryHolder()
					.is(tag);
		}

		public boolean matches(ItemStack stack) {
			return stack.is(tag);
		}

		private static void init() {}

	}

	public enum AllFluidTags {

		FAN_PROCESSING_CATALYSTS_SANDING(NameSpace.MOD, "fan_processing_catalysts/sanding"),
		FAN_PROCESSING_CATALYSTS_FREEZING(NameSpace.MOD, "fan_processing_catalysts/freezing"),
		FAN_PROCESSING_CATALYSTS_SEETHING(NameSpace.MOD, "fan_processing_catalysts/seething"),
		FAN_PROCESSING_CATALYSTS_WITHERING(NameSpace.MOD, "fan_processing_catalysts/withering"),
		FAN_PROCESSING_CATALYSTS_DRAGON_BREATHING(NameSpace.MOD, "fan_processing_catalysts/dragon_breathing"),

		SAP(COMMON),
		CHOCOLATE(COMMON),
		VANILLA(COMMON),
		STRAWBERRY(COMMON),
		GLOWBERRY(COMMON),
		PUMPKIN(COMMON)

		;

		public final TagKey<Fluid> tag;
		public final boolean alwaysDatagen;

		AllFluidTags() {
			this(NameSpace.MOD);
		}

		AllFluidTags(NameSpace namespace) {
			this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		AllFluidTags(NameSpace namespace, String path) {
			this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		AllFluidTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
			this(namespace, null, optional, alwaysDatagen);
		}

		AllFluidTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
			ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace.id, path == null ? Lang.asId(name()) : path);
			if (optional) {
				tag = optionalTag(BuiltInRegistries.FLUID, id);
			} else {
				tag = FluidTags.create(id);
			}
			this.alwaysDatagen = alwaysDatagen;
		}

		@SuppressWarnings("deprecation")
		public boolean matches(Fluid fluid) {
			return fluid.is(tag);
		}

		public boolean matches(FluidState state) {
			return state.is(tag);
		}

		private static void init() {}

	}

	public enum HenryRecipeSerializerTags {

		;

		public final TagKey<RecipeSerializer<?>> tag;
		public final boolean alwaysDatagen;

		HenryRecipeSerializerTags() {
			this(NameSpace.MOD);
		}

		HenryRecipeSerializerTags(NameSpace namespace) {
			this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		HenryRecipeSerializerTags(NameSpace namespace, String path) {
			this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		HenryRecipeSerializerTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
			this(namespace, null, optional, alwaysDatagen);
		}

		HenryRecipeSerializerTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
			ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace.id, path == null ? Lang.asId(name()) : path);
			if (optional) {
				tag = optionalTag(BuiltInRegistries.RECIPE_SERIALIZER, id);
			} else {
				tag = TagKey.create(Registries.RECIPE_SERIALIZER, id);
			}
			this.alwaysDatagen = alwaysDatagen;
		}

		public boolean matches(RecipeSerializer<?> recipeSerializer) {
			return BuiltInRegistries.RECIPE_SERIALIZER.getResourceKey(recipeSerializer)
					.flatMap(BuiltInRegistries.RECIPE_SERIALIZER::getHolder)
					.orElseThrow()
					.is(tag);
		}

		private static void init() {}
	}

	public static void init() {
		AllBlockTags.init();
		AllItemTags.init();
		AllFluidTags.init();
		HenryRecipeSerializerTags.init();
	}
}
