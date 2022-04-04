package com.blukzen.createlab.data;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.world.GameType;

import java.util.List;

public interface ILaboratory {
    void savePlayerData(ServerPlayerEntity player);
    void restorePlayerData(ServerPlayerEntity player);
    void clearPlayerInventory(ServerPlayerEntity player);

    GameType getGamemode();
    void saveGamemode(GameType gamemode);

    BlockPos getPosition();
    void savePosition(BlockPos position);

    void saveInventory(PlayerInventory inventory);
    void restoreInventory(PlayerInventory inventory);

    List<ItemStack> getSavedArmor();
    ListNBT getSavedArmorNBT();
    void setSavedArmor(NonNullList<ItemStack> armor);
    void setSavedArmor(ListNBT armor);

    List<ItemStack> getSavedItems();
    ListNBT getSavedItemsNBT();
    void setSavedItems(NonNullList<ItemStack> items);
    void setSavedItems(ListNBT items);

    List<ItemStack> getSavedOffHand();
    ListNBT getSavedOffHandNBT();
    void setSavedOffHand(NonNullList<ItemStack> offhand);
    void setSavedOffHand(ListNBT offhand);

    INBT serializeNBT();
    void deserializeNBT(INBT nbt);

    IFormattableTextComponent toText();
}
