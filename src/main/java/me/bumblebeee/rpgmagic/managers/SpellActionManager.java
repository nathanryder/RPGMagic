package me.bumblebeee.rpgmagic.managers;

import lombok.Getter;
import me.bumblebeee.rpgmagic.RPGMagic;
import me.bumblebeee.rpgmagic.Wand;
import me.bumblebeee.rpgmagic.utils.ParticleEffect;
import me.bumblebeee.rpgmagic.utils.Storage;
import me.bumblebeee.rpgmagic.utils.Utils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class SpellActionManager {

    ShapeManager shapeManager = new ShapeManager();

    private @Getter List<UUID> actionsRunning = new ArrayList<>();
    private static @Getter Map<UUID, Map<Location, Material>> changedBlocks = new HashMap<>();
    private static @Getter List<UUID> protectFromLightning = new ArrayList<>();
    private static @Getter List<UUID> frozen = new ArrayList<>();

    public void addRunning(UUID uuid) {
        actionsRunning.add(uuid);
    }

    public void removeRunning(UUID uuid) {
        actionsRunning.remove(uuid);
    }

    public void manageAction(Player p, Location castLocation, Location targetLoc, Wand wand, String action) {
        String[] data = action.split(":");
        String function = data[0];
        String[] args = new String[100];

        for (int i = 1; i < data.length; i++)
            args[i - 1] = data[i];

        UUID uuid = UUID.randomUUID();
        addRunning(uuid);

        if (function.equalsIgnoreCase("loop")) {
            int time = Integer.parseInt(data[data.length-2]);
            int iterations = Integer.parseInt(data[data.length-1]);

            final Location target = targetLoc;
            new BukkitRunnable() {
                int count = 0;
                @Override
                public void run() {
                    StringBuilder ac = new StringBuilder();
                    for (String s : args)
                        ac.append(s).append(":");

                    manageAction(p, castLocation.clone(), target.clone(), wand, ac.toString());

                    count++;
                    if (count >= iterations) {
                        removeRunning(uuid);
                        this.cancel();
                    }
                }
            }.runTaskTimer(RPGMagic.getInstance(), 0, time);
        } else if (function.equalsIgnoreCase("applyPotionShape")) {
            String shape = wand.getShape();
            if (shape.equalsIgnoreCase("raggio")) {
                List<Location> locations = shapeManager.getCircle(p.getLocation(), wand.getDistance());

                for (Location l : locations)
                    applyPotion(p, l, wand.getDistance(), args);
            } else if (shape.equalsIgnoreCase("linea")) {
                boolean onGround = false;
                if (wand.getSpell().getName().equalsIgnoreCase("speed"))
                    onGround = true;

                List<Location> locations = shapeManager.getLine(p.getLocation(), wand.getDistance(), onGround);

                for (Location l : locations)
                    applyPotion(p, l, wand.getDistance(), args);
            } else if (shape.equalsIgnoreCase("cono")) {
                List<Location> locations = shapeManager.getCone(p.getLocation(), wand.getDistance());

                for (Location l : locations)
                    applyPotion(p, l, wand.getDistance(), args);
            }

            removeRunning(uuid);
        } else if (function.equalsIgnoreCase("waterWalk")) {
            if (args[0].equalsIgnoreCase("enable"))
                Storage.getWaterWalk().add(p);
            else if (args[0].equalsIgnoreCase("disable"))
                Storage.getWaterWalk().remove(p);
        } else if (function.equalsIgnoreCase("wallWalk")) {
            if (args[0].equalsIgnoreCase("enable"))
                Storage.getWallWalk().add(p);
            else if (args[0].equalsIgnoreCase("disable"))
                Storage.getWallWalk().remove(p);
        } else if (function.equalsIgnoreCase("freezePlayer")) {
            SpellCastController.getFrozen().add(p.getUniqueId());
        } else if (function.equalsIgnoreCase("unfreezePlayer")) {
            SpellCastController.getFrozen().remove(p.getUniqueId());
        } else if (function.equalsIgnoreCase("teleportToStoredLocation")) {
            Location l = SpellCastController.getStoredLocations().get(p.getUniqueId()).add(0,1,0);
            if (l == null)
                p.sendMessage(ChatColor.RED + "Failed to find stored location!");
            else
                p.teleport(l);
        }
    }

    public Location getLocationFromData(World w, String[] locData) {
        int x = Integer.parseInt(locData[0]);
        int y = Integer.parseInt(locData[1]);
        int z = Integer.parseInt(locData[2]);
        return new Location(w, x, y, z);
    }

    public void applyPotion(Player p, Location loc, int radius, String[] args) {
        Bukkit.getScheduler().runTaskLater(RPGMagic.getInstance(), new Runnable() {
            @Override
            public void run() {
                String potionName = args[0];
                int boost = Integer.parseInt(args[1])-1;
                int duration = Integer.parseInt(args[2])*20;
                PotionEffectType effect = PotionEffectType.getByName(potionName);
                if (effect == null) {
                    RPGMagic.getInstance().getLogger().severe(ChatColor.RED + "Invalid potion effect " + potionName);
                    return;
                }

                PotionEffect potion = new PotionEffect(effect, duration, boost, false);
                if (radius <= 1) {
                    p.addPotionEffect(potion);
                } else {
                    for (Entity e : loc.getWorld().getNearbyEntities(loc, radius, radius, radius)) {
                        if (e instanceof LivingEntity)
                            ((LivingEntity) e).addPotionEffect(potion);
                    }
                }
            }
        }, 1);
    }

}
