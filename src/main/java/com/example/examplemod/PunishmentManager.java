package com.example.examplemod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PunishmentManager{
    private static final String ID = "punishments_test";

    public static final Set<UUID> MUTED_PLAYERS = new HashSet<>();
    public static final Set<UUID> FROZEN_PLAYERS = new HashSet<>();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File FILE = FMLPaths.CONFIGDIR.get().resolve("punicoes.json").toFile();

    public static void setMuted(UUID uuid, boolean muted) {
        if (muted) MUTED_PLAYERS.add(uuid);
        else MUTED_PLAYERS.remove(uuid);
        save();
    }

    public static boolean isMuted(UUID uuid) {
        return MUTED_PLAYERS.contains(uuid);
    }

    public static void setFrozen(UUID uuid, boolean frozen) {
        if (frozen) FROZEN_PLAYERS.add(uuid);
        else FROZEN_PLAYERS.remove(uuid);
        save();
    }

    public static boolean isFrozen(UUID uuid) {
        return FROZEN_PLAYERS.contains(uuid);
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(FILE)) {
            Map<String, Set<UUID>> data = new HashMap<>();
            data.put("muted", MUTED_PLAYERS);
            data.put("frozen", FROZEN_PLAYERS);
            GSON.toJson(data, writer);
        } catch (Exception e) {
            System.out.println("Erro ao salvar punicoes.json: " + e.getMessage());
        }
    }

    public static void load() {
        if (FILE.exists()) {
            try (FileReader reader = new FileReader(FILE)) {
                Type type = new TypeToken<Map<String, Set<UUID>>>() {}.getType();
                Map<String, Set<UUID>> data = GSON.fromJson(reader, type);
                
                if (data != null) {
                    MUTED_PLAYERS.clear();
                    FROZEN_PLAYERS.clear();
                    if (data.containsKey("muted")) MUTED_PLAYERS.addAll(data.get("muted"));
                    if (data.containsKey("frozen")) FROZEN_PLAYERS.addAll(data.get("frozen"));
                }
            } catch (Exception e) {
                System.out.println("Erro ao carregar punicoes.json: " + e.getMessage());
            }
        }
    }
}
