package com.example.examplemod.manager;

import com.example.examplemod.ExampleMod;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ForbiddenItemsManager {
    // Agora guardamos o nome do item como String (ex: "minecraft:diamond") para facilitar a leitura no JSON
    public static final Map<UUID, Set<String>> FORBIDDEN_ITEMS = new HashMap<>();

    // O GSON é a ferramenta do Google (já inclusa no Minecraft) para transformar código em texto JSON
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    // Arquivo salvo dentro da pasta do mundo
    private static File getFile() {
        return ExampleMod
                .getWorldDataFolder()
                .resolve("forbidden_items.json")
                .toFile();
    }

    public static void forbidItem(UUID playerUUID, Item item) {
        ResourceLocation key = ForgeRegistries.ITEMS.getKey(item);
        if (key == null) return;
        String itemName = key.toString();
        FORBIDDEN_ITEMS.computeIfAbsent(playerUUID, k -> new HashSet<>()).add(itemName);
        save(); // Salva toda vez que uma alteração é feita
    }

    public static void allowItem(UUID playerUUID, Item item) {
        ResourceLocation key = ForgeRegistries.ITEMS.getKey(item);
        if (key == null) return;
        String itemName = key.toString();
        if (FORBIDDEN_ITEMS.containsKey(playerUUID)) {
            FORBIDDEN_ITEMS.get(playerUUID).remove(itemName);
            
            // Se a lista do jogador ficar vazia, remove o jogador do arquivo pra ficar mais limpo
            if (FORBIDDEN_ITEMS.get(playerUUID).isEmpty()) {
                FORBIDDEN_ITEMS.remove(playerUUID);
            }
            save(); // Salva após remover
        }
    }

    public static boolean isForbidden(UUID playerUUID, Item item) {
        ResourceLocation key = ForgeRegistries.ITEMS.getKey(item);
        if (key == null) return false;
        String itemName = key.toString();
        return FORBIDDEN_ITEMS.containsKey(playerUUID) && FORBIDDEN_ITEMS.get(playerUUID).contains(itemName);
    }

    // Metodo para salvar os dados no arquivo
    public static void save() {
        File file = getFile();

        try (FileWriter writer = new FileWriter(file)) {
            GSON.toJson(FORBIDDEN_ITEMS, writer);
        } catch (Exception e) {
            System.out.println(
                    "Error to save forbidden_items.json: "
                            + e.getMessage()
            );
        }
    }

    // Metodo para ler os dados do arquivo
    public static void load() {
        FORBIDDEN_ITEMS.clear();

        File file = getFile();

        if (!file.exists()) {
            return;
        }

        try (FileReader reader = new FileReader(file)) {
            Type type = new TypeToken<Map<UUID, Set<String>>>() {}.getType();
            Map<UUID, Set<String>> data = GSON.fromJson(reader, type);
            if (data != null) {
                FORBIDDEN_ITEMS.putAll(data);
            }
        } catch (Exception e) {
            System.out.println(
                    "Error to load forbidden_items.json: " + e.getMessage()
            );
        }
    }
}
