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
            if (type.equalsIgnoreCase("level")) {
                Storage.getInputingLevel().add(p.getUniqueId());
                p.sendMessage(Messages.INPUT_LEVEL.get());
            } else if (type.equalsIgnoreCase("power")) {
                Storage.getInputingPower().add(p.getUniqueId());
                p.sendMessage(Messages.INPUT_POWER.get());
            } else if (type.equalsIgnoreCase("effect area")) {
                Storage.getInputingEA().add(p.getUniqueId());
                p.sendMessage(Messages.INPUT_AREA_INFO.get());
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
        } else if (Storage.getInputingLevel().contains(p.getUniqueId())) {
            e.setCancelled(true);

            int level;
            try {
                level = Integer.parseInt(ChatColor.stripColor(e.getMessage()));
            } catch (Exception e1) {
                p.sendMessage(Messages.NOT_A_NUMBER.get().replace("%number%", ChatColor.stripColor(e.getMessage())));
                return;
            }
            Storage.getInputingLevel().remove(p.getUniqueId());

            String type = Storage.getTypeHolder().get(p.getUniqueId());
            type = type == null ? "level" : type;
            int npcId = Storage.getOpenNPCs().get(p.getUniqueId()).getId();
            ItemStack item = Storage.getItemHolder().get(p.getUniqueId());
            double price = Storage.getPriceHolder().get(p.getUniqueId());

            String data = type + ":" + level;
            paperManager.addPaper(item, npcId, data, price);
            p.sendMessage(Messages.ADDED_PAPER.get());
        } else if (Storage.getInputingPower().contains(p.getUniqueId())) {
            e.setCancelled(true);

            int power;
            try {
                power = Integer.parseInt(ChatColor.stripColor(e.getMessage()));
            } catch (Exception e1) {
                p.sendMessage(Messages.NOT_A_NUMBER.get().replace("%number%", ChatColor.stripColor(e.getMessage())));
                return;
            }
            Storage.getInputingPower().remove(p.getUniqueId());

            String type = Storage.getTypeHolder().get(p.getUniqueId());
            type = type == null ? "level" : type;
            int npcId = Storage.getOpenNPCs().get(p.getUniqueId()).getId();
            ItemStack item = Storage.getItemHolder().get(p.getUniqueId());
            double price = Storage.getPriceHolder().get(p.getUniqueId());

            String data = type + ":" + power;
            paperManager.addPaper(item, npcId, data, price);
            p.sendMessage(Messages.ADDED_PAPER.get());
        } else if (Storage.getInputingEA().contains(p.getUniqueId())) {
            e.setCancelled(true);

            String msg = ChatColor.stripColor(e.getMessage());
            if (msg.split(" ").length < 2) {
                p.sendMessage(Messages.INVALID_FORMATTING.get());
                return;
            }

            String shape = msg.split(" ")[0];
            int addDistance;
            try {
                addDistance = Integer.parseInt(msg.split(" ")[1]);
            } catch (Exception e1) {
                p.sendMessage(Messages.NOT_A_NUMBER.get().replace("%number%", msg.split(" ")[1]));
                return;
            }

            if (!shapeExists(shape)) {
                p.sendMessage(Messages.SHAPE_DOES_NOT_EXIST.get().replace("%shape%", shape));
                return;
            }

            String type = Storage.getTypeHolder().get(p.getUniqueId());
            type = type == null ? "level" : type;
            int npcId = Storage.getOpenNPCs().get(p.getUniqueId()).getId();
            ItemStack item = Storage.getItemHolder().get(p.getUniqueId());
            double price = Storage.getPriceHolder().get(p.getUniqueId());

            int distance = addDistance;
            String data = type + ":" + shape + ":" + distance;
            paperManager.addPaper(item, npcId, data, price);
            p.sendMessage(Messages.ADDED_PAPER.get());
        } else if (Storage.getInputingSearch().contains(p.getUniqueId())) {
            e.setCancelled(true);

            String playerName = ChatColor.stripColor(e.getMessage());
            inventoryManager.openAdminPlayers(p, playerName, 0);
            Storage.getInputingSearch().remove(p.getUniqueId());
        }
    }

    public boolean shapeExists(String shape) {
        if (shape.equalsIgnoreCase("coni"))
            return true;
        else if (shape.equalsIgnoreCase("raggio"))
            return true;
        else if (shape.equalsIgnoreCase("linee"))
            return true;
        else
            return false;
    }

}