package com.blukzen.createlab.block;

import com.blukzen.createlab.CreateLab;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

@Nonnull
@Mod.EventBusSubscriber(modid = CreateLab.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LabBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CreateLab.MODID);

    public static final RegistryObject<Block> LAB_PORTAL = BLOCKS.register("lab_portal", () -> new LabPortalBlock(Block.Properties.of(Material.PORTAL).noCollission()));

    @SubscribeEvent
    public static void registerItemblocks(RegistryEvent.Register<Item> evt) {
        LabBlockItems.registerBlockItems(evt);
    }

    @SubscribeEvent
    public static void setup(FMLClientSetupEvent evt) {
        RenderTypeLookup.setRenderLayer(LAB_PORTAL.get(), RenderType.translucent());
    }
}
