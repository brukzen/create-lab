package com.blukzen.createlab.data;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.concurrent.Callable;

public class LaboratoryCapability {

    @CapabilityInject(ILaboratory.class)
    public static final Capability<ILaboratory> LABORATORY_CAPABILITY;
    public static final String LABORATORY_KEY = "laboratory";

    static {
        LABORATORY_CAPABILITY = null;
    }

    public static void register() {
        CapabilityManager.INSTANCE.register(ILaboratory.class, new Storage(), Laboratory::new);
    }

    public static ICapabilityProvider createProvider(final PlayerEntity playerEntity) {
        return new LaboratoryProvider(playerEntity);
    }

    public static class Storage implements Capability.IStorage<ILaboratory> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<ILaboratory> capability, ILaboratory instance, Direction side) {
            return instance.serializeNBT();
        }


        @Override
        public void readNBT(Capability<ILaboratory> capability, ILaboratory instance, Direction side, INBT nbt) {
            instance.deserializeNBT(nbt);
        }
    }
}