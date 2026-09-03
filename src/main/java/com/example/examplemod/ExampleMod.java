package com.example.examplemod;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExampleMod.MODID)
public class ExampleMod
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "mod_de_teste";

    public ExampleMod(FMLJavaModLoadingContext context)
    {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Carrega os itens proibidos salvos no arquivo JSON quando o mundo/servidor ligar
        ForbiddenItemsManager.load();
        
        // Carrega a lista de jogadores ignorados localmente
        IgnoreManager.load();
    }

    @SubscribeEvent
    public void registrarComandos(RegisterCommandsEvent event)
    {
        // Comando de anúncio
        event.getDispatcher().register(
                Commands.literal("announcement")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument(
                                "mensagem",
                                StringArgumentType.greedyString()
                        ).executes(context -> {
                            String mensagem = StringArgumentType.getString(
                                    context,
                                    "mensagem"
                            );

                            Component texto = Component.translatable(
                                    "command.mod_de_teste.announcement",
                                    mensagem
                            );

                            context.getSource().getServer().getPlayerList()
                                    .broadcastSystemMessage(texto, false);

                            return 1;
                        }))
        );

        // Comando de proibir/permitir itens
        ForbidCommand.register(
                event.getDispatcher(),
                event.getBuildContext()
        );

        // Curar jogadores
        HealCommand.register(event.getDispatcher());

        // Lixeira
        TrashCommand.register(event.getDispatcher());

        // Congelar / Descongelar
        FreezeCommand.register(event.getDispatcher());

        // Mutar / Desmutar
        MuteCommand.register(event.getDispatcher());

        // Ignorar / Designorar
        IgnoreCommand.register(event.getDispatcher());

        // AFK
        AFKCommand.register(event.getDispatcher());
    }
}