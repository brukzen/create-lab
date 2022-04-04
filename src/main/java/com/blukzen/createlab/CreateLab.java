package com.blukzen.createlab;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("createlab")
public class CreateLab {
    public static final String MODID = "createlab";

    public CreateLab() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(CreateLabRegistry::init);
    }
}
