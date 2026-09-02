package com.example.examplemod;

import net.minecraft.server.level.ServerPlayer;

public class PunishmentManager {
    private static final String NBT_KEY = "mod_de_teste";
    private static final String FROZEN_KEY = "frozen";
    private static final String MUTED_KEY = "muted";

    /*** Define se o jogador está congelado.*/
    public static void setFrozen(ServerPlayer player, boolean frozen) {
        player.getPersistentData()
                .getCompound(NBT_KEY)
                .putBoolean(FROZEN_KEY, frozen);
    }

    /*** Verifica se o jogador está congelado.*/
    public static boolean isFrozen(ServerPlayer player) {
        return player.getPersistentData()
                .getCompound(NBT_KEY)
                .getBoolean(FROZEN_KEY);
    }

    /*** Define se o jogador está mutado.*/
    public static void setMuted(ServerPlayer player, boolean muted) {
        player.getPersistentData()
                .getCompound(NBT_KEY)
                .putBoolean(MUTED_KEY, muted);
    }

    /*** Verifica se o jogador está mutado.*/
    public static boolean isMuted(ServerPlayer player) {
        return player.getPersistentData()
                .getCompound(NBT_KEY)
                .getBoolean(MUTED_KEY);
    }
}