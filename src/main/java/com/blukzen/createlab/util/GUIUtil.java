package com.blukzen.createlab.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class GUIUtil {

    public static GUIUtil INSTANCE;
    public boolean enabled = false;

    private final Map<String, String> debugMessages = new HashMap<>();

    public void addDebugMessage(String key, String message) {
        debugMessages.put(key, message);
    }

    public void renderDebugMessages(MatrixStack matrix) {
        if (enabled) {
            FontRenderer font = Minecraft.getInstance().font;

            matrix.pushPose();
            matrix.translate(5, 5, 0);
            {
                AtomicInteger i = new AtomicInteger();
                debugMessages.forEach((key, message) -> {
                    font.draw(matrix, key + ": " + message, 0, i.get() * (font.lineHeight + 2), 0xFFFFFF);
                    i.getAndIncrement();
                });
            }
            matrix.popPose();
        }
    }
}
