package me.bumblebeee.rpgmagic.listeners;

import me.bumblebeee.rpgmagic.RPGMagic;
import me.bumblebeee.rpgmagic.Wand;
import me.bumblebeee.rpgmagic.events.AlterCraftEvent;
import me.bumblebeee.rpgmagic.managers.InventoryManager;
import me.bumblebeee.rpgmagic.managers.StructureManager;
import me.bumblebeee.rpgmagic.utils.HiddenStringUtils;
import me.bumblebeee.rpgmagic.utils.Storage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class InventoryClose implements Listener {

    Storage storage = new Storage();
    InventoryManager inv = new InventoryManager();
    StructureManager spellManager = new StructureManager();

    //TODO admin add/remove

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        String name = RPGMagic.getInstance().getConfig().getString("alterName");
        String playerPaperAdminTitle = inv.getMessage("inventory.playerPaperAdminMenu.title", false);
        String playerWandAdminTitle = inv.getMessage("inventory.playerWandAdminMenu.title", false);
        String playerAdminSpellTitle = inv.getMessage("inventory.playerSpellAdminMenu.title", false);

        String globalChestTitle = inv.getMessage("inventory.globalChest.title", false);
        String title = e.getInventory().getName();

        Player p = (Player) e.getPlayer();
        if (title.equalsIgnoreCase(globalChestTitle)) {
            storage.setStorage(Storage.getGlobalChestStorage().get(0));
            return;
        } else if (title.equalsIgnoreCase(name)) {
            Storage.getOpenNPCs().remove(p.getUniqueId());
            Storage.getPages().remove(p.getUniqueId());
            AlterCraftEvent ev = new AlterCraftEvent(p, e.getInventory());
            Bukkit.getServer().getPluginManager().callEvent(ev);
            Storage.getOpenInventories().remove(p.getUniqueId());
        } else if (title.equalsIgnoreCase(playerAdminSpellTitle)) {
            List<String> spells = storage.getSpellsForPlayer(p.getUniqueId());
            for (ItemStack i : e.getInventory().getContents()) {
                if (i == null)
                    continue;
                if (!i.hasItemMeta())
                    continue;
                if (!i.getItemMeta().hasLore())
                    continue;
                int index = i.getItemMeta().getLore().size()-1;

                if (!HiddenStringUtils.hasHiddenString(i.getItemMeta().getLore().get(index)))
                    continue;
                String spell = HiddenStringUtils.extractHiddenString(i.getItemMeta().getLore().get(index)).split(":")[1];

                if (spells.contains(spell))
                    continue;

                storage.addSpellPlayer(p.getUniqueId(), spell);
            }
        } else if (title.equalsIgnoreCase(playerPaperAdminTitle)) {
            for (ItemStack i : e.getInventory().getContents()) {
                if (i == null)
                    continue;
                if (!i.hasItemMeta())
                    continue;
                if (!i.getItemMeta().hasLore())
                    continue;
                int index = i.getItemMeta().getLore().size()-1;

                if (!HiddenStringUtils.hasHiddenString(i.getItemMeta().getLore().get(index)))
                    continue;
                String[] data = HiddenStringUtils.extractHiddenString(i.getItemMeta().getLore().get(index)).split(":");

                if (data[0].equalsIgnoreCase("level") &&
                            Storage.getCategorySelector().get(p.getUniqueId()).equalsIgnoreCase("level")) {
                    storage.addPaperPlayer(p.getUniqueId(), "level", Integer.parseInt(data[1]), null);
                } else if (data[0].equalsIgnoreCase("power") &&
                        Storage.getCategorySelector().get(p.getUniqueId()).equalsIgnoreCase("power")) {
                    storage.addPaperPlayer(p.getUniqueId(), "power", Integer.parseInt(data[1]), null);
                } else if (data[0].equalsIgnoreCase("shape") &&
                        Storage.getCategorySelector().get(p.getUniqueId()).equalsIgnoreCase("effect area")) {
                    String shape = data[1].split("\\|")[0];
                    storage.addPaperPlayer(p.getUniqueId(), "effect area", Integer.parseInt(data[2]), shape);
                } else {
                    p.sendRawMessage(ChatColor.RED + "FAILED");
                    p.closeInventory();
                }
            }
        } else if (title.equalsIgnoreCase(playerWandAdminTitle)) {
            List<ItemStack> wands = storage.getPlayerWands(p.getUniqueId());
            for (ItemStack i : e.getInventory().getContents()) {
                if (i == null)
                    continue;
                Wand wand = new Wand(i);

                if (wand.getItem() == null)
                    continue;
                if (wands.contains(i))
                    continue;

                YamlConfiguration c = spellManager.getFile();
                String desc = ChatColor.translateAlternateColorCodes('&', c.getString("spells." + wand.getSpell() + ".description"));
                String data = wand.getPower() + ":" + wand.getLevel() + ":" + wand.getShape() + ":" + wand.getDistance() + ":" + wand.getSpell() + ":" + i.getType() + ":" + desc;

                storage.addWandPlayer(p.getUniqueId(), data);
            }
        }
    }

}