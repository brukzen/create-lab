package com.blukzen.createlab.commands;

import com.blukzen.createlab.util.GUIUtil;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class CommandDebug implements Command<CommandSource> {
    private static final CommandDebug CMD = new CommandDebug();

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("debug")
                .requires(cs -> cs.hasPermission(0))
                .executes(CMD);
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        if (FMLEnvironment.dist.isClient()) {
            GUIUtil.INSTANCE.enabled = !GUIUtil.INSTANCE.enabled;
            return 0;
        }

        return 1;
    }
}
