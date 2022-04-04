package com.blukzen.createlab.data;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LaboratoryProvider implements ICapabilitySerializable<INBT> {

    private final ILaboratory handler;
    private final LazyOptional<ILaboratory> optional;

    public LaboratoryProvider(PlayerEntity player) {
        if (ModList.get().isLoaded("curios")) {
            this.handler = new LaboratoryCurios();
        } else {
            this.handler = new Laboratory();
        }
        this.optional = LazyOptional.of(() -> this.handler);
    }

    public static LazyOptional<ILaboratory> getHandler(PlayerEntity player) {
        return player.getCapability(LaboratoryCapability.LABORATORY_CAPABILITY);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return LaboratoryCapability.LABORATORY_CAPABILITY.orEmpty(cap, this.optional);
    }

    @Override
    public INBT serializeNBT() {
        return LaboratoryCapability.LABORATORY_CAPABILITY.writeNBT(this.handler, null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        LaboratoryCapability.LABORATORY_CAPABILITY.readNBT(this.handler, null, nbt);
    }
}
