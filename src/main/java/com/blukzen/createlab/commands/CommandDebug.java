package com.blukzen.createlab.commands;

import com.blukzen.createlab.CreateLabConfig;
import com.blukzen.createlab.util.GUIUtil;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class CommandDebug implements Command<CommandSource>, SuggestionProvider<CommandSource> {
    private static final CommandDebug CMD = new CommandDebug();

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("debug")
                .requires(cs -> cs.hasPermission(CreateLabConfig.debugCommandPermissionLevel.get()))
                .executes(CMD)
                .then(
                        RequiredArgumentBuilder.<CommandSource, String>argument("args", StringArgumentType.greedyString())
                                .suggests(CMD)
                                .executes(CMD)
                );
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        String[] args = processArgs(context, true);
        if (FMLEnvironment.dist.isClient()) {

            if (args != null && args.length > 0) {
                if (Objects.equals(args[0], "clear")) {
                    GUIUtil.INSTANCE.clear();
                    return 0;
                }
            }

            GUIUtil.INSTANCE.enabled = !GUIUtil.INSTANCE.enabled;
            return 0;
        }

        return 1;
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        builder.suggest("clear");
        return builder.buildFuture();
    }

    private static String[] processArgs(CommandContext<CommandSource> context, boolean tabComplete) {
        String[] split = context.getInput().replace("/lab debug", "").split(" ", tabComplete ? -1 : 0);
        if (split.length == 0) {
            return null;
        }

        return Arrays.copyOfRange(split, 1, split.length);
    }

}
