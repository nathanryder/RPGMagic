package me.bumblebeee.rpgmagic.listeners;

import me.bumblebeee.rpgmagic.RPGMagic;
import me.bumblebeee.rpgmagic.Spell;
import me.bumblebeee.rpgmagic.events.SpellCastEvent;
import me.bumblebeee.rpgmagic.managers.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class SpellCast implements Listener {

    SpellManager spellManager = new SpellManager();
    RPLManaManager mana = new RPLManaManager();
    Cooldown cooldowns = new Cooldown();

    @EventHandler
    public void onSpellCast(SpellCastEvent e) {
        if (!spellManager.spellExists(e.getSpell().getName()))
            return;

        Spell spell = e.getSpell();
        Player p = e.getPlayer();

        int cd = cooldowns.getCooldown(p.getUniqueId(), spell.getName());
        if (cd > 0) {
            p.sendMessage(Messages.SPELL_ON_COOLDOWN.get().replace("%time%", String.valueOf(cd)));
            return;
        }
        if (mana.getPlayerMana(p) < spell.getMana()) {
            p.sendMessage(Messages.NOT_ENOUGH_MANA.get());
            return;
        }

        SpellCastController controller = new SpellCastController(p, e.getWand());

        controller.runParticles("START");
        controller.runCommands();
        controller.runParticles("NORMAL");
        controller.runActions();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (controller.getActionManager().getActionsRunning().size() == 0) {
                    controller.runParticles("END");
                    this.cancel();
                }
            }
        }.runTaskTimer(RPGMagic.getInstance(), 5, 5);

        mana.takeMana(p, spell.getMana());
        cooldowns.giveCooldown(p.getUniqueId(), spell.getName());
        new BukkitRunnable() {
            @Override
            public void run() {
                cooldowns.reduceCooldown(p.getUniqueId(), spell.getName(), 1);
                if (cooldowns.getCooldown(p.getUniqueId(), spell.getName()) == 0)
                    this.cancel();
            }
        }.runTaskTimer(RPGMagic.getInstance(), 20, 20);
    }


}
