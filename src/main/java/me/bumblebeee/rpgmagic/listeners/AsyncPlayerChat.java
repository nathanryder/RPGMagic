package me.bumblebeee.rpgmagic.listeners;

import me.bumblebeee.rpgmagic.managers.InventoryManager;
import me.bumblebeee.rpgmagic.managers.Messages;
import me.bumblebeee.rpgmagic.managers.PaperManager;
import me.bumblebeee.rpgmagic.managers.WandManager;
import me.bumblebeee.rpgmagic.utils.Storage;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

public class AsyncPlayerChat implements Listener {

    PaperManager paperManager = new PaperManager();
    WandManager wandManager = new WandManager();
    InventoryManager inventoryManager = new InventoryManager();

    @EventHandler (priority = EventPriority.LOWEST)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (Storage.getInputingPrice().contains(p.getUniqueId())) {
            e.setCancelled(true);

            double price;
            try {
                price = Double.parseDouble(ChatColor.stripColor(e.getMessage()));
            } catch (Exception e1) {
                p.sendMessage(Messages.NOT_A_NUMBER.get().replace("%number%", ChatColor.stripColor(e.getMessage())));
                return;
            }

            Storage.getPriceHolder().put(p.getUniqueId(), price);
            Storage.getInputingPrice().remove(p.getUniqueId());

            String type = Storage.getTypeHolder().get(p.getUniqueId());
            type = type == null ? "level" : Storage.getTypeHolder().get(p.getUniqueId());
            if (type.equalsIgnoreCase("level") || type.equalsIgnoreCase("power")) {
                int npcId = Storage.getOpenNPCs().get(p.getUniqueId()).getId();
                ItemStack item = Storage.getItemHolder().get(p.getUniqueId());

                String data = type + ":" + Storage.getLvlPwrDistHolder().get(p.getUniqueId());
                paperManager.addPaper(item, npcId, data, price);
                p.sendMessage(Messages.ADDED_PAPER.get());
            } else if (type.equalsIgnoreCase("effect area")) {
                int npcId = Storage.getOpenNPCs().get(p.getUniqueId()).getId();
                ItemStack item = Storage.getItemHolder().get(p.getUniqueId());

                double distance = Storage.getLvlPwrDistHolder().get(p.getUniqueId());
                String shape = Storage.getEAHolder().get(p.getUniqueId());
                String data = type + ":" + shape + ":" + distance;
                paperManager.addPaper(item, npcId, data, price);
                p.sendMessage(Messages.ADDED_PAPER.get());
            }
        } else if (Storage.getInputingWandPrice().contains(p.getUniqueId())) {
            e.setCancelled(true);

            double price;
            try {
                price = Double.parseDouble(ChatColor.stripColor(e.getMessage()));
            } catch (Exception e1) {
                p.sendMessage(Messages.NOT_A_NUMBER.get().replace("%number%", ChatColor.stripColor(e.getMessage())));
                return;
            }
            Storage.getInputingWandPrice().remove(p.getUniqueId());

            ItemStack i = Storage.getItemHolder().get(p.getUniqueId());
            NPC npc = Storage.getOpenNPCs().get(p.getUniqueId());
            wandManager.addWandToNpc(i, npc.getId(), price);
        } else if (Storage.getInputingSearch().contains(p.getUniqueId())) {
            e.setCancelled(true);

            String playerName = ChatColor.stripColor(e.getMessage());
            inventoryManager.openAdminPlayers(p, playerName, 0);
            Storage.getInputingSearch().remove(p.getUniqueId());
        }
    }

    public boolean shapeExists(String shape) {
        if (shape.equalsIgnoreCase("cono"))
            return true;
        else if (shape.equalsIgnoreCase("raggio"))
            return true;
        else if (shape.equalsIgnoreCase("linea"))
            return true;
        else
            return false;
    }

}