package com.blukzen.createlab.commands;

import com.blukzen.createlab.CreateLab;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class ModCommands {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralCommandNode<CommandSource> commandSource = dispatcher.register(
                Commands.literal(CreateLab.MODID)
                        .then(CommandTeleport.register(dispatcher))
                        .then(CommandSaveInventory.register(dispatcher))
                        .then(CommandDebug.register(dispatcher))
        );

        dispatcher.register(Commands.literal("lab").redirect(commandSource));
    }

}
