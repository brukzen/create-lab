package com.blukzen.createlab.world;

import com.blukzen.createlab.CreateLab;
import com.blukzen.createlab.data.ILaboratory;
import com.blukzen.createlab.data.LaboratoryCapability;
import com.blukzen.createlab.dimension.LabDimensions;
import net.minecraft.block.PortalInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;

import javax.annotation.Nullable;
import java.util.function.Function;

public class LabTeleporter implements ITeleporter {
    private final BlockPos spawn = new BlockPos(0, 64, 0);

    @Nullable
    @Override
    public PortalInfo getPortalInfo(Entity entity, ServerWorld dest, Function<ServerWorld, PortalInfo> defaultPortalInfo) {
        CreateLab.LOGGER.info("Teleporting " + entity.getDisplayName() + " to " + dest.dimension().getRegistryName().getPath());
        if (dest.dimension() == LabDimensions.LABDIM) {
            if (entity instanceof ServerPlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity) entity;
                entity.getCapability(LaboratoryCapability.LABORATORY_CAPABILITY).ifPresent(lab -> {
                    lab.savePlayerData(player);
                    lab.clearPlayerInventory(player);
                    player.setGameMode(GameType.CREATIVE);
                });
            }

            return new PortalInfo(Vector3d.atCenterOf(spawn), Vector3d.ZERO, entity.yRot, entity.xRot);
        } else {
            if (entity instanceof ServerPlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity) entity;
                ILaboratory lab = entity.getCapability(LaboratoryCapability.LABORATORY_CAPABILITY).orElseThrow(RuntimeException::new);

                lab.clearPlayerInventory(player);
                lab.restorePlayerData(player);
                return new PortalInfo(Vector3d.atCenterOf(lab.getPosition()), Vector3d.ZERO, entity.yRot, entity.xRot);
            }

            return new PortalInfo(Vector3d.atCenterOf(spawn), Vector3d.ZERO, entity.yRot, entity.xRot);
        }
    }

}
