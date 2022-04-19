package com.blukzen.createlab;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

public class CreateLabConfig {
    public static final ForgeConfigSpec COMMON_SPEC;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> labPortalFrameBlocks;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        COMMON_SPEC = configBuilder.build();
    }

    private static void setupConfig(ForgeConfigSpec.Builder builder) {
        labPortalFrameBlocks = builder.comment("Blocks that can be used as a lab portal frame").defineList("lab_portal_frame_blocks", Arrays.asList("minecraft:smooth_quartz", "minecraft:quartz_block"), entry -> true);
    }
}
