package com.blukzen.createlab;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

public class CreateLabConfig {
    public static final ForgeConfigSpec COMMON_SPEC;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> labPortalFrameBlocks;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> disabledItems;
    public static ForgeConfigSpec.IntValue tpCommandPermissionLevel;
    public static ForgeConfigSpec.IntValue debugCommandPermissionLevel;
    public static ForgeConfigSpec.IntValue saveCommandPermissionLevel;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        COMMON_SPEC = configBuilder.build();
    }

    private static void setupConfig(ForgeConfigSpec.Builder builder) {
        labPortalFrameBlocks = builder.comment("Blocks that can be used as a lab portal frame").defineList("lab_portal_frame_blocks", Arrays.asList("minecraft:smooth_quartz", "minecraft:quartz_block"), entry -> true);
        disabledItems = builder.comment("Blocks and items that are disabled in the laboratory dimension").defineList("disabled_items", Arrays.asList("minecraft:ender_chest"), entry -> true);

        builder.push("Command Permission Levels");
        {
            builder.comment("0 = All, 1 = OPs, 3 = Disabled");
            tpCommandPermissionLevel = builder.defineInRange("tp_permission_level", 0, 0, 2);
            debugCommandPermissionLevel = builder.defineInRange("debug_permission_level", 0, 0, 2);
            saveCommandPermissionLevel = builder.defineInRange("save_permission_level", 0, 0, 2);
        }
        builder.pop();



    }
}
