package com.chocoboy.create_henry.registry;

import com.chocoboy.create_henry.HenryCreate;
import com.chocoboy.create_henry.content.items.MilkshakeItem;
import com.chocoboy.create_henry.registry.HenryTags;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import java.util.function.Supplier;

import static com.tterrag.registrate.providers.RegistrateRecipeProvider.getItemName;
import static com.tterrag.registrate.providers.RegistrateRecipeProvider.has;
import static com.chocoboy.create_henry.HenryCreate.REGISTRATE;
import static com.chocoboy.create_henry.registry.HenryTags.forgeItemTag;

@SuppressWarnings({"unused", "deprecation", "all"})
public class HenryItems {

	public static final ItemEntry<MilkshakeItem> CHOCOLATE_MILKSHAKE = milkshake("Chocolate Milkshake", () -> new MobEffectInstance(
			MobEffects.HEALTH_BOOST,
			4 * 60 * 20,
			1,
			false,
			false,
			false
	));
	public static final ItemEntry<MilkshakeItem> VANILLA_MILKSHAKE = milkshake("Vanilla Milkshake", () -> new MobEffectInstance(
			MobEffects.SATURATION,
			10 * 20,
			0,
			false,
			false,
			false
	));
	public static final ItemEntry<MilkshakeItem> STRAWBERRY_MILKSHAKE = milkshake("Strawberry Milkshake", () -> new MobEffectInstance(
			MobEffects.REGENERATION,
			30 * 20,
			1,
			false,
			false,
			false
	));
	public static final ItemEntry<MilkshakeItem> GLOWBERRY_MILKSHAKE  = milkshake("Glowberry Milkshake", () -> new MobEffectInstance(
			MobEffects.NIGHT_VISION,
			10 * 60 * 20,
			0,
			false,
			false,
			false
	));
	public static final ItemEntry<MilkshakeItem> PUMPKIN_MILKSHAKE    = milkshake("Pumpkin Milkshake", () -> new MobEffectInstance(
			MobEffects.MOVEMENT_SPEED,
			60 * 20,
			1,
			false,
			false,
			false
	));

	public static final ItemEntry<SequencedAssemblyItem>
			INCOMPLETE_KINETIC_MECHANISM = sequencedItem("incomplete_kinetic_mechanism");

	public static final ItemEntry<Item>
			KINETIC_MECHANISM = item("kinetic_mechanism");

	public static final ItemEntry<Item> GOLDEN_WHISK = REGISTRATE.item("golden_whisk", Item::new)
			.model((c, p) -> p.withExistingParent(c.getId().getPath(),
					new ResourceLocation("item/generated")).texture("layer0",
					ResourceLocation.fromNamespaceAndPath(HenryCreate.MOD_ID,"item/" + c.getId().getPath())))
			.lang("Golden Whisk")
			.tab(HenryCreativeModeTabs.BASE_CREATIVE_TAB.getKey())
			.register();

	public static final ItemEntry<Item> RAW_RUBBER = REGISTRATE.item("raw_rubber", Item::new)
			.model((c, p) -> p.withExistingParent(c.getId().getPath(),
					new ResourceLocation("item/generated")).texture("layer0",
					ResourceLocation.fromNamespaceAndPath(HenryCreate.MOD_ID,"item/" + c.getId().getPath())))
			.tag(forgeItemTag("raw_rubbers"))
			.lang("Raw Rubber")
			.tab(HenryCreativeModeTabs.BASE_CREATIVE_TAB.getKey())
			.register();

	public static final ItemEntry<Item> RUBBER = REGISTRATE.item("rubber", Item::new)
			.model((c, p) -> p.withExistingParent(c.getId().getPath(),
					new ResourceLocation("item/generated")).texture("layer0",
					ResourceLocation.fromNamespaceAndPath(HenryCreate.MOD_ID,"item/" + c.getId().getPath())))
			.tag(forgeItemTag("rubbers"), forgeItemTag("crude_rubbers"))
			.lang("Rubber")
			.tab(HenryCreativeModeTabs.BASE_CREATIVE_TAB.getKey())
			.register();

	public static final ItemEntry<Item> LAPIS_LAZULI_SHARD = REGISTRATE.item("lapis_lazuli_shard", Item::new)
			.model((c, p) -> p.withExistingParent(c.getId().getPath(),
					new ResourceLocation("item/generated")).texture("layer0",
					ResourceLocation.fromNamespaceAndPath(HenryCreate.MOD_ID,"item/" + c.getId().getPath())))
			.tag(forgeItemTag("nuggets/lapis"), forgeItemTag("nuggets"))
			.lang("Lapis Lazuli Shard")
			.tab(HenryCreativeModeTabs.BASE_CREATIVE_TAB.getKey())
			.register();

	public static final ItemEntry<Item> COAL_PIECE = REGISTRATE.item("coal_piece", p -> new Item(p) {
		@Override
		public int getBurnTime(ItemStack stack, net.minecraft.world.item.crafting.RecipeType<?> recipeType) {
			return 200;
		}
	})
			.model((c, p) -> p.withExistingParent(c.getId().getPath(),
					new ResourceLocation("item/generated")).texture("layer0",
					ResourceLocation.fromNamespaceAndPath(HenryCreate.MOD_ID,"item/" + c.getId().getPath())))
			.tag(forgeItemTag("nuggets/coal"), forgeItemTag("nuggets"))
			.lang("Coal Piece")
			.tab(HenryCreativeModeTabs.BASE_CREATIVE_TAB.getKey())
			.register();

	// Saves with default unlock (has the item being registered) and default path (crafting/<name>)
	private static <I extends Item> void save(RecipeBuilder b, DataGenContext<Item, I> c, RegistrateRecipeProvider p) {
		b.unlockedBy("has_" + c.getName(), has(c.get()))
		 .save(p, HenryCreate.asResource("crafting/" + c.getName()));
	}

	// Saves to a custom path; caller is responsible for calling .unlockedBy() on the builder
	private static void save(RecipeBuilder b, RegistrateRecipeProvider p, String path) {
		b.save(p, HenryCreate.asResource(path));
	}

	private static ItemEntry<MilkshakeItem> milkshake(String name, Supplier<MobEffectInstance> effect) {
		String id = name.toLowerCase().replace(" ", "_");
		FoodProperties food = new FoodProperties.Builder()
				.nutrition(4).saturationMod(0.3f).alwaysEat()
				.effect(effect, 1.0f)
				.build();
		return REGISTRATE.item(id, p -> new MilkshakeItem(p.food(food).stacksTo(16)))
				.lang(name)
				.tab(HenryCreativeModeTabs.BASE_CREATIVE_TAB.getKey())
				.register();
	}

	private static ItemEntry<Item> item(String name) {
		ResourceKey<CreativeModeTab> tab = HenryCreativeModeTabs.BASE_CREATIVE_TAB.getKey();
		assert tab != null;
		return REGISTRATE.item(name, Item::new)
				.tab(tab)
				.register();
	}

	private static ItemEntry<SequencedAssemblyItem> sequencedItem(String name) {
		return REGISTRATE.item(name, SequencedAssemblyItem::new)
				.register();
	}

	public static void register() {}
}