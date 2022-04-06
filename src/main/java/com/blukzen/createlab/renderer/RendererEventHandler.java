package com.blukzen.createlab.renderer;

import com.blukzen.createlab.CreateLab;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Nonnull
@Mod.EventBusSubscriber(modid = CreateLab.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RendererEventHandler {
    private static final ResourceLocation NAUSEA_LOCATION = new ResourceLocation("textures/misc/nausea.png");

    @SubscribeEvent
    public static void renderOverlay(TickEvent.RenderTickEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
//        if (event.phase == TickEvent.Phase.END && event.side == LogicalSide.CLIENT) {
//            ClientPlayerEntityMixin player = (ClientPlayerEntityMixin) minecraft.player;
//            if (!minecraft.noRender && minecraft.level != null && player != null) {
//                float f = MathHelper.lerp(event.renderTickTime, player.getOTimeInLabPortal(), player.getTimeInLabPortal());
//                if (player.isInsideLabPortal()) {
//                    renderConfusionOverlay(f * (1.0f - minecraft.options.screenEffectScale));
//                }
//            }
//        }
    }

    private static void renderConfusionOverlay(float p_243497_1_) {
        Minecraft minecraft = Minecraft.getInstance();

        int i = minecraft.getWindow().getGuiScaledWidth();
        int j = minecraft.getWindow().getGuiScaledHeight();
        double d0 = MathHelper.lerp((double) p_243497_1_, 2.0D, 1.0D);
        float f = 0.2F * p_243497_1_;
        float f1 = 0.4F * p_243497_1_;
        float f2 = 0.2F * p_243497_1_;
        double d1 = (double) i * d0;
        double d2 = (double) j * d0;
        double d3 = ((double) i - d1) / 2.0D;
        double d4 = ((double) j - d2) / 2.0D;
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        RenderSystem.color4f(f, f1, f2, 1.0F);
        minecraft.getTextureManager().bind(NAUSEA_LOCATION);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.vertex(d3, d4 + d2, -90.0D).uv(0.0F, 1.0F).endVertex();
        bufferbuilder.vertex(d3 + d1, d4 + d2, -90.0D).uv(1.0F, 1.0F).endVertex();
        bufferbuilder.vertex(d3 + d1, d4, -90.0D).uv(1.0F, 0.0F).endVertex();
        bufferbuilder.vertex(d3, d4, -90.0D).uv(0.0F, 0.0F).endVertex();
        tessellator.end();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
    }
}
