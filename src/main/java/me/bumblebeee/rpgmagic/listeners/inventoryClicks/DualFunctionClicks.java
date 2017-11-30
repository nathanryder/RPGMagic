package me.bumblebeee.rpgmagic.listeners.inventoryClicks;

import me.bumblebeee.rpgmagic.managers.InventoryManager;
import me.bumblebeee.rpgmagic.managers.PaperManager;
import me.bumblebeee.rpgmagic.managers.WandManager;
import me.bumblebeee.rpgmagic.utils.Storage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;

public class DualFunctionClicks implements Listener {

    InventoryManager inv = new InventoryManager();
    PaperManager paperManager = new PaperManager();
    WandManager wandManager = new WandManager();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        String playerSpellTitle = inv.getMessage("inventory.playerSpellMenu.title", false);
        String playerPaperTitle = inv.getMessage("inventory.playerPaperMenu.title", false);
        String playerPaperSelectTitle = inv.getMessage("inventory.playerPaperSelectMenu.title", false);
        String playerWandTitle = inv.getMessage("inventory.playerWandMenu.title", false);
        String playerAdminSpellTitle = inv.getMessage("inventory.playerSpellAdminMenu.title", false);
        String playerAdminPaperSelectTitle = inv.getMessage("inventory.playerPaperAdminSelectMenu.title", false);
        String playerPaperAdminTitle = inv.getMessage("inventory.playerPaperAdminMenu.title", false);
        String playerWandAdminTitle = inv.getMessage("inventory.playerWandAdminMenu.title", false);

        String title = e.getInventory().getTitle();

        if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta()
                && e.getCurrentItem().getItemMeta().hasDisplayName()) {
            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            if (clicked.equalsIgnoreCase("close"))
                p.closeInventory();
        }

        if (title.equalsIgnoreCase(playerSpellTitle) || title.equalsIgnoreCase(playerAdminSpellTitle)) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null)
                return;
            if (!e.getCurrentItem().hasItemMeta())
                return;
            if (!e.getCurrentItem().getItemMeta().hasDisplayName())
                return;
            boolean admin = false;
            if (title.equalsIgnoreCase(playerAdminSpellTitle))
                admin = true;

            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            String nextMsg = inv.getMessage("inventory." + title + ".next", false);
            String prevMsg = inv.getMessage("inventory." + title + ".previous", false);

            UUID target = Storage.getAdminMenuHolder().get(p.getUniqueId());
            if (clicked.equalsIgnoreCase(nextMsg)) {
                if (Storage.getPages().containsKey(p.getUniqueId())) {
                    int page = Storage.getPages().get(p.getUniqueId());
                    Storage.getPages().put(p.getUniqueId(), page+1);
                    if (admin)
                        inv.openSpellsAdminMenu(target, p, page, false);
                    else
                        inv.openSpellsMenu(p, page, false);
                } else {
                    Storage.getPages().put(p.getUniqueId(), 1);
                    if (admin)
                        inv.openSpellsAdminMenu(target, p, 1, false);
                    else
                        inv.openSpellsMenu(p, 1, false);
                }
            } else if (clicked.equalsIgnoreCase(prevMsg)) {
                if (Storage.getPages().containsKey(p.getUniqueId())) {
                    int page = Storage.getPages().get(p.getUniqueId());
                    Storage.getPages().put(p.getUniqueId(), page-1);
                    if (admin)
                        inv.openSpellsAdminMenu(target, p, page, false);
                    else
                        inv.openSpellsMenu(p, page, false);
                } else {
                    Storage.getPages().put(p.getUniqueId(), 0);
                    if (admin)
                        inv.openSpellsAdminMenu(target, p, 0, false);
                    else
                        inv.openSpellsMenu(p, 0, false);
                }
            } else if (e.getCurrentItem().getType() == Material.STRUCTURE_VOID) {
                p.getInventory().addItem(e.getCurrentItem());
            }
        } else if (title.equalsIgnoreCase(playerPaperTitle) || title.equalsIgnoreCase(playerPaperAdminTitle)) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null)
                return;
            if (!e.getCurrentItem().hasItemMeta())
                return;
            if (!e.getCurrentItem().getItemMeta().hasDisplayName())
                return;
            boolean admin = false;
            if (title.equalsIgnoreCase(playerPaperAdminTitle))
                admin = true;

            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            String nextMsg = inv.getMessage("inventory.playerPaperMenu.next", false);
            String prevMsg = inv.getMessage("inventory.playerSpellMenu.previous", false);

            String type = Storage.getCategorySelector().get(p.getUniqueId());
            if (clicked.equalsIgnoreCase(nextMsg)) {
                if (Storage.getPages().containsKey(p.getUniqueId())) {
                    int page = Storage.getPages().get(p.getUniqueId());

                    if (admin)
                        inv.openPaperAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, type, page, false);
                    else
                        inv.openPaperMenu(p, type, page, false);
                    Storage.getPages().put(p.getUniqueId(), page+1);
                } else {
                    if (admin)
                        inv.openPaperAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, type, 1, false);
                    else
                        inv.openPaperMenu(p, type, 1, false);
                    Storage.getPages().put(p.getUniqueId(), 1);
                }
            } else if (clicked.equalsIgnoreCase(prevMsg)) {
                if (Storage.getPages().containsKey(p.getUniqueId())) {
                    int page = Storage.getPages().get(p.getUniqueId());

                    if (admin)
                        inv.openPaperAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, type, page, false);
                    else
                        inv.openPaperMenu(p, type, page, false);
                    Storage.getPages().put(p.getUniqueId(), page-1);
                } else {
                    if (admin)
                        inv.openPaperAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, type, 0, false);
                    else
                        inv.openPaperMenu(p, type, 0, true);
                    Storage.getPages().put(p.getUniqueId(), 0);
                }
            } else if (e.getCurrentItem().getType() == Material.PAPER) {
                p.getInventory().addItem(e.getCurrentItem());
            }
        } else if (title.equalsIgnoreCase(playerWandTitle) || title.equalsIgnoreCase(playerWandAdminTitle)) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null)
                return;
            if (!e.getCurrentItem().hasItemMeta())
                return;
            if (!e.getCurrentItem().getItemMeta().hasDisplayName())
                return;
            boolean admin = false;
            if (title.equalsIgnoreCase(playerWandAdminTitle))
                admin = true;

            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            String nextMsg = inv.getMessage("inventory.playerWandTitle.next", false);
            String prevMsg = inv.getMessage("inventory.playerWandTitle.previous", false);

            if (clicked.equalsIgnoreCase(nextMsg)) {
                if (Storage.getPages().containsKey(p.getUniqueId())) {
                    int page = Storage.getPages().get(p.getUniqueId());

                    if (admin)
                        inv.openWandAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, page, false);
                    else
                        inv.openWandMenu(p, page, false);
                    Storage.getPages().put(p.getUniqueId(), page+1);
                } else {
                    if (admin)
                        inv.openWandAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, 1, false);
                    else
                        inv.openWandMenu(p, 1, false);
                    Storage.getPages().put(p.getUniqueId(), 1);
                }
            } else if (clicked.equalsIgnoreCase(prevMsg)) {
                if (Storage.getPages().containsKey(p.getUniqueId())) {
                    int page = Storage.getPages().get(p.getUniqueId());

                    if (admin)
                        inv.openWandAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, page, false);
                    else
                        inv.openWandMenu(p, page, false);
                    Storage.getPages().put(p.getUniqueId(), page-1);
                } else {
                    if (admin)
                        inv.openWandAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, 0, true);
                    else
                        inv.openWandMenu(p, 0, true);
                    Storage.getPages().put(p.getUniqueId(), 0);
                }
            } else if (e.getCurrentItem().getType() != Material.STAINED_GLASS_PANE) {
                p.getInventory().addItem(e.getCurrentItem());
            }
        }
        else if (title.equalsIgnoreCase(playerPaperSelectTitle) || title.equalsIgnoreCase(playerAdminPaperSelectTitle)) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null)
                return;
            if (!e.getCurrentItem().hasItemMeta())
                return;
            if (!e.getCurrentItem().getItemMeta().hasDisplayName())
                return;
            boolean admin = false;
            if (title.equalsIgnoreCase(playerAdminPaperSelectTitle))
                admin = true;

            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            String levelMsg = inv.getMessage("inventory.playerPaperSelectMenu.level", false);
            String powerMsg = inv.getMessage("inventory.playerPaperSelectMenu.power", false);
            String areaMsg = inv.getMessage("inventory.playerPaperSelectMenu.effectArea", false);

            if (clicked.equalsIgnoreCase(levelMsg)) {
                if (admin)
                    inv.openPaperAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, "level", 0, true);
                else
                    inv.openPaperMenu(p, "level", 0, true);
                Storage.getCategorySelector().put(p.getUniqueId(), "level");
            } else if (clicked.equalsIgnoreCase(powerMsg)) {
                if (admin)
                    inv.openPaperAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, "power", 0, true);
                else
                    inv.openPaperMenu(p, "power", 0, true);
                Storage.getCategorySelector().put(p.getUniqueId(), "power");
            } else if (clicked.equalsIgnoreCase(areaMsg)) {
                if (admin)
                    inv.openPaperAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, "effect area", 0, true);
                else
                    inv.openPaperMenu(p, "effect area", 0, true);
                Storage.getCategorySelector().put(p.getUniqueId(), "effect area");
            }
        }
    }
}