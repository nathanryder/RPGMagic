package me.bumblebeee.rpgmagic.spells;

import me.bumblebeee.rpgmagic.RPGMagic;
import me.bumblebeee.rpgmagic.Wand;
import me.bumblebeee.rpgmagic.events.SpellCastEvent;
import me.bumblebeee.rpgmagic.managers.Cooldown;
import me.bumblebeee.rpgmagic.managers.Messages;
import me.bumblebeee.rpgmagic.managers.RPLManaManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

public class Lightning implements Listener {

    RPLManaManager mana = new RPLManaManager();
    Cooldown cooldowns = new Cooldown();

    @EventHandler
    public void onLightning(SpellCastEvent e) {
        if (!e.getSpell().equalsIgnoreCase("lightning"))
            return;

        Player p = e.getPlayer();
        Wand wand = e.getWand();
        String spell = wand.getSpell();
        int manaRequired = wand.getManaRequired();

        int cd = cooldowns.getCooldown(p.getUniqueId(), spell);
        if (cd > 0) {
            p.sendMessage(Messages.SPELL_ON_COOLDOWN.get().replace("%time%", String.valueOf(cd)));
            return;
        }
        if (mana.getPlayerMana(p) < manaRequired) {
            p.sendMessage(Messages.NOT_ENOUGH_MANA.get());
            return;
        }

        p.getWorld().strikeLightning(p.getTargetBlock((Set<Material>)null, 50).getLocation());
        mana.takeMana(p, manaRequired);

        cooldowns.giveCooldown(p.getUniqueId(), spell);
        new BukkitRunnable() {
            @Override
            public void run() {
                cooldowns.reduceCooldown(p.getUniqueId(), spell, 1);
                if (cooldowns.getCooldown(p.getUniqueId(), spell) == 0)
                    this.cancel();
            }
        }.runTaskTimer(RPGMagic.getInstance(), 20, 20);
    }

}