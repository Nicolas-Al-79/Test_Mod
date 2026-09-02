package com.example.examplemod;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PunishmentManager extends SavedData {

    private static final String ID = "punishments_test";

    private static final String MUTED_KEY = "muted";
    private static final String FROZEN_KEY = "frozen";
    private static final String UUID_KEY = "UUID";

    private final Set<UUID> mutedPlayers = new HashSet<>();
    private final Set<UUID> frozenPlayers = new HashSet<>();

    private PunishmentManager() {
    }

    /*** Obtém o SavedData correspondente ao mapa.*
     * Independentemente da dimensão em que o jogador esteja,
     * os dados são armazenados no Overworld do mapa atual.*/
    private static PunishmentManager get(ServerLevel level) {

        ServerLevel overworld = level.getServer().overworld();

        return overworld.getDataStorage().computeIfAbsent(
                PunishmentManager::load,
                PunishmentManager::new,
                ID
        );
    }

    /**
     * Define se um jogador está mutado.
     */
    public static void setMuted(ServerLevel level, UUID uuid, boolean muted) {

        PunishmentManager data = get(level);

        if (muted) {
            data.mutedPlayers.add(uuid);
        } else {
            data.mutedPlayers.remove(uuid);
        }

        data.setDirty();
    }

    /**
     * Verifica se um jogador está mutado.
     */
    public static boolean isMuted(ServerLevel level, UUID uuid) {
        return get(level).mutedPlayers.contains(uuid);
    }

    /**
     * Define se um jogador está congelado.
     */
    public static void setFrozen(ServerLevel level, UUID uuid, boolean frozen) {

        PunishmentManager data = get(level);

        if (frozen) {
            data.frozenPlayers.add(uuid);
        } else {
            data.frozenPlayers.remove(uuid);
        }

        data.setDirty();
    }

    /**
     * Verifica se um jogador está congelado.
     */
    public static boolean isFrozen(ServerLevel level, UUID uuid) {
        return get(level).frozenPlayers.contains(uuid);
    }

    /**
     * Carrega os dados do arquivo .dat.
     */
    private static PunishmentManager load(CompoundTag tag) {

        PunishmentManager data = new PunishmentManager();

        /*
         * Carrega os jogadores mutados.
         */
        ListTag muted = tag.getList(
                MUTED_KEY,
                Tag.TAG_COMPOUND
        );

        for (Tag entry : muted) {

            if (entry instanceof CompoundTag uuidTag
                    && uuidTag.hasUUID(UUID_KEY)) {

                data.mutedPlayers.add(
                        uuidTag.getUUID(UUID_KEY)
                );
            }
        }

        /*
         * Carrega os jogadores congelados.
         */
        ListTag frozen = tag.getList(
                FROZEN_KEY,
                Tag.TAG_COMPOUND
        );

        for (Tag entry : frozen) {

            if (entry instanceof CompoundTag uuidTag
                    && uuidTag.hasUUID(UUID_KEY)) {

                data.frozenPlayers.add(
                        uuidTag.getUUID(UUID_KEY)
                );
            }
        }

        return data;
    }

    /**
     * Salva os dados no formato NBT.
     */
    @Override
    public CompoundTag save(CompoundTag tag) {

        /*
         * Salva os jogadores mutados.
         */
        ListTag muted = new ListTag();

        for (UUID uuid : mutedPlayers) {

            muted.add(
                    NbtUtils.createUUID(uuid)
            );
        }

        tag.put(
                MUTED_KEY,
                muted
        );

        /*
         * Salva os jogadores congelados.
         */
        ListTag frozen = new ListTag();

        for (UUID uuid : frozenPlayers) {

            frozen.add(
                    NbtUtils.createUUID(uuid)
            );
        }

        tag.put(
                FROZEN_KEY,
                frozen
        );

        return tag;
    }
}
