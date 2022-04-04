package com.blukzen.createlab;

import com.blukzen.createlab.data.LaboratoryCapability;
import com.blukzen.createlab.data.LaboratoryEventHandler;
import com.blukzen.createlab.commands.ModCommands;
import com.blukzen.createlab.network.NetworkHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = CreateLab.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CreateLabRegistry {
    public static void init(final FMLCommonSetupEvent event) {
        LaboratoryCapability.register();
        NetworkHandler.register();
        MinecraftForge.EVENT_BUS.register(new LaboratoryEventHandler());
    }

    @SubscribeEvent
    public static void serverLoad(RegisterCommandsEvent event) {
        ModCommands.register(event.getDispatcher());
    }
}
