package com.blukzen.createlab.commands;

import com.blukzen.createlab.data.LaboratoryCapability;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;

public class CommandSaveInventory implements Command<CommandSource> {
    private static final CommandSaveInventory CMD = new CommandSaveInventory();

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("save")
                .requires(cs -> cs.hasPermission(0))
                .executes(CMD);
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrException();

        player.getCapability(LaboratoryCapability.LABORATORY_CAPABILITY).ifPresent(lab -> {
            lab.savePlayerData(player);
        });

        return 0;
    }
}
