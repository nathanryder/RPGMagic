package me.bumblebeee.rpgmagic.listeners.inventoryClicks;

import me.bumblebeee.rpgmagic.managers.InventoryManager;
import me.bumblebeee.rpgmagic.utils.Storage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Spells implements Listener {

    InventoryManager inv = new InventoryManager();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        String playerSpellTitle = inv.getMessage("inventory.playerSpellMenu.title", false);

        String title = e.getInventory().getTitle();

        if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta()
                && e.getCurrentItem().getItemMeta().hasDisplayName()) {
            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            if (clicked.equalsIgnoreCase("close"))
                p.closeInventory();
        }

        if (title.equalsIgnoreCase(playerSpellTitle)) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null)
                return;
            if (!e.getCurrentItem().hasItemMeta())
                return;
            if (!e.getCurrentItem().getItemMeta().hasDisplayName())
                return;

            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            String nextMsg = inv.getMessage("inventory.playerSpellMenu.next", false);
            String prevMsg = inv.getMessage("inventory.playerSpellMenu.previous", false);

            if (clicked.equalsIgnoreCase(nextMsg)) {
                if (Storage.getPages().containsKey(p.getUniqueId())) {
                    int page = Storage.getPages().get(p.getUniqueId());
                    Storage.getPages().put(p.getUniqueId(), page+1);
                    inv.openSpellsMenu(p, page, false);
                } else {
                    Storage.getPages().put(p.getUniqueId(), 1);
                    inv.openSpellsMenu(p, 1, false);
                }
            } else if (clicked.equalsIgnoreCase(prevMsg)) {
                if (Storage.getPages().containsKey(p.getUniqueId())) {
                    int page = Storage.getPages().get(p.getUniqueId());
                    Storage.getPages().put(p.getUniqueId(), page-1);
                    inv.openSpellsMenu(p, page, false);
                } else {
                    Storage.getPages().put(p.getUniqueId(), 0);
                    inv.openSpellsMenu(p, 0, false);
                }
            } else if (e.getCurrentItem().getType() == Material.STRUCTURE_VOID) {
                p.getInventory().addItem(e.getCurrentItem());
            }
        }
    }
}
