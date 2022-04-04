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
import net.minecraft.util.text.StringTextComponent;

public class CommandDebug implements Command<CommandSource> {
    private static final CommandDebug CMD = new CommandDebug();

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("debug")
                .requires(cs -> cs.hasPermission(0))
                .executes(CMD);
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrException();

        player.getCapability(LaboratoryCapability.LABORATORY_CAPABILITY).ifPresent(lab -> {
            player.sendMessage(new StringTextComponent("Gamemode: " + lab.getGamemode().getName()), null);
            player.sendMessage(new StringTextComponent("Position: " + lab.getPosition().toString()), null);
            player.sendMessage(new StringTextComponent("Items: " + lab.getSavedItems().toString()), null);
        });

        return 0;
    }
}
