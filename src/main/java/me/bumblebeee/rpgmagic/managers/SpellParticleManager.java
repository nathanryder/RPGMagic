package me.bumblebeee.rpgmagic.managers;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.DonutEffect;
import de.slikey.effectlib.effect.HelixEffect;
import de.slikey.effectlib.effect.ShieldEffect;
import de.slikey.effectlib.effect.TraceEffect;
import de.slikey.effectlib.util.DynamicLocation;
import me.bumblebeee.rpgmagic.RPGMagic;
import me.bumblebeee.rpgmagic.Wand;
import me.bumblebeee.rpgmagic.utils.ParticleEffect;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class SpellParticleManager {

    ShapeManager shapeManager = new ShapeManager();

    public void manageParticle(Player p, Location castLocation, Location targetLoc, Wand wand, String action) {
        String[] data = action.split(":");
        String function = data[0];
        String[] args = new String[10];

        for (int i = 1; i < data.length; i++)
            args[i - 1] = data[i];


        if (function.equalsIgnoreCase("loop")) {
            int time = Integer.parseInt(data[data.length-2]);
            int iterations = Integer.parseInt(data[data.length-1]);

            new BukkitRunnable() {
                int count = 0;
                @Override
                public void run() {
                    StringBuilder ac = new StringBuilder();
                    for (String s : args)
                        ac.append(s).append(":");

                    manageParticle(p, castLocation, targetLoc, wand, ac.toString());

                    count++;
                    if (count >= iterations)
                        this.cancel();
                }
            }.runTaskTimer(RPGMagic.getInstance(), 0, time);
        } else if (function.equalsIgnoreCase("outlineArea")) {
            String shape = wand.getShape();
            if (shape.equalsIgnoreCase("raggio")) {
                List<Location> locations = shapeManager.getCircleBorder(p.getLocation(), wand.getDistance(), 30);

                for (Location l : locations) {
                    ParticleEffect particle = ParticleEffect.valueOf(args[0]);
                    if (particle == null) {
                        RPGMagic.getInstance().getLogger().severe("FAILED TO FIND PARTICLE NAMED " + args[0]);
                        continue;
                    }
                    particle.display(0, 0, 0, 0, 0, l.add(0,0.1,0), (double) 50);
                }
            } else if (shape.equalsIgnoreCase("linea")) {
                boolean onGround = false;
                if (wand.getSpell().getName().equalsIgnoreCase("speed"))
                    onGround = true;

                List<Location> locations = shapeManager.getLine(p.getLocation(), wand.getDistance(), onGround);

                for (Location l : locations) {
                    ParticleEffect particle = ParticleEffect.valueOf(args[0]);
                    if (particle == null) {
                        RPGMagic.getInstance().getLogger().severe("FAILED TO FIND PARTICLE NAMED " + args[0]);
                        continue;
                    }
                    particle.display(0, 0, 0, 0, 0, l.add(0,0.1,0), (double) 50);
                }
            } else if (shape.equalsIgnoreCase("cono")) {
                List<Location> locations = shapeManager.getConeBorder(p.getLocation(), wand.getDistance());
                for (Location l : locations) {
                    ParticleEffect particle = ParticleEffect.valueOf(args[0]);
                    if (particle == null) {
                        RPGMagic.getInstance().getLogger().severe("FAILED TO FIND PARTICLE NAMED " + args[0]);
                        continue;
                    }
                    particle.display(0, 0, 0, 0, 0, l.add(0,0.1,0), (double) 50);
                }
            }
        }
    }
}




















