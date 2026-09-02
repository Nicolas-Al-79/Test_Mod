package com.example.examplemod;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class MuteCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("mute")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("alvos", EntityArgument.players())
                        .executes(context -> {
                            Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "alvos");

                            for (ServerPlayer player : players) {
                                PunishmentManager.setMuted(player, true);
                                player.sendSystemMessage(Component.translatable("command.mod_de_teste.mute.muted"));
                            }

                            context.getSource().sendSuccess(() ->
                                    Component.translatable("command.mod_de_teste.mute.success", players.size()), true);

                            return players.size();
                        })
                )
        );

        dispatcher.register(Commands.literal("unmute")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("alvos", EntityArgument.players())
                        .executes(context -> {
                            Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "alvos");

                            for (ServerPlayer player : players) {
                                PunishmentManager.setMuted(player, false);
                                player.sendSystemMessage(Component.translatable("command.mod_de_teste.unmute.unmuted"));
                            }

                            context.getSource().sendSuccess(() ->
                                    Component.translatable("command.mod_de_teste.unmute.success", players.size()), true);

                            return players.size();
                        })
                )
        );
    }
}
