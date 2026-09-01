package com.example.examplemod;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ChestMenu;

public class TrashCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("trash")
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();

                    SimpleContainer container = new SimpleContainer(27);

                    player.openMenu(new SimpleMenuProvider(
                            (id, inventory, playerEntity) -> ChestMenu.threeRows(id, inventory, container),
                            Component.translatable("command.mod_de_teste.trash.title")
                    ));

                    return 1;
                })
        );
    }
}
