package com.blukzen.createlab.render;

import com.blukzen.createlab.CreateLab;
import com.blukzen.createlab.accessor.ClientPlayerEntityAccessor;
import com.blukzen.createlab.block.LabBlocks;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CreateLab.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class RenderEventHandler {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void renderGameOverlay(RenderGameOverlayEvent.Post evt) {
        if (evt.getType() == RenderGameOverlayEvent.ElementType.PORTAL) {
            Minecraft minecraft = Minecraft.getInstance();
            ClientPlayerEntityAccessor player = (ClientPlayerEntityAccessor) minecraft.player;

            if (player != null) {
                float f = player.getOldLabPortalTime() + (player.getLabPortalTime() - player.getOldLabPortalTime()) * evt.getPartialTicks();
                if (f > 0) {
                    renderPortalOverlay(f);
                }
            }
        }
    }

    protected static void renderPortalOverlay(float intensity) {
        Minecraft minecraft = Minecraft.getInstance();

        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();

        if (intensity < 1.0F) {
            intensity = intensity * intensity;
            intensity = intensity * intensity;
            intensity = intensity * 0.8F + 0.2F;
        }

        RenderSystem.disableAlphaTest();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, intensity);
        minecraft.getTextureManager().bind(AtlasTexture.LOCATION_BLOCKS);
        TextureAtlasSprite textureatlassprite = minecraft.getBlockRenderer().getBlockModelShaper().getParticleIcon(LabBlocks.LAB_PORTAL.get().defaultBlockState());
        float f = textureatlassprite.getU0();
        float f1 = textureatlassprite.getV0();
        float f2 = textureatlassprite.getU1();
        float f3 = textureatlassprite.getV1();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.vertex(0.0D, (double) screenHeight, -90.0D).uv(f, f3).endVertex();
        bufferbuilder.vertex((double) screenWidth, (double) screenHeight, -90.0D).uv(f2, f3).endVertex();
        bufferbuilder.vertex((double) screenWidth, 0.0D, -90.0D).uv(f2, f1).endVertex();
        bufferbuilder.vertex(0.0D, 0.0D, -90.0D).uv(f, f1).endVertex();
        tessellator.end();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableAlphaTest();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
