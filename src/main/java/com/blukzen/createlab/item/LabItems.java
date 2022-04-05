package com.blukzen.createlab.item;

import com.blukzen.createlab.CreateLab;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class LabItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CreateLab.MODID);

    public static ItemGroup creativeTab = new ItemGroup(CreateLab.MODID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Blocks.STONE.getBlock());
        }
    };

    public static Item.Properties defaultBuilder() {
        return new Item.Properties().tab(creativeTab);
    }
}
