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

import java.util.UUID;

public class Admin implements Listener {

    InventoryManager inv = new InventoryManager();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        String playerAdminMenuTitle = inv.getMessage("inventory.playerAdminMenu.title", false);
        String playerAdminMainMenuTitle = inv.getMessage("inventory.playerAdminMainMenu.title", false);
        String adminPlayerMenuTitle = inv.getMessage("inventory.adminPlayerMenu.title", false);
        String playerAdminSpellTitle = inv.getMessage("inventory.playerSpellAdminMenu.title", false);
        String playerWandAdminTitle = inv.getMessage("inventory.playerWandAdminMenu.title", false);
        String playerAdminPaperSelectTitle = inv.getMessage("inventory.playerPaperAdminSelectMenu.title", false);
        String playerPaperAdminTitle = inv.getMessage("inventory.playerPaperAdminMenu.title", false);

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
        } else if (title.equalsIgnoreCase(playerAdminSpellTitle)) {
            if (e.getCurrentItem() == null)
                return;
            if (!e.getCurrentItem().hasItemMeta())
                return;
            if (!e.getCurrentItem().getItemMeta().hasDisplayName())
                return;

            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            String nextMsg = inv.getMessage("inventory.playerSpellAdminMenu.next", false);
            String prevMsg = inv.getMessage("inventory.playerSpellAdminMenu.previous", false);

            UUID target = Storage.getAdminMenuHolder().get(p.getUniqueId());
            if (clicked.equalsIgnoreCase(nextMsg)) {
                e.setCancelled(true);
                if (Storage.getPages().containsKey(p.getUniqueId())) {
                    int page = Storage.getPages().get(p.getUniqueId());
                    Storage.getPages().put(p.getUniqueId(), page+1);
                    inv.openSpellsAdminMenu(target, p, page, false);
                } else {
                    Storage.getPages().put(p.getUniqueId(), 1);
                    inv.openSpellsAdminMenu(target, p, 1, false);
                }
            } else if (clicked.equalsIgnoreCase(prevMsg)) {
                e.setCancelled(true);
                if (Storage.getPages().containsKey(p.getUniqueId())) {
                    int page = Storage.getPages().get(p.getUniqueId());
                    Storage.getPages().put(p.getUniqueId(), page-1);
                    inv.openSpellsMenu(p, page, false);
                } else {
                    Storage.getPages().put(p.getUniqueId(), 0);
                    inv.openSpellsAdminMenu(target, p, 0, false);
                }
            } else if (e.getCurrentItem().getType() == Material.STAINED_GLASS_PANE) {
                e.setCancelled(true);
            }
        } else if (title.equalsIgnoreCase(playerWandAdminTitle)) {
            if (e.getCurrentItem() == null)
                return;
            if (!e.getCurrentItem().hasItemMeta())
                return;
            if (!e.getCurrentItem().getItemMeta().hasDisplayName())
                return;

            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            String nextMsg = inv.getMessage("inventory.playerWandAdminMenu.next", false);
            String prevMsg = inv.getMessage("inventory.playerWandAdminMenu.previous", false);

            if (clicked.equalsIgnoreCase(nextMsg)) {
                e.setCancelled(true);
                if (Storage.getPages().containsKey(p.getUniqueId())) {
                    int page = Storage.getPages().get(p.getUniqueId());

                    inv.openWandAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, page, false);
                    Storage.getPages().put(p.getUniqueId(), page+1);
                } else {
                    inv.openWandAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, 1, false);
                    Storage.getPages().put(p.getUniqueId(), 1);
                }
            } else if (clicked.equalsIgnoreCase(prevMsg)) {
                e.setCancelled(true);
                if (Storage.getPages().containsKey(p.getUniqueId())) {
                    int page = Storage.getPages().get(p.getUniqueId());

                    inv.openWandAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, page, false);
                    Storage.getPages().put(p.getUniqueId(), page-1);
                } else {
                    inv.openWandAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, 0, true);
                    Storage.getPages().put(p.getUniqueId(), 0);
                }
            } else if (e.getCurrentItem().getType() == Material.STAINED_GLASS_PANE) {
                e.setCancelled(true);
            }
        } else if (title.equalsIgnoreCase(playerAdminPaperSelectTitle)) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null)
                return;
            if (!e.getCurrentItem().hasItemMeta())
                return;
            if (!e.getCurrentItem().getItemMeta().hasDisplayName())
                return;

            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            String levelMsg = inv.getMessage("inventory.playerPaperSelectMenu.level", false);
            String powerMsg = inv.getMessage("inventory.playerPaperSelectMenu.power", false);
            String areaMsg = inv.getMessage("inventory.playerPaperSelectMenu.effectArea", false);

            if (clicked.equalsIgnoreCase(levelMsg)) {
                inv.openPaperAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, "level", 0, true);
                Storage.getCategorySelector().put(p.getUniqueId(), "level");
            } else if (clicked.equalsIgnoreCase(powerMsg)) {
                inv.openPaperAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, "power", 0, true);
                Storage.getCategorySelector().put(p.getUniqueId(), "power");
            } else if (clicked.equalsIgnoreCase(areaMsg)) {
                inv.openPaperAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, "effect area", 0, true);
                Storage.getCategorySelector().put(p.getUniqueId(), "effect area");
            }
        } else if (title.equalsIgnoreCase(playerPaperAdminTitle)) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null)
                return;
            if (!e.getCurrentItem().hasItemMeta())
                return;
            if (!e.getCurrentItem().getItemMeta().hasDisplayName())
                return;

            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            String nextMsg = inv.getMessage("inventory.playerPaperMenu.next", false);
            String prevMsg = inv.getMessage("inventory.playerSpellMenu.previous", false);

            String type = Storage.getCategorySelector().get(p.getUniqueId());
            if (clicked.equalsIgnoreCase(nextMsg)) {
                if (Storage.getPages().containsKey(p.getUniqueId())) {
                    int page = Storage.getPages().get(p.getUniqueId());
                    inv.openPaperAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, type, page, false);
                    Storage.getPages().put(p.getUniqueId(), page+1);
                } else {
                    inv.openPaperAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, type, 1, false);
                    Storage.getPages().put(p.getUniqueId(), 1);
                }
            } else if (clicked.equalsIgnoreCase(prevMsg)) {
                if (Storage.getPages().containsKey(p.getUniqueId())) {
                    int page = Storage.getPages().get(p.getUniqueId());
                    inv.openPaperAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, type, page, false);
                    Storage.getPages().put(p.getUniqueId(), page-1);
                } else {
                    inv.openPaperAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, type, 0, false);
                    Storage.getPages().put(p.getUniqueId(), 0);
                }
            } else if (e.getCurrentItem().getType() == Material.PAPER) {
                p.getInventory().addItem(e.getCurrentItem());
            }
        }
    }
}
