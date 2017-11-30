package me.bumblebeee.rpgmagic.listeners.inventoryClicks;

import me.bumblebeee.rpgmagic.managers.InventoryManager;
import me.bumblebeee.rpgmagic.managers.Messages;
import me.bumblebeee.rpgmagic.managers.WandManager;
import me.bumblebeee.rpgmagic.utils.HiddenStringUtils;
import me.bumblebeee.rpgmagic.utils.Storage;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Wands implements Listener {

    InventoryManager inv = new InventoryManager();
    WandManager wandManager = new WandManager();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        String removeWandTitle = inv.getMessage("inventory.removeWand.title", false);
        String addWandTitle = inv.getMessage("inventory.addWand.title", false);

        String title = e.getInventory().getTitle();

        if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta()
                && e.getCurrentItem().getItemMeta().hasDisplayName()) {
            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            if (clicked.equalsIgnoreCase("close"))
                p.closeInventory();
        }

        if (title.equalsIgnoreCase(addWandTitle)) {
            if (e.getCurrentItem() == null)
                return;
            if (!e.getCurrentItem().hasItemMeta())
                return;
            if (!e.getCurrentItem().getItemMeta().hasDisplayName())
                return;

            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            String saveMsg = inv.getMessage("inventory.addWand.save", false);

            if (clicked.equalsIgnoreCase(saveMsg)) {
                e.setCancelled(true);

                ItemStack i = e.getInventory().getItem(13);
                if (i == null)
                    return;
                if (!i.hasItemMeta())
                    return;
                if (!i.getItemMeta().hasDisplayName())
                    return;
                i.setAmount(1);
                String clickedItem = ChatColor.stripColor(i.getItemMeta().getDisplayName());
                if (clickedItem.equalsIgnoreCase(" "))
                    return;

                //input price
                Storage.getItemHolder().put(p.getUniqueId(), i);
                Storage.getInputingWandPrice().add(p.getUniqueId());
                p.sendMessage(Messages.ENTER_A_NUMBER.get());
                p.closeInventory();
            } else if (clicked.equalsIgnoreCase(" ")) {
                e.setCancelled(true);
            }
        } else if (title.equalsIgnoreCase(removeWandTitle)) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null)
                return;
            if (!e.getCurrentItem().hasItemMeta())
                return;
            if (!Storage.getOpenNPCs().containsKey(p.getUniqueId()))
                return;
            NPC npc = Storage.getOpenNPCs().get(p.getUniqueId());

            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());

            if (clicked == null)
                return;
            ItemMeta im = e.getCurrentItem().getItemMeta();

            if (clicked.equalsIgnoreCase("close")) {
                p.closeInventory();
                return;
            }

            if (!HiddenStringUtils.hasHiddenString(im.getLore().get(im.getLore().size()-1)))
                return;

            String id = HiddenStringUtils.extractHiddenString(im.getLore().get(im.getLore().size()-1)).split(":")[1];
            wandManager.deleteWand(Integer.parseInt(id), npc.getId());
            e.getInventory().setItem(e.getRawSlot(), new ItemStack(Material.AIR));
        }
    }
}
