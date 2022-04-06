package com.blukzen.createlab.gui;

import com.blukzen.createlab.CreateLab;
import com.blukzen.createlab.util.GUIUtil;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CreateLab.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class GuiEventHandler {
    @SubscribeEvent
    public static void renderGameOverlay(RenderGameOverlayEvent.Post evt) {
        if (evt.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            GUIUtil.INSTANCE.renderDebugMessages(evt.getMatrixStack());
        }
    }
}
