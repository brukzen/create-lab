package com.blukzen.createlab.network;

import com.blukzen.createlab.data.ILaboratory;
import com.blukzen.createlab.data.LaboratoryProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncLaboratory {

    private int entityId;
    private CompoundNBT laboratory;

    public PacketSyncLaboratory(int entityId, ILaboratory laboratory) {
        this.entityId = entityId;
        this.laboratory = (CompoundNBT) laboratory.serializeNBT();
    }

    public PacketSyncLaboratory(int entityId, CompoundNBT laboratory) {
        this.entityId = entityId;
        this.laboratory = laboratory;
    }

    public static void encode(PacketSyncLaboratory msg, PacketBuffer buf) {
        buf.writeInt(msg.entityId);
        buf.writeNbt(msg.laboratory);
    }

    public static PacketSyncLaboratory decode(PacketBuffer buf) {
        int entityId = buf.readInt();
        CompoundNBT laboratory = buf.readNbt();

        return new PacketSyncLaboratory(entityId, laboratory);
    }

    public static void handle(PacketSyncLaboratory msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientWorld world = Minecraft.getInstance().level;

            if (world != null) {
                Entity entity = world.getEntity(msg.entityId);

                if (entity instanceof PlayerEntity) {
                    LaboratoryProvider.getHandler((PlayerEntity) entity).ifPresent((handler) -> {
                        handler.deserializeNBT(msg.laboratory);
                    });
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}