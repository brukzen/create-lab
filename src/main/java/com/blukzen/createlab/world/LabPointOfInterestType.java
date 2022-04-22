package com.blukzen.createlab.world;

import com.blukzen.createlab.CreateLab;
import com.blukzen.createlab.block.LabBlocks;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;

public class LabPointOfInterestType {
    public static DeferredRegister<PointOfInterestType> POI_TYPE = DeferredRegister.create(ForgeRegistries.POI_TYPES, CreateLab.MODID);

    public static final RegistryObject<PointOfInterestType> LAB_PORTAL = POI_TYPE.register("lab_portal", () -> new PointOfInterestType("lab_portal", getAllStates(LabBlocks.LAB_PORTAL.get()), 0, 1));

    public static Set<BlockState> getAllStates(Block block) {
        return ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates());
    }
}
