package me.bumblebeee.rpgmagic.listeners.inventoryClicks;

import me.bumblebeee.rpgmagic.managers.InventoryManager;
import me.bumblebeee.rpgmagic.managers.Messages;
import me.bumblebeee.rpgmagic.utils.Storage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Admin implements Listener {

    InventoryManager inv = new InventoryManager();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        String playerAdminMenuTitle = inv.getMessage("inventory.playerAdminMenu.title", false);
        String playerAdminMainMenuTitle = inv.getMessage("inventory.playerAdminMainMenu.title", false);
        String adminPlayerMenuTitle = inv.getMessage("inventory.adminPlayerMenu.title", false);

        String title = e.getInventory().getTitle();

        if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta()
                && e.getCurrentItem().getItemMeta().hasDisplayName()) {
            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            if (clicked.equalsIgnoreCase("close"))
                p.closeInventory();
        }

        if (title.equalsIgnoreCase(playerAdminMenuTitle)) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null)
                return;
            if (!e.getCurrentItem().hasItemMeta())
                return;

            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            String spellMsg = inv.getMessage("inventory.playerAdminMenu.structure", false);
            String paperMsg = inv.getMessage("inventory.playerAdminMenu.paper", false);
            String wandMsg = inv.getMessage("inventory.playerAdminMenu.wand", false);

            if (clicked.equalsIgnoreCase(spellMsg))
                inv.openSpellsAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, 0, true);
            else if (clicked.equalsIgnoreCase(paperMsg))
                inv.openPaperAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, null, 0, true);
            else if (clicked.equalsIgnoreCase(wandMsg))
                inv.openWandAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, 0, true);
        } else if (title.equalsIgnoreCase(playerAdminMainMenuTitle)) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null)
                return;
            if (!e.getCurrentItem().hasItemMeta())
                return;

            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            String searchMsg = inv.getMessage("inventory.playerAdminMainMenu.search", false);
            String allMsg = inv.getMessage("inventory.playerAdminMainMenu.all", false);

            if (clicked.equalsIgnoreCase(searchMsg)) {
                p.closeInventory();
                Storage.getInputingSearch().add(p.getUniqueId());
                p.sendMessage(Messages.ENTER_A_PLAYER_NAME.get());
            } else if (clicked.equalsIgnoreCase(allMsg)) {
                inv.openAdminPlayers(p, null, 0);
            }
        } else if (title.equalsIgnoreCase(adminPlayerMenuTitle)) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null)
                return;
            if (!e.getCurrentItem().hasItemMeta())
                return;

            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            String nextMsg = inv.getMessage("inventory.adminPlayerMenu.next", false);
            String prevMsg = inv.getMessage("inventory.adminPlayerMenu.previous", false);

            if (clicked.equalsIgnoreCase(nextMsg)) {
                inv.openAdminPlayers(p, null, Storage.getPages().get(p.getUniqueId())+1);
            } else if (clicked.equalsIgnoreCase(prevMsg)) {
                inv.openAdminPlayers(p, null, Storage.getPages().get(p.getUniqueId())-1);
            } else if (e.getCurrentItem().getType() != Material.STAINED_GLASS_PANE) {
                Player t = Bukkit.getServer().getPlayer(clicked);
                if (t == null) {
                    OfflinePlayer ot = Bukkit.getServer().getOfflinePlayer(clicked);
                    Storage.getAdminMenuHolder().put(p.getUniqueId(), ot.getUniqueId());
                } else {
                    Storage.getAdminMenuHolder().put(p.getUniqueId(), t.getUniqueId());
                }

                inv.openAdminMenu(p);
            }
        }
    }
}
