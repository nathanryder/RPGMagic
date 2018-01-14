package me.bumblebeee.rpgmagic.managers;

import me.bumblebeee.rpgmagic.RPGMagic;
import me.bumblebeee.rpgmagic.Spell;
import me.bumblebeee.rpgmagic.utils.HiddenStringUtils;
import me.bumblebeee.rpgmagic.utils.Storage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StructureManager {

    Storage storage = new Storage();

    public double getPrice(String id) {
        FileConfiguration c = RPGMagic.getInstance().getConfig();
        id = id.toLowerCase();

        double price = c.getDouble("spells." + id + ".price");
        return price;
    }

    public ItemStack getItemByName(String id, boolean addLore) {
        id = id.toLowerCase();
        if (!SpellManager.getSpells().containsKey(id))
            return null;

        Spell spell = SpellManager.getSpell(id);
        YamlConfiguration c = YamlConfiguration.loadConfiguration(spell.getFile());

        String displayName = ChatColor.translateAlternateColorCodes('&', c.getString("name"));
        String type = ChatColor.translateAlternateColorCodes('&', spell.getType());
        String desc = ChatColor.translateAlternateColorCodes('&', spell.getDescription());
        String price = String.valueOf(c.getInt("price"));

        FileConfiguration fc = RPGMagic.getInstance().getConfig();
        Material m;
        try {
            m = Material.matchMaterial(fc.getString("spellItem.item"));
        } catch (Exception e) {
            return null;
        }
        short data = (short) fc.getInt("spellItem.data");
        String display = fc.getString("spellItem.name");
        List<String> lore = new ArrayList<>();

        for (String loreItem : fc.getStringList("spellItem.lore")) {
            loreItem = ChatColor.translateAlternateColorCodes('&', loreItem);
            loreItem = loreItem.replace("%name%", displayName);
            loreItem = loreItem.replace("%type%", type);
            loreItem = loreItem.replace("%desc%", desc);
            lore.add(loreItem);
        }
        if (addLore) {
            for (String loreItem : fc.getStringList("spellItem.addLore"))
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

    public void removeSpellFromPlayer(UUID uuid, String spell) {
        YamlConfiguration c = storage.getPlayerFile(uuid);
        if (c.getStringList(uuid + ".spells") == null)
            return;

        List<String> spells = c.getStringList(uuid + ".spells");
        List<String> remove = new ArrayList<>();
        for (String id : spells) {
            if (id.equalsIgnoreCase(spell))
                remove.add(id);
        }

        spells.removeAll(remove);
        c.set(uuid + ".spells", spells);
        storage.savePlayerFile(uuid, c);
    }
}
