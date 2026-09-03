package com.example.examplemod.command;

import com.example.examplemod.manager.AFKManager;
import com.example.examplemod.Config;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class AFKCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("afk")
                .requires(source -> source.hasPermission(Config.COMMAND_AFK_PERMISSION_LEVEL.get()))
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    AFKManager.startPending(player);
                    return 1;
                })
        );
    }
}
