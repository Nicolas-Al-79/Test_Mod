package com.example.examplemod;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ComandoAnuncio {

    @SubscribeEvent
    public static void registrarComandos(RegisterCommandsEvent event) {
        // Registra o comando de anúncio
        event.getDispatcher().register(Commands.literal("announcement")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("mensagem", StringArgumentType.greedyString())
                        .executes(context -> {
                            String mensagem = StringArgumentType.getString(context, "mensagem");
                            Component texto = Component.translatable("command.mod_de_teste.announcement", mensagem);
                            context.getSource().getServer().getPlayerList().broadcastSystemMessage(texto, false);
                            return 1;
                        }))
        );

        // Comando de proibir/permitir itens
        ForbidCommand.register(event.getDispatcher(), event.getBuildContext());

        // Curar jogadores
        HealCommand.register(event.getDispatcher());

        // Lixeira
        TrashCommand.register(event.getDispatcher());

        // Congelar / Descongelar
        FreezeCommand.register(event.getDispatcher());

        // Mutar / Desmutar (servidor)
        MuteCommand.register(event.getDispatcher());

        // Ignorar / Designorar (local)
        IgnoreCommand.register(event.getDispatcher());

        // AFK
        AFKCommand.register(event.getDispatcher());
    }
}
