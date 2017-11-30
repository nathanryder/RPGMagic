package me.bumblebeee.rpgmagic.listeners;

import me.bumblebeee.rpgmagic.RPGMagic;
import me.bumblebeee.rpgmagic.events.AlterCraftEvent;
import me.bumblebeee.rpgmagic.managers.InventoryManager;
import me.bumblebeee.rpgmagic.utils.Storage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.Arrays;

public class InventoryClose implements Listener {

    Storage storage = new Storage();
    InventoryManager inv = new InventoryManager();

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        String name = RPGMagic.getInstance().getConfig().getString("alterName");
        String title = inv.getMessage("inventory.globalChest.title", false);
        Player p = (Player) e.getPlayer();
        if (e.getInventory().getName().equals(title)) {
            storage.setStorage(Storage.getGlobalChestStorage().get(0));
            return;
        }

        if (!e.getInventory().getName().equals(name))
            return;

        Storage.getOpenNPCs().remove(p.getUniqueId());
        Storage.getPages().remove(p.getUniqueId());
        AlterCraftEvent ev = new AlterCraftEvent(p, e.getInventory());
        Bukkit.getServer().getPluginManager().callEvent(ev);
        Storage.getOpenInventories().remove(p.getUniqueId());
    }

}