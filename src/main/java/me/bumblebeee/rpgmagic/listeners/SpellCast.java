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
        if (e.getSpell() == null)
            return;
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

        int castTime = 3;
        SpellCastController controller = new SpellCastController(p, e.getWand());
//        controller.startCasting();
//
//        EffectManager em = new EffectManager(RPGMagic.getInstance());
//
//        AtomEffect effect = new AtomEffect(em);
//        effect.setLocation(p.getLocation().add(0,2,0));
//        effect.particleNucleus = de.slikey.effectlib.util.ParticleEffect.PORTAL;
//        effect.particleOrbital = de.slikey.effectlib.util.ParticleEffect.REDSTONE;
//        effect.duration = 2800;
//        effect.start();
//
//        Bukkit.getScheduler().runTaskLater(RPGMagic.getInstance(), new Runnable() {
//            @Override
//            public void run() {
//                SpellCastController.getCasting().remove(p.getUniqueId());
//                p.setFlying(false);
//            }
//        }, 20*castTime);
//
//        Bukkit.getScheduler().runTaskLater(RPGMagic.getInstance(), new Runnable() {
//            @Override
//            public void run() {
                controller.process();
//            }
//        }, (20*castTime)+7);

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
