package com.chocoboy.create_henry.content.fluids;

import java.util.function.Supplier;

import org.joml.Vector3f;

import net.createmod.catnip.theme.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.FluidStack;
import com.tterrag.registrate.builders.FluidBuilder.FluidTypeFactory;

public class SolidRenderedPlaceableFluidType extends TintedFluidType {

	private Supplier<Vector3f> fogColor;
	private Supplier<Float> fogDistance;
	private int tintRGB = -1; // -1 means no tint

	public static FluidTypeFactory create(int fogColor, Supplier<Float> fogDistance) {
		return (p, s, f) -> {
			SolidRenderedPlaceableFluidType fluidType = new SolidRenderedPlaceableFluidType(p, s, f);
			Vector3f baked = new Color(fogColor, false).asVectorF();
			fluidType.fogColor = () -> baked;
			fluidType.fogDistance = fogDistance;
			return fluidType;
		};
	}

	public static FluidTypeFactory create(int fogColor, Supplier<Float> fogDistance,
			ResourceLocation stillTex, ResourceLocation flowTex) {
		return (p, s, f) -> {
			SolidRenderedPlaceableFluidType fluidType = new SolidRenderedPlaceableFluidType(p, stillTex, flowTex);
			Vector3f baked = new Color(fogColor, false).asVectorF();
			fluidType.fogColor = () -> baked;
			fluidType.fogDistance = fogDistance;
			return fluidType;
		};
	}

	public static FluidTypeFactory create(Supplier<Integer> fogColor, Supplier<Float> fogDistance,
			ResourceLocation stillTex, ResourceLocation flowTex) {
		return (p, s, f) -> {
			SolidRenderedPlaceableFluidType fluidType = new SolidRenderedPlaceableFluidType(p, stillTex, flowTex);
			fluidType.fogColor = () -> new Color(fogColor.get(), false).asVectorF();
			fluidType.fogDistance = fogDistance;
			return fluidType;
		};
	}

	public static FluidTypeFactory createTinted(int tintColor, Supplier<Float> fogDistance,
			ResourceLocation stillTex, ResourceLocation flowTex) {
		return (p, s, f) -> {
			SolidRenderedPlaceableFluidType fluidType = new SolidRenderedPlaceableFluidType(p, stillTex, flowTex);
			Vector3f baked = new Color(tintColor, false).asVectorF();
			fluidType.fogColor = () -> baked;
			fluidType.fogDistance = fogDistance;
			fluidType.tintRGB = tintColor & 0x00FFFFFF;
			return fluidType;
		};
	}

	private SolidRenderedPlaceableFluidType(Properties properties, ResourceLocation stillTexture,
											ResourceLocation flowingTexture) {
		super(properties, stillTexture, flowingTexture);
	}

	@Override
	protected int getTintColor(FluidStack stack) {
		return tintRGB == -1 ? NO_TINT : (0xFF000000 | tintRGB);
	}

	/*
	 * Removing alpha from tint prevents optifine from forcibly applying biome
	 * colors to modded fluids (this workaround only works for fluids in the solid
	 * render layer)
	 */
	@Override
	public int getTintColor(FluidState state, BlockAndTintGetter world, BlockPos pos) {
		// alpha=0 prevents optifine from applying biome colors; RGB provides the tint
		return tintRGB == -1 ? 0x00FFFFFF : tintRGB;
	}

	@Override
	protected Vector3f getCustomFogColor() {
		return fogColor.get();
	}

	@Override
	protected float getFogDistanceModifier() {
		return fogDistance.get();
	}

}
