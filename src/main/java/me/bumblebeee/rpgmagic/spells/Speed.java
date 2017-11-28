package me.bumblebeee.rpgmagic.spells;

import me.bumblebeee.rpgmagic.ParticleEffect;
import me.bumblebeee.rpgmagic.RPGMagic;
import me.bumblebeee.rpgmagic.Wand;
import me.bumblebeee.rpgmagic.events.SpellCastEvent;
import me.bumblebeee.rpgmagic.managers.Cooldown;
import me.bumblebeee.rpgmagic.managers.Messages;
import me.bumblebeee.rpgmagic.managers.RPLManaManager;
import me.bumblebeee.rpgmagic.managers.ShapeManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Speed implements Listener {

    RPLManaManager mana = new RPLManaManager();
    Cooldown cooldowns = new Cooldown();
    ShapeManager shapes = new ShapeManager();

    @EventHandler
    public void onSpeed(SpellCastEvent e) {
        if (!e.getSpell().equalsIgnoreCase("Speed"))
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

        new BukkitRunnable() {
            double t = 0;
            Location loc = p.getLocation().add(new Vector(0.5, 0, 0.5));

            @Override
            public void run() {
                t = t + 0.1 * Math.PI;
                for (double theta = 0; theta <= 2 * Math.PI; theta = theta + Math.PI / 32) {
                    double x = t * Math.cos(theta);
                    double z = t * Math.sin(theta);

                    loc.add(x, 0, z);
                    ParticleEffect.VILLAGER_HAPPY.display(0, 0, 0, 0, 0, loc, (double) 50);
                    loc.subtract(x, 0, z);
                }

                if (t > 8)
                    this.cancel();
            }
        }.runTaskTimer(RPGMagic.getInstance(), 0, 1);

        new BukkitRunnable() {
            int duration = (10*2)-1;
            @Override
            public void run() {
                for (Location l : shapes.getCircleBorder(p.getLocation(), 9, 60)) {
                    ParticleEffect.VILLAGER_HAPPY.display(0, 0, 0, 0, 0, l.add(0,0.1,0), (double) 50);
                }

                duration--;
                if (duration <= 0)
                    this.cancel();
            }
        }.runTaskTimer(RPGMagic.getInstance(), 0, 10);


        PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 40, 1);
        new BukkitRunnable() {
            int duration = 10;
            @Override
            public void run() {
                for (Entity e : p.getNearbyEntities(9, 9, 9)) {
                    if (!(e instanceof Player))
                        continue;
                    Player t = (Player) e;

                    t.addPotionEffect(speed);
                }
                p.addPotionEffect(speed);

                duration--;
                if (duration <= 0) {
                    for (Location l : shapes.getCircleBorder(p.getLocation(), 9, 60)) {
                        ParticleEffect.REDSTONE.display(0, 0, 0, 0, 0, l.add(0,0.1,0), (double) 50);
                        ParticleEffect.REDSTONE.display(0, 0, 0, 0, 0, l.add(0,0.3,0), (double) 50);
                    }
                    this.cancel();
                }
            }
        }.runTaskTimer(RPGMagic.getInstance(), 0, 20);



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
