package com.chocoboy.create_henry.content.fans.processing;

import com.chocoboy.create_henry.compat.HenryMods;
import com.chocoboy.create_henry.registry.HenryRecipeTypes;
import com.chocoboy.create_henry.registry.HenryTags;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.trains.CubeParticleData;
import net.createmod.catnip.theme.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public final class SeethingType extends AbstractFanProcessingType {

    private static final int COLOR_LIGHT = 0x64C9FD;
    private static final int COLOR_DARK  = 0x3f74e8;

    public SeethingType() {
        super(HenryRecipeTypes.SEETHING);
    }

    @Override
    public boolean isValidAt(Level level, BlockPos pos) {
        if (HenryTags.AllFluidTags.FAN_PROCESSING_CATALYSTS_SEETHING.matches(level.getFluidState(pos)))
            return true;

        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        if (block instanceof BlazeBurnerBlock)
            return state.getValue(BlazeBurnerBlock.HEAT_LEVEL).isAtLeast(BlazeBurnerBlock.HeatLevel.SEETHING);

        // CreateAddition liquid blaze burner reuses the same HEAT_LEVEL property
        if (HenryMods.CREATEADDITION.isLoaded()) {
            Block compat = HenryMods.CREATEADDITION.getBlock("liquid_blaze_burner");
            if (block == compat)
                return state.getValue(BlazeBurnerBlock.HEAT_LEVEL).isAtLeast(BlazeBurnerBlock.HeatLevel.SEETHING);
        }

        return HenryTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_SEETHING.matches(state);
    }

    @Override
    public int getPriority() {
        return 691200;
    }

    @Override
    public void spawnProcessingParticles(Level level, Vec3 pos) {
        if (level.random.nextInt(8) != 0) return;
        Vector3f color = new Color(0x1e0f3d).asVectorF();
        level.addParticle(new DustParticleOptions(color, 1),
                pos.x + (level.random.nextFloat() - .5f) * .5f, pos.y + .5f,
                pos.z + (level.random.nextFloat() - .5f) * .5f, 0, 1 / 8f, 0);
        level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, pos.x, pos.y + .45f, pos.z, 0, 0, 0);
        level.addParticle(ParticleTypes.SOUL_FIRE_FLAME,
                pos.x + (level.random.nextFloat() - .5f) * .5f, pos.y + .5f,
                pos.z + (level.random.nextFloat() - .5f) * .5f, 0, 1 / 8f, 0);
    }

    @Override
    public void morphAirFlow(FanProcessingType.AirFlowParticleAccess particleAccess, RandomSource random) {
        particleAccess.setColor(Color.mixColors(COLOR_LIGHT, COLOR_DARK, random.nextFloat()));
        particleAccess.setAlpha(1f);
        if (random.nextFloat() < 1 / 32f)
            particleAccess.spawnExtraParticle(ParticleTypes.SOUL_FIRE_FLAME, .125f);
        Vector3f colorBright = new Color(COLOR_LIGHT).asVectorF();
        Vector3f colorDark   = new Color(COLOR_DARK).asVectorF();
        if (random.nextFloat() < 1 / 32f)
            particleAccess.spawnExtraParticle(new DustParticleOptions(colorBright, 1), .125f);
        if (random.nextFloat() < 1 / 32f)
            particleAccess.spawnExtraParticle(new DustParticleOptions(colorDark, 1), .125f);
        if (random.nextFloat() < 1 / 48f)
            particleAccess.spawnExtraParticle(ParticleTypes.SMOKE, .125f);
        if (random.nextFloat() < 1 / 32f)
            particleAccess.spawnExtraParticle(new CubeParticleData(192, 122, 85, 0.075f, 10, true), .125f);
        if (random.nextFloat() < 1 / 32f)
            particleAccess.spawnExtraParticle(new CubeParticleData(191, 82, 91, 0.1f, 10, true), .125f);
    }

    @Override
    public void affectEntity(Entity entity, Level level) {
        if (level.isClientSide) return;

        if (entity instanceof Blaze blaze)
            blaze.heal(4);

        if (!entity.fireImmune()) {
            entity.igniteForSeconds(10);
            entity.hurt(level.damageSources().lava(), 10);
        }

        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 2, false, false));
            livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20, 1, false, false));
            livingEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 30, 0, false, false));
        }
    }
}
