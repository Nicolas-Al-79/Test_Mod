package com.example.examplemod.command;

import com.example.examplemod.Config;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class HealCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("heal")
                .requires(source -> source.hasPermission(Config.COMMAND_HEAL_PERMISSION_LEVEL.get()))
                .then(Commands.argument("alvos", EntityArgument.players())
                        .executes(context -> {
                            Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "alvos");

                            for (ServerPlayer player : players) {
                                player.setHealth(player.getMaxHealth());
                                player.getFoodData().setFoodLevel(20);
                                player.getFoodData().setSaturation(20.0f);
                                player.removeAllEffects();
                            }

                            context.getSource().sendSuccess(() ->
                                    Component.translatable("command.mod_de_teste.heal.success", players.size()), true);

                            return players.size();
                        })
                )
        );
    }
}
