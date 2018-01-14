package me.bumblebeee.rpgmagic.listeners;

import me.bumblebeee.rpgmagic.events.ItemBuyEvent;
import me.bumblebeee.rpgmagic.utils.Storage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.UUID;

public class ItemBuy implements Listener {

    Storage storage = new Storage();

    @EventHandler
    public void onItemBuy(ItemBuyEvent e) {
        Player p = e.getPlayer();
        if (e.getType().equalsIgnoreCase("spell")) {
            storage.addSpellPlayer(p.getUniqueId(), e.getArgs());
        } else if (e.getType().equalsIgnoreCase("paper")) {
            String[] data = e.getArgs().split(":");
            UUID uuid = p.getUniqueId();
            String type = data[1];
            double lvlPwrDist = Double.parseDouble(data[2]);

            if (type.equalsIgnoreCase("level") || type.equalsIgnoreCase("power"))
                storage.addPaperPlayer(uuid, type, lvlPwrDist, null);
            else if (type.equalsIgnoreCase("effect area")) {
                storage.addPaperPlayer(uuid, type, lvlPwrDist, data[3]);
            }
        } else if (e.getType().equalsIgnoreCase("wand")) {
            storage.addWandPlayer(p.getUniqueId(), e.getArgs());
        }
    }
}