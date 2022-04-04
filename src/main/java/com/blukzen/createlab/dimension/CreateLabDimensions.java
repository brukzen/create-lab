package com.blukzen.createlab.dimension;

import com.blukzen.createlab.CreateLab;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;

public class CreateLabDimensions {
    public static final RegistryKey<DimensionType> LABIDIM_TYPE = RegistryKey.create(Registry.DIMENSION_TYPE_REGISTRY, new ResourceLocation(CreateLab.MODID, "laboratory"));
    public static final RegistryKey<World> LABDIM = RegistryKey.create((Registry.DIMENSION_REGISTRY), new ResourceLocation(CreateLab.MODID, "laboratory"));
}
