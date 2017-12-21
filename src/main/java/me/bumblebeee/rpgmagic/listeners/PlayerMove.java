package me.bumblebeee.rpgmagic.listeners;

import me.bumblebeee.rpgmagic.managers.SpellCastController;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class PlayerMove implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (e.getFrom().getBlockX() != e.getTo().getBlockX()
                    && e.getFrom().getBlockY() != e.getTo().getBlockY()
                    && e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
            if (SpellCastController.getCasting().contains(p.getUniqueId()))
                e.setCancelled(true);
        }



    }
}
