package me.bumblebeee.rpgmagic.managers;

import me.bumblebeee.rpgmagic.RPGMagic;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NPCManager {

    public void setNPCType(int npcId, String type) {
        YamlConfiguration c = getFile();
        c.set("npcs." + npcId + ".type", type.toLowerCase());
        saveFile(c);
    }

    public YamlConfiguration getFile() {
        File f = new File(RPGMagic.getInstance().getDataFolder() + File.separator + "npcs.yml");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return YamlConfiguration.loadConfiguration(f);
    }

    public void saveFile(YamlConfiguration c) {
        File f = new File(RPGMagic.getInstance().getDataFolder() + File.separator + "npcs.yml");
        try {
            c.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
