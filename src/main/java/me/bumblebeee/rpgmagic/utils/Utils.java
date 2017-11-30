package me.bumblebeee.rpgmagic.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Utils {

    public static Map<UUID, String> searchForPlayer(String name) {
        Storage storage = new Storage();
        YamlConfiguration c = storage.getStorageFile();
        if (c.getConfigurationSection("uuids") == null)
            return null;

        Map<UUID, String> players = new HashMap<>();
        for (String suuid : c.getConfigurationSection("uuids").getKeys(false)) {
            UUID uuid = UUID.fromString(suuid);
            Player t = Bukkit.getServer().getPlayer(uuid);
            if (t == null) {
                OfflinePlayer ot = Bukkit.getServer().getOfflinePlayer(uuid);
                if (!ot.hasPlayedBefore())
                    continue;

                if (ot.getName().toLowerCase().contains(name.toLowerCase()))
                    players.put(uuid, ot.getName());
            } else {
                if (t.getName().toLowerCase().contains(name.toLowerCase()))
                    players.put(uuid, t.getName());
            }
        }

        return players;
    }

    public static Map<UUID,String> getAllPlayers() {
        Storage storage = new Storage();
        YamlConfiguration c = storage.getStorageFile();
        if (c.getConfigurationSection("uuids") == null)
            return null;

        Map<UUID, String> players = new HashMap<>();
        for (String suuid : c.getConfigurationSection("uuids").getKeys(false)) {
            UUID uuid = UUID.fromString(suuid);
            Player t = Bukkit.getServer().getPlayer(uuid);
            if (t == null) {
                OfflinePlayer ot = Bukkit.getServer().getOfflinePlayer(uuid);
                players.put(uuid, ot.getName());
            } else {
                players.put(uuid, t.getName());
            }
        }

        return players;
    }
}
