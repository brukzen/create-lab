package com.blukzen.createlab.data;

import com.blukzen.createlab.util.NBTHelpers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class Laboratory implements ILaboratory {
    public static String INVENTORY_KEY = "inventory";
    public static String POSITION_KEY = "position";
    public static String GAMEMODE_KEY = "gamemode";

    GameType gamemode = GameType.NOT_SET;
    BlockPos position = BlockPos.ZERO;
    ListNBT armor = new ListNBT();
    ListNBT items = new ListNBT();
    ListNBT offhand = new ListNBT();
    int experienceLevels;
    float experienceProgress;

    @Override
    public void savePlayerData(ServerPlayerEntity player) {
        saveInventory(player.inventory);
        savePosition(player.blockPosition());
        saveGamemode(player.gameMode.getGameModeForPlayer());
        experienceLevels = player.experienceLevel;
        experienceProgress = player.experienceProgress;
    }

    @Override
    public void restorePlayerData(ServerPlayerEntity player) {
        restoreInventory(player.inventory);
        player.setGameMode(this.gamemode);
        player.experienceLevel = experienceLevels;
        player.experienceProgress = experienceProgress;
    }

    @Override
    public void resetPlayer(ServerPlayerEntity player) {
        player.inventory.clearContent();
        player.setExperienceLevels(0);
        player.setExperiencePoints(0);
    }

    @Override

    public GameType getGamemode() {
        return this.gamemode;
    }

    @Override
    public void saveGamemode(GameType gamemode) {
        if (gamemode == null) {
            return;
        }

        this.gamemode = gamemode;
    }

    @Override
    public BlockPos getPosition() {
        return this.position;
    }

    @Override
    public void savePosition(BlockPos position) {
        this.position = position;
    }

    @Override
    public void saveInventory(PlayerInventory inventory) {
        if (inventory == null) {
            return;
        }

        this.armor = NBTHelpers.itemStackToNBT(inventory.armor);
        this.items = NBTHelpers.itemStackToNBT(inventory.items);
        this.offhand = NBTHelpers.itemStackToNBT(inventory.offhand);
    }

    @Override
    public void restoreInventory(PlayerInventory inventory) {
        if (inventory == null) {
            return;
        }

        replaceInventory(inventory.items, getSavedItems());
        replaceInventory(inventory.armor, getSavedArmor());
        replaceInventory(inventory.offhand, getSavedOffHand());
    }

    private void replaceInventory(NonNullList<ItemStack> inventory, List<ItemStack> items) {
        for (int i = 0; i < items.size(); i++) {
            inventory.set(i, items.get(i));
        }
    }

    @Override
    public List<ItemStack> getSavedArmor() {
        if (this.armor == null) {
            return new ArrayList<>();
        }

        return NBTHelpers.itemStackFromNBT(this.armor);
    }

    @Override
    public ListNBT getSavedArmorNBT() {
        return this.armor;
    }

    @Override
    public void setSavedArmor(NonNullList<ItemStack> armor) {
        this.armor = NBTHelpers.itemStackToNBT(armor);
    }

    @Override
    public void setSavedArmor(ListNBT armor) {
        this.armor = armor;
    }

    @Override
    public List<ItemStack> getSavedItems() {
        if (this.items == null) {
            return new ArrayList<>();
        }

        return NBTHelpers.itemStackFromNBT(this.items);
    }

    @Override
    public ListNBT getSavedItemsNBT() {
        return this.items;
    }

    @Override
    public void setSavedItems(NonNullList<ItemStack> items) {
        this.items = NBTHelpers.itemStackToNBT(items);
    }

    @Override
    public void setSavedItems(ListNBT items) {
        this.items = items;
    }

    @Override
    public List<ItemStack> getSavedOffHand() {
        if (this.armor == null) {
            return new ArrayList<>();
        }

        return NBTHelpers.itemStackFromNBT(this.offhand);
    }

    @Override
    public ListNBT getSavedOffHandNBT() {
        return this.offhand;
    }

    @Override
    public void setSavedOffHand(NonNullList<ItemStack> offhand) {
        this.offhand = NBTHelpers.itemStackToNBT(offhand);
    }

    @Override
    public void setSavedOffHand(ListNBT offhand) {
        this.offhand = offhand;
    }

    @Override
    public INBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();

        CompoundNBT inventoryTag = new CompoundNBT();
        inventoryTag.put("items", this.items);
        inventoryTag.put("armor", this.armor);
        inventoryTag.put("offhand", this.offhand);

        tag.put(INVENTORY_KEY, inventoryTag);
        tag.put(POSITION_KEY, NBTUtil.writeBlockPos(getPosition()));
        tag.putInt(GAMEMODE_KEY, this.gamemode.getId());

        return tag;
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        CompoundNBT inventoryTag = tag.getCompound(INVENTORY_KEY);
        CompoundNBT positionTag = tag.getCompound(POSITION_KEY);

        ListNBT itemsNBT = inventoryTag.getList("items", Constants.NBT.TAG_COMPOUND);
        ListNBT armorNBT = inventoryTag.getList("armor", Constants.NBT.TAG_COMPOUND);
        ListNBT offhandNBT = inventoryTag.getList("offhand", Constants.NBT.TAG_COMPOUND);

        setSavedItems(itemsNBT);
        setSavedArmor(armorNBT);
        setSavedOffHand(offhandNBT);
        savePosition(NBTUtil.readBlockPos(positionTag));
        saveGamemode(GameType.byId(tag.getInt(GAMEMODE_KEY)));
    }

    @Override
    public IFormattableTextComponent toText() {
        StringTextComponent component = new StringTextComponent("");

        component.append(TextFormatting.AQUA + "Gamemode: " + TextFormatting.RESET + gamemode + "\n");
        component.append(TextFormatting.AQUA + "Position: " + TextFormatting.RESET + position.toString() + "\n");
        component.append(TextFormatting.AQUA + "Items: " + TextFormatting.RESET + "\n" + items.toString() + "\n");
        component.append(TextFormatting.AQUA + "Armor: " + TextFormatting.RESET + "\n" + armor.toString() + "\n");
        component.append(TextFormatting.AQUA + "Offhand: " + TextFormatting.RESET + "\n" + offhand.toString() + "\n");

        return component;
    }
}
