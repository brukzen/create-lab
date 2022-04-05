package com.blukzen.createlab.block;

import com.blukzen.createlab.item.LabItems;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Objects;

public class LabBlockItems {

    public static  void registerBlockItems(RegistryEvent.Register<Item> evt) {
        IForgeRegistry<Item> r = evt.getRegistry();
        r.register(blockItem(LabBlocks.LAB_PORTAL));
    }

    private static <B extends Block> Item blockItem(RegistryObject<B> block) {
        return makeBlockItem(new BlockItem(block.get(), LabItems.defaultBuilder()), block);
    }

    private static Item makeBlockItem(Item blockitem, RegistryObject<? extends Block> block) {
        return blockitem.setRegistryName(Objects.requireNonNull(block.getId()));
    }
}
