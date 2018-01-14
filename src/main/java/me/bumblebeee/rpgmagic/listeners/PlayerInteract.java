package me.bumblebeee.rpgmagic.listeners;

import me.bumblebeee.rpgmagic.RPGMagic;
import me.bumblebeee.rpgmagic.Wand;
import me.bumblebeee.rpgmagic.events.SpellCastEvent;
import me.bumblebeee.rpgmagic.managers.Messages;
import me.bumblebeee.rpgmagic.utils.Storage;
import me.bumblebeee.rpgmagic.utils.TableUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import java.util.ArrayList;
import java.util.List;

public class PlayerInteract implements Listener {

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock() != null) {
                if (e.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE) {
                    boolean check = TableUtils.checkShape(e.getClickedBlock().getLocation().subtract(0, 1, 0));
                    if (!check)
                        return;

                    e.setCancelled(true);
                    Location l = e.getClickedBlock().getLocation();
                    if (Storage.getOpenInventories().containsValue(l)) {
                        p.sendMessage(Messages.ALTAR_IN_USE.get());
                        return;
                    }

                        String name = RPGMagic.getInstance().getConfig().getString("alterName");
                    Inventory inv = Bukkit.getServer().createInventory(null, InventoryType.DISPENSER, name);

                    if (Storage.getCrafts().get(l) != null)
                        inv.setContents(Storage.getCrafts().get(l));

                    p.openInventory(inv);
                    Storage.getOpenInventories().put(e.getPlayer().getUniqueId(), l);
                }
            }
        }

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
            if (e.getItem() == null)
                return;
            ItemStack i = e.getItem();
            if (i.getType() != Material.DIAMOND_SWORD)
                return;

            if (!i.hasItemMeta())
                return;
            if (!i.getItemMeta().hasDisplayName())
                return;
            if (i.getDurability() != 9)
                return;
            if (!i.getItemMeta().isUnbreakable())
                return;

            String name = ChatColor.translateAlternateColorCodes('&', RPGMagic.getInstance().getConfig().getString("wandItem.display"));
            if (i.getItemMeta().getDisplayName().equals(name)) {
                e.setCancelled(true);

                Wand wand = new Wand(i);
                SpellCastEvent event = new SpellCastEvent(p, wand);
                Bukkit.getServer().getPluginManager().callEvent(event);
            }
        }
    }

}