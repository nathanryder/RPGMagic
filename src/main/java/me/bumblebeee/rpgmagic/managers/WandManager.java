package me.bumblebeee.rpgmagic.managers;

import me.bumblebeee.rpgmagic.utils.HiddenStringUtils;
import me.bumblebeee.rpgmagic.utils.Storage;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class WandManager {

    NPCManager npcs = new NPCManager();
    Storage storage = new Storage();

    public void removeWandFromPlayer(UUID uuid, String[] data) {
        int power = Integer.parseInt(data[0]);
        int level = Integer.parseInt(data[1]);
        String shape = data[2];
        int distance = Integer.parseInt(data[3]);
        String spell = data[4];

        YamlConfiguration c = storage.getPlayerFile(uuid);
        if (c.getConfigurationSection(uuid + ".wands") == null)
            return;

        for (String id : c.getConfigurationSection(uuid + ".wands").getKeys(false)) {
            if (id.equalsIgnoreCase("counter"))
                continue;
            int cPower = Integer.parseInt(c.getString(uuid + ".wands." + id + ".power"));
            int cLevel = Integer.parseInt(c.getString(uuid + ".wands." + id + ".level"));
            String cShape = c.getString(uuid + ".wands." + id + ".shape");
            int cDistance = Integer.parseInt(c.getString(uuid + ".wands." + id + ".distance"));
            String cSpell = c.getString(uuid + ".wands." + id + ".spell");

            if (power == cPower && level == cLevel
                    && shape.equalsIgnoreCase(cShape) && distance == cDistance
                    && spell.equalsIgnoreCase(cSpell))
                c.set(uuid + ".wands." + id, null);
        }

        storage.savePlayerFile(uuid, c);
    }

    public void addWandToNpc(ItemStack wand, int npcId, double price) {
        YamlConfiguration c = npcs.getFile();
        int counter = c.getInt("npcs." + npcId + ".wands.counter")+1;

        c.set("npcs." + npcId + ".wands." + counter + ".material", wand.getType().name());
        c.set("npcs." + npcId + ".wands." + counter + ".data", wand.getDurability());
        c.set("npcs." + npcId + ".wands." + counter + ".enchanted", wand.getItemMeta().hasEnchants());
        c.set("npcs." + npcId + ".wands." + counter + ".price", price);
        c.set("npcs." + npcId + ".wands." + counter + ".lore", wand.getItemMeta().getLore());
        c.set("npcs." + npcId + ".wands." + counter + ".data", HiddenStringUtils.extractHiddenString(wand.getItemMeta().getLore().get(wand.getItemMeta().getLore().size()-1)));
        if (wand.hasItemMeta() && wand.getItemMeta().hasDisplayName())
            c.set("npcs." + npcId + ".wands." + counter + ".display", wand.getItemMeta().getDisplayName());
        if (wand.hasItemMeta() && wand.getItemMeta().hasLore()) {
            List<String> lore = new ArrayList<>();
            for (String li : wand.getItemMeta().getLore())
                lore.add(ChatColor.translateAlternateColorCodes('&', li));
            c.set("npcs." + npcId + ".wands." + counter + ".lore", lore);
        }

        c.set("npcs." + npcId + ".wands.counter", counter);
        npcs.saveFile(c);
    }

    public void deleteWand(int id, int npcId) {
        YamlConfiguration c = npcs.getFile();

        c.set("npcs." + npcId + ".wands." + id, null);
        npcs.saveFile(c);
    }
}
