package com.blukzen.createlab.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;

import java.util.List;

public class NBTHelpers {

    public static ListNBT itemStackToNBT(List<ItemStack> stacks) {
        ListNBT nbtTagList = new ListNBT();
        for (int i = 0; i < stacks.size(); i++) {
            CompoundNBT tag = stacks.get(i).serializeNBT();
            tag.putInt("slotIndex", i);

            nbtTagList.add(tag);
        }

        return nbtTagList;
    }

    public static NonNullList<ItemStack> itemStackFromNBT(ListNBT nbt) {
        NonNullList<ItemStack> inventory = NonNullList.withSize(nbt.size(), ItemStack.EMPTY);

        for (int i = 0; i < nbt.size(); i++) {
            CompoundNBT tag = nbt.getCompound(i);
            inventory.set(tag.getInt("slotIndex"), ItemStack.of(tag));
        }

        return inventory;
    }
}