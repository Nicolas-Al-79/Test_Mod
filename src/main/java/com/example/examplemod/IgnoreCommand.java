package com.example.examplemod;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class IgnoreCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("ignore")
                .then(Commands.argument("jogador", StringArgumentType.string())
                        .executes(context -> {
                            String playerName = StringArgumentType.getString(context, "jogador");

                            IgnoreManager.setIgnored(playerName, true);

                            context.getSource().sendSuccess(() ->
                                Component.translatable("command.mod_de_teste.ignore.ignored", playerName), false);

                            return 1;
                        })
                )
        );

        dispatcher.register(Commands.literal("unignore")
                .then(Commands.argument("jogador", StringArgumentType.string())
                        .executes(context -> {
                            String playerName = StringArgumentType.getString(context, "jogador");

                            IgnoreManager.setIgnored(playerName, false);

                            context.getSource().sendSuccess(() ->
                                Component.translatable("command.mod_de_teste.ignore.unignored", playerName), false);

                            return 1;
                        })
                )
        );
    }
}
