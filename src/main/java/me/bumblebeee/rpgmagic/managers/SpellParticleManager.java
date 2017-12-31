package me.bumblebeee.rpgmagic.managers;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.DonutEffect;
import de.slikey.effectlib.effect.HelixEffect;
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

    public void manageParticle(Player p, Location targetLoc, Wand wand, String action) {
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

                    manageParticle(p, targetLoc, wand, ac.toString());

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
            } else if (shape.equalsIgnoreCase("linee")) {
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
            } else if (shape.equalsIgnoreCase("coni")) {
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
        } else if (function.equalsIgnoreCase("outlineAreaTarget")) {
            String shape = wand.getShape();
            if (shape.equalsIgnoreCase("raggio")) {
                List<Location> locations = shapeManager.getCircleBorder(targetLoc, wand.getDistance(), 30);

                for (Location l : locations) {
                    ParticleEffect particle = ParticleEffect.valueOf(args[0]);
                    if (particle == null) {
                        RPGMagic.getInstance().getLogger().severe("FAILED TO FIND PARTICLE NAMED " + args[0]);
                        continue;
                    }
                    particle.display(0, 0, 0, 0, 0, l.add(0,0.1,0), (double) 50);
                }
            } else if (shape.equalsIgnoreCase("linee")) {
                boolean onGround = false;
                if (wand.getSpell().getName().equalsIgnoreCase("speed"))
                    onGround = true;

                List<Location> locations = shapeManager.getLine(targetLoc, wand.getDistance(), onGround);

                for (Location l : locations) {
                    ParticleEffect particle = ParticleEffect.valueOf(args[0]);
                    if (particle == null) {
                        RPGMagic.getInstance().getLogger().severe("FAILED TO FIND PARTICLE NAMED " + args[0]);
                        continue;
                    }
                    particle.display(0, 0, 0, 0, 0, l.add(0,0.1,0), (double) 50);
                }
            } else if (shape.equalsIgnoreCase("coni")) {
                List<Location> locations = shapeManager.getConeBorder(targetLoc, wand.getDistance());
                for (Location l : locations) {
                    ParticleEffect particle = ParticleEffect.valueOf(args[0]);
                    if (particle == null) {
                        RPGMagic.getInstance().getLogger().severe("FAILED TO FIND PARTICLE NAMED " + args[0]);
                        continue;
                    }
                    particle.display(0, 0, 0, 0, 0, l.add(0,0.1,0), (double) 50);
                }
            }
        } else if (function.equalsIgnoreCase("helix")) {
            int duration = Integer.parseInt(args[1]);
            int radius = Integer.parseInt(args[2]);
            de.slikey.effectlib.util.ParticleEffect particle = de.slikey.effectlib.util.ParticleEffect.fromName(args[3]);
            int strands = Integer.parseInt(args[4]);
            int curve = Integer.parseInt(args[5]);
            int period = Integer.parseInt(args[6]);

            EffectManager em = new EffectManager(RPGMagic.getInstance());

            HelixEffect helix = new HelixEffect(em);
            helix.duration = duration;
            helix.radius = radius;
            helix.particle = particle;
            helix.strands = strands;
            helix.curve = curve;
            helix.period = period;

            if (args[0].equalsIgnoreCase("target"))
                helix.setLocation(targetLoc);
            else
                helix.setLocation(p.getLocation());

            helix.start();
        } else if (function.equalsIgnoreCase("createBlackHole")) {
            Location l = SpellCastController.getStoredLocations().get(p.getUniqueId()).add(0,1,0);
            if (l == null) {
                p.sendMessage(ChatColor.RED + "Failed to find stored location!");
                return;
            }

            EffectManager em = new EffectManager(RPGMagic.getInstance());

            DonutEffect helix = new DonutEffect(em);
            helix.setLocation(l.add(0,2,0));
            helix.particle = de.slikey.effectlib.util.ParticleEffect.PORTAL;
            helix.circles = 50;
            helix.particlesCircle = 15;
            helix.radiusDonut = 1.8F;
            helix.radiusTube = 0.1F;
            helix.iterations = 30;
            helix.start();


            //TODO ShieldEffect - protection bubble
        }
    }
}




















