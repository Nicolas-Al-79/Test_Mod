package com.example.examplemod;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(
        modid = ExampleMod.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE,
        value = Dist.CLIENT
)
public class ClientEvents {

    @SubscribeEvent
    public static void registerClientCommands(
            RegisterClientCommandsEvent event
    ) {
        if (Config.COMMAND_IGNORE_ENABLED.get()) {
            IgnoreCommand.register(event.getDispatcher());
        }
    }

    @SubscribeEvent
    public static void onClientChatReceived(
            ClientChatReceivedEvent.Player event
    ) {
        if (IgnoreManager.isIgnored(
                event.getSender()
        )) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        IgnoreManager.load();
    }
}
