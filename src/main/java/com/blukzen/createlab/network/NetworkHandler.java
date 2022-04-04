package com.blukzen.createlab.network;

import com.blukzen.createlab.CreateLab;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class NetworkHandler {

    private static final String PTC_VERSION = "1";
    private static int id = 0;
    public static SimpleChannel INSTANCE;

    public static void register() {
        INSTANCE = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(CreateLab.MODID, "main"))
                .networkProtocolVersion(() -> PTC_VERSION)
                .clientAcceptedVersions(PTC_VERSION::equals)
                .serverAcceptedVersions(PTC_VERSION::equals)
                .simpleChannel();

        register(PacketSyncLaboratory.class, PacketSyncLaboratory::encode, PacketSyncLaboratory::decode, PacketSyncLaboratory::handle);
    }

    private static <M> void register(Class<M> messageType, BiConsumer<M, PacketBuffer> encoder,
                                     Function<PacketBuffer, M> decoder,
                                     BiConsumer<M, Supplier<NetworkEvent.Context>> messageConsumer) {
        INSTANCE.registerMessage(id++, messageType, encoder, decoder, messageConsumer);
    }
}
