package me.bumblebeee.rpgmagic.utils;

import me.bumblebeee.rpgmagic.RPGMagic;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Utils {

    public static Map<UUID, String> searchForPlayer(String name) {
        File folder = new File(RPGMagic.getInstance().getDataFolder() + File.separator + "data");
        File[] listOfFiles = folder.listFiles();
        Map<UUID, String> players = new HashMap<>();

        if (listOfFiles == null)
            return new HashMap<>();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (!listOfFiles[i].isFile())
                continue;

            String suuid = listOfFiles[i].getName().split("\\.")[0];
            if (suuid.equalsIgnoreCase("storage"))
                continue;

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
        File folder = new File(RPGMagic.getInstance().getDataFolder() + File.separator + "data");
        File[] listOfFiles = folder.listFiles();
        Map<UUID, String> players = new HashMap<>();

        if (listOfFiles == null)
            return new HashMap<>();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (!listOfFiles[i].isFile())
                continue;

            String suuid = listOfFiles[i].getName().split("\\.")[0];
            if (suuid.equalsIgnoreCase("storage"))
                continue;

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

    public static String getCardinalDirection(Location loc) {
        double rot = (loc.getYaw() - 90) % 360;
        if (rot < 0) {
            rot += 360.0;
        }
        return getDirection(rot);
    }

    private static String getDirection(double rot) {
        if (45 <= rot && rot < 135) {
            return "North";
        } else if (135 <= rot && rot < 225) {
            return "East";
        } else if (225 <= rot && rot < 315) {
            return "South";
        } else if (315 <= rot && rot < 360) {
            return "West";
        } else if (0 <= rot && rot < 45) {
            return "West";
        } else {
            return null;
        }
    }
}
