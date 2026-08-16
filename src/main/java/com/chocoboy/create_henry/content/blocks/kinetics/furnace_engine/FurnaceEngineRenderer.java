package com.chocoboy.create_henry.content.blocks.kinetics.furnace_engine;

import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.createmod.catnip.math.AngleHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import com.chocoboy.create_henry.registry.HenryPartialModels;

public class FurnaceEngineRenderer extends SafeBlockEntityRenderer<FurnaceEngineBlockEntity> {
    public FurnaceEngineRenderer(BlockEntityRendererProvider.Context context) {
    }

    protected void renderSafe(FurnaceEngineBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (!VisualizationManager.supportsVisualization(be.getLevel())) {
            Float angle = be.getTargetAngle();
            if (angle != null) {
                BlockState blockState = be.getBlockState();
                Direction facing = FurnaceEngineBlock.getFacing(blockState);
                Direction.Axis facingAxis = facing.getAxis();
                Direction.Axis axis = Axis.Y;
                PoweredFlywheelBlockEntity flywheel = be.getFlywheel();
                if (flywheel != null) {
                    axis = KineticBlockEntityRenderer.getRotationAxisOf(flywheel);
                }

                boolean roll90 = facingAxis.isHorizontal() && axis == Axis.Y || facingAxis.isVertical() && axis == Axis.Z;
                float sine = Mth.sin(angle);
                float sine2 = Mth.sin(angle - 1.5707964F);
                float piston = (1.0F - sine) / 4.0F * 24.0F / 16.0F;
                VertexConsumer vb = buffer.getBuffer(RenderType.solid());
                this.transformed(HenryPartialModels.ENGINE_PISTON, blockState, facing, roll90)
                        .translate(0.0, piston, 0.0)
                        .light(light)
                        .renderInto(ms, vb);
                this.transformed(HenryPartialModels.ENGINE_LINKAGE, blockState, facing, roll90).center()
                        .translate(0.0, 1.0, 0.0)
                        .uncenter().translate(0.0, piston, 0.0)
                        .translate(0.0, 0.25, 0.5)
                        .rotateXDegrees(sine2 * 23.0F)
                        .translate(0.0, -0.25, -0.5)
                        .light(light)
                        .renderInto(ms, vb);
                this.transformed(HenryPartialModels.ENGINE_CONNECTOR, blockState, facing, roll90)
                        .translate(0.0, 2.0, 0.0)
                        .center()
                        .rotateX(-angle + 1.5707964F)
                        .uncenter()
                        .light(light)
                        .renderInto(ms, vb);
            }
        }
    }

    private SuperByteBuffer transformed(PartialModel model, BlockState blockState, Direction facing, boolean roll90) {
        // Debugging log
        com.chocoboy.create_henry.HenryCreate.LOGGER.debug("Rendering model {} with facing {}. Angle: {}", model, facing, net.createmod.catnip.math.AngleHelper.horizontalAngle(facing));
        
        return CachedBuffers.partial(model, blockState)
                .center()
                .rotateYDegrees(net.createmod.catnip.math.AngleHelper.horizontalAngle(facing))
                .rotateXDegrees(net.createmod.catnip.math.AngleHelper.verticalAngle(facing) + 90.0F)
                .rotateYDegrees((float) (roll90 ? -90.0 : 0.0))
                .uncenter();
    }

    public int getViewDistance() {
        return 128;
    }
}
