package me.bumblebeee.rpgmagic.listeners;

import me.bumblebeee.rpgmagic.managers.SpellActionManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamage implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.LIGHTNING) {
            if (SpellActionManager.getProtectFromLightning().contains(e.getEntity().getUniqueId()))
                e.setCancelled(true);
        }
    }
}
