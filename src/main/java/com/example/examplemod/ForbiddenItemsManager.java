package com.example.examplemod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.loading.FMLPaths;
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
    
    // O arquivo vai ficar salvo na pasta 'config' do servidor/jogo
    private static final File FILE = FMLPaths.CONFIGDIR.get().resolve("itens_proibidos.json").toFile();

    public static void forbidItem(UUID playerUUID, Item item) {
        String itemName = ForgeRegistries.ITEMS.getKey(item).toString();
        FORBIDDEN_ITEMS.computeIfAbsent(playerUUID, k -> new HashSet<>()).add(itemName);
        save(); // Salva toda vez que uma alteração é feita
    }

    public static void allowItem(UUID playerUUID, Item item) {
        String itemName = ForgeRegistries.ITEMS.getKey(item).toString();
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
        String itemName = ForgeRegistries.ITEMS.getKey(item).toString();
        return FORBIDDEN_ITEMS.containsKey(playerUUID) && FORBIDDEN_ITEMS.get(playerUUID).contains(itemName);
    }

    // Método para salvar os dados no arquivo
    public static void save() {
        try (FileWriter writer = new FileWriter(FILE)) {
            GSON.toJson(FORBIDDEN_ITEMS, writer);
        } catch (Exception e) {
            System.out.println("Erro ao salvar itens_proibidos.json: " + e.getMessage());
        }
    }

    // Método para ler os dados do arquivo
    public static void load() {
        if (FILE.exists()) {
            try (FileReader reader = new FileReader(FILE)) {
                Type type = new TypeToken<Map<UUID, Set<String>>>() {}.getType();
                Map<UUID, Set<String>> data = GSON.fromJson(reader, type);
                
                if (data != null) {
                    FORBIDDEN_ITEMS.clear();
                    FORBIDDEN_ITEMS.putAll(data);
                }
            } catch (Exception e) {
                System.out.println("Erro ao carregar itens_proibidos.json: " + e.getMessage());
            }
        }
    }
}
