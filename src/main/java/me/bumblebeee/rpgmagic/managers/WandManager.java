package me.bumblebeee.rpgmagic.managers;

import me.bumblebeee.rpgmagic.utils.HiddenStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class WandManager {

    NPCManager npcs = new NPCManager();

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
