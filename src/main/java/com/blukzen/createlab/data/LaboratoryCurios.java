package com.blukzen.createlab.data;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.Constants;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.Map;

public class LaboratoryCurios extends Laboratory {

    ListNBT curios = new ListNBT();

    @Override
    public void savePlayerData(ServerPlayerEntity player) {
        super.savePlayerData(player);

        CuriosApi.getCuriosHelper().getCuriosHandler(player).ifPresent(handler -> {
            this.curios = curiosToNBT(handler.getCurios());
        });
    }

    @Override
    public void restorePlayerData(ServerPlayerEntity player) {
        super.restorePlayerData(player);

        CuriosApi.getCuriosHelper().getCuriosHandler(player).ifPresent(handler -> {
            for (int i = 0; i < this.curios.size(); i++) {
                CompoundNBT tag = this.curios.getCompound(i);
                String identifier = tag.getString("slot");

                handler.getStacksHandler(identifier).ifPresent((stacksHandler -> {
                    stacksHandler.deserializeNBT(tag.getCompound("curiosStack"));
                }));
            }
        });
    }

    @Override
    public void clearPlayerInventory(ServerPlayerEntity player) {
        super.clearPlayerInventory(player);
        CuriosApi.getCuriosHelper().getCuriosHandler(player).ifPresent(ICuriosItemHandler::reset);
    }

    @Override
    public INBT serializeNBT() {
        CompoundNBT tag = (CompoundNBT) super.serializeNBT();

        tag.put("curios", this.curios);

        return tag;
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        super.deserializeNBT(tag);

        this.curios = tag.getList("curios", Constants.NBT.TAG_COMPOUND);
    }

    @Override
    public IFormattableTextComponent toText() {
        IFormattableTextComponent component = super.toText();

        component.append(TextFormatting.AQUA + "Curios: " + TextFormatting.RESET + "\n" + curios.toString() + "\n");

        return component;
    }

    private ListNBT curiosToNBT(Map<String, ICurioStacksHandler> curios) {
        ListNBT tagList = new ListNBT();

        curios.forEach((slot, stacksHandler) -> {
            CompoundNBT tag = new CompoundNBT();

            tag.putString("slot", slot);
            tag.put("curiosStack", stacksHandler.serializeNBT());

            tagList.add(tag);
        });

        return tagList;
    }
}
