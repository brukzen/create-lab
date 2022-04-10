package com.blukzen.createlab.commands;

import com.blukzen.createlab.data.LaboratoryCapability;
import com.blukzen.createlab.dimension.LabDimensions;
import com.blukzen.createlab.util.TeleportationTools;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class CommandTeleport implements Command<CommandSource> {

    private static final CommandTeleport CMD = new CommandTeleport();

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("tp")
                .requires(cs -> cs.hasPermission(0))
                .executes(CMD);
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrException();

        if (player.getCommandSenderWorld().dimension().equals(LabDimensions.LABDIM)) {
            ServerWorld world = player.getServer().getLevel(World.OVERWORLD);

            player.getCapability(LaboratoryCapability.LABORATORY_CAPABILITY).ifPresent(lab -> {
                lab.resetPlayer(player);
                lab.restorePlayerData(player);
                TeleportationTools.teleport(player, world, lab.getPosition());
            });
        } else {
            ServerWorld world = player.getServer().getLevel(LabDimensions.LABDIM);

            player.getCapability(LaboratoryCapability.LABORATORY_CAPABILITY).ifPresent(lab -> {
                lab.savePlayerData(player);
                lab.resetPlayer(player);
                player.setGameMode(GameType.CREATIVE);
                TeleportationTools.teleport(player, world, new BlockPos(0, 65, 0));
            });
        }

        return 0;
    }

}