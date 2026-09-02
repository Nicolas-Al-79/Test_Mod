package com.example.examplemod;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class FreezeCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("freeze")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("alvos", EntityArgument.players())
                        .executes(context -> {
                            Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "alvos");

                            for (ServerPlayer player : players) {
                                PunishmentManager.setFrozen(player, true);
                                player.sendSystemMessage(Component.translatable("command.mod_de_teste.freeze.frozen"));
                            }

                            context.getSource().sendSuccess(() ->
                                    Component.translatable("command.mod_de_teste.freeze.success", players.size()), true);

                            return players.size();
                        })
                )
        );

        dispatcher.register(Commands.literal("unfreeze")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("alvos", EntityArgument.players())
                        .executes(context -> {
                            Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "alvos");

                            for (ServerPlayer player : players) {
                                PunishmentManager.setFrozen(player, false);
                                player.sendSystemMessage(Component.translatable("command.mod_de_teste.unfreeze.unfrozen"));
                            }

                            context.getSource().sendSuccess(() ->
                                    Component.translatable("command.mod_de_teste.unfreeze.success", players.size()), true);

                            return players.size();
                        })
                )
        );
    }
}
