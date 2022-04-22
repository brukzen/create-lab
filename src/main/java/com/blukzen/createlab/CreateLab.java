package com.blukzen.createlab;

import com.blukzen.createlab.block.LabBlocks;
import com.blukzen.createlab.item.LabItems;
import com.blukzen.createlab.util.GUIUtil;
import com.blukzen.createlab.world.LabPointOfInterestType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("createlab")
public class CreateLab {
    public static final String MODID = "createlab";
    public static Logger LOGGER = LogManager.getLogger("CreateLab");

    public CreateLab() {
        IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
        LabBlocks.BLOCKS.register(modbus);
        LabItems.ITEMS.register(modbus);
        LabPointOfInterestType.POI_TYPE.register(modbus);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(CreateLabRegistry::init);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CreateLabConfig.COMMON_SPEC, "createlab-common.toml");

        if (FMLEnvironment.dist.isClient()) {
            GUIUtil.INSTANCE = new GUIUtil();
        }
    }
}
