package com.blukzen.createlab.data;

import com.blukzen.createlab.CreateLab;
import com.blukzen.createlab.network.NetworkHandler;
import com.blukzen.createlab.network.PacketSyncLaboratory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;

public class LaboratoryEventHandler {
    @SubscribeEvent
    public void attachEntitiesCapabilities(AttachCapabilitiesEvent<Entity> evt) {
        Entity entity = evt.getObject();

        if (entity instanceof PlayerEntity) {
            evt.addCapability(new ResourceLocation(CreateLab.MODID, LaboratoryCapability.LABORATORY_KEY),
                    LaboratoryCapability.createProvider((PlayerEntity) entity));
        }
    }

    @SubscribeEvent
    public void playerClone(PlayerEvent.Clone evt) {
        PlayerEntity player = evt.getPlayer();
        PlayerEntity oldPlayer = evt.getOriginal();
        oldPlayer.revive();

        LazyOptional<ILaboratory> oldHandler = LaboratoryProvider.getHandler(oldPlayer);
        LazyOptional<ILaboratory> newHandler = LaboratoryProvider.getHandler(player);
        oldHandler.ifPresent(
                oldLaboratory -> newHandler.ifPresent(newLaboratory -> newLaboratory.deserializeNBT(oldLaboratory.serializeNBT())));
    }

    @SubscribeEvent
    public void entityJoinWorld(EntityJoinWorldEvent evt) {
        Entity entity = evt.getEntity();

        if (entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;

            LaboratoryProvider.getHandler(player).ifPresent(handler -> {
                ServerPlayerEntity mp = (ServerPlayerEntity) entity;
                NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> mp),
                        new PacketSyncLaboratory(mp.getId(), handler));
            });
        }
    }
}
