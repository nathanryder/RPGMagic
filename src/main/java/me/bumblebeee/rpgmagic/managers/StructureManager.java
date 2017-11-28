package me.bumblebeee.rpgmagic.managers;

import me.bumblebeee.rpgmagic.RPGMagic;
import me.bumblebeee.rpgmagic.utils.HiddenStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StructureManager {

    public double getPrice(String id) {
        YamlConfiguration c = getFile();
        id = id.toLowerCase();

        double price = c.getDouble("spells." + id + ".price");
        return price;
    }

    public ItemStack getItemById(String id, boolean addLore) {
        YamlConfiguration c = getFile();
        id = id.toLowerCase();
        if (c.getConfigurationSection("spells." + id) == null)
            return null;

        String displayName = ChatColor.translateAlternateColorCodes('&', c.getString("spells." + id + ".name"));
        String type = ChatColor.translateAlternateColorCodes('&', c.getString("spells." + id + ".type"));
        String desc = ChatColor.translateAlternateColorCodes('&', c.getString("spells." + id + ".description"));
        String price = ChatColor.translateAlternateColorCodes('&', c.getString("spells." + id + ".price"));

        Material m;
        try {
            m = Material.matchMaterial(c.getString("spellItem.item"));
        } catch (Exception e) {
            return null;
        }
        short data = (short) c.getInt("spellItem.data");
        String display = c.getString("spellItem.name");
        List<String> lore = new ArrayList<>();

        for (String loreItem : c.getStringList("spellItem.lore")) {
            loreItem = ChatColor.translateAlternateColorCodes('&', loreItem);
            loreItem = loreItem.replace("%name%", displayName);
            loreItem = loreItem.replace("%type%", type);
            loreItem = loreItem.replace("%desc%", desc);
            lore.add(loreItem);
        }
        if (addLore) {
            for (String loreItem : c.getStringList("spellItem.addLore"))
                lore.add(ChatColor.translateAlternateColorCodes('&', loreItem.replace("%price%", price)));
        }
        lore.add(HiddenStringUtils.encodeString("ID:" + id));

        ItemStack item = new ItemStack(m, 1, data);
        ItemMeta im = item.getItemMeta();
        item.setDurability(data);
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', display));
        im.setLore(lore);
        item.setItemMeta(im);

        return item;
    }

    public YamlConfiguration getFile() {
        File f = new File(RPGMagic.getInstance().getDataFolder() + File.separator + "spells.yml");
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
        File f = new File(RPGMagic.getInstance().getDataFolder() + File.separator + "spells.yml");
        try {
            c.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
