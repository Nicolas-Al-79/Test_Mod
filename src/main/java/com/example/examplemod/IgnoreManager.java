package com.example.examplemod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class IgnoreManager {
    // Lista de nomes de jogadores ignorados localmente
    public static final Set<String> IGNORED_PLAYERS = new HashSet<>();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File FILE = FMLPaths.CONFIGDIR.get().resolve("jogadores_ignorados.json").toFile();

    public static void setIgnored(String playerName, boolean ignore) {
        if (ignore) IGNORED_PLAYERS.add(playerName.toLowerCase());
        else IGNORED_PLAYERS.remove(playerName.toLowerCase());
        save();
    }

    public static boolean isIgnored(String playerName) {
        return IGNORED_PLAYERS.contains(playerName.toLowerCase());
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(FILE)) {
            GSON.toJson(IGNORED_PLAYERS, writer);
        } catch (Exception e) {
            System.out.println("Erro ao salvar jogadores_ignorados.json: " + e.getMessage());
        }
    }

    public static void load() {
        if (FILE.exists()) {
            try (FileReader reader = new FileReader(FILE)) {
                Type type = new TypeToken<Set<String>>() {}.getType();
                Set<String> data = GSON.fromJson(reader, type);
                
                if (data != null) {
                    IGNORED_PLAYERS.clear();
                    IGNORED_PLAYERS.addAll(data);
                }
            } catch (Exception e) {
                System.out.println("Erro ao carregar jogadores_ignorados.json: " + e.getMessage());
            }
        }
    }
}
