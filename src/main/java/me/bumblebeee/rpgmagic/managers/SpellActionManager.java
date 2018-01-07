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

    public void addRunning(UUID uuid) {
        actionsRunning.add(uuid);
    }

    public void removeRunning(UUID uuid) {
        actionsRunning.remove(uuid);
    }

    public void manageAction(Player p, Location targetLoc, Wand wand, String action) {
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

                    manageAction(p, target.clone(), wand, ac.toString());

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
                    applyPotion(l, wand.getDistance(), args);
            } else if (shape.equalsIgnoreCase("linee")) {
                boolean onGround = false;
                if (wand.getSpell().getName().equalsIgnoreCase("speed"))
                    onGround = true;

                List<Location> locations = shapeManager.getLine(p.getLocation(), wand.getDistance(), onGround);

                for (Location l : locations)
                    applyPotion(l, wand.getDistance(), args);
            } else if (shape.equalsIgnoreCase("coni")) {
                List<Location> locations = shapeManager.getCone(p, wand.getDistance());

                for (Location l : locations)
                    applyPotion(l, wand.getDistance(), args);
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
        } else if (function.equalsIgnoreCase("shootMissileAtTarget")) {
            int range = Integer.parseInt(args[0]);
            Entity target = Utils.getPlayerTarget(p, range);
            ParticleEffect particle = ParticleEffect.valueOf(args[1]);
            if (particle == null) {
                RPGMagic.getInstance().getLogger().severe("FAILED TO FIND PARTICLE NAMED " + args[0]);
                return;
            }

            if (target == null) {
                p.sendMessage("Target not found");
                return;
            }

            new BukkitRunnable() {
                double t = 0;
                Location loc = p.getLocation();
                Location lastBlock = p.getLocation().clone();
                int blocksMoved = 0;
                @Override
                public void run() {
                    if (loc == null)
                        loc = p.getTargetBlock((Set<Material>)null, range).getLocation();

                    if (blocksMoved >= range) {
                        this.cancel();
                        return;
                    }

                    Vector direction = loc.toVector().subtract(target.getLocation().toVector()).multiply(-1).normalize();
                    t = t + 0.02;
                    double x = direction.getX() * t;
                    double y = direction.getY();
                    double z = direction.getZ() * t;
                    loc = loc.add(x,y,z);
                    particle.display(0, 0, 0, 0, 0, loc, (double) 100);


                    if (loc.getBlockX() != lastBlock.getBlockX() ||
                            loc.getBlockY() != lastBlock.getBlockY() || loc.getBlockZ() != lastBlock.getBlockZ()) {
                        blocksMoved++;
                        lastBlock = loc.clone();
                    }

                    Collection<Entity> entities = loc.getWorld().getNearbyEntities(loc, 0.5, 0.5, 0.5);
                    if (entities.size() > 0) {
                        if (!entities.contains(p)) {
                            loc.getWorld().strikeLightning(loc);
                            this.cancel();
                            return;
                        }
                    }

                    if (t > 5) {
                        this.cancel();
                    }
                }
            }.runTaskTimer(RPGMagic.getInstance(), 0, 2);
        } else if (function.equalsIgnoreCase("pullNearbyEntityTowards")) {
            String[] locData = args[0].replace("{", "").replace("}", "").split(",");
            int range = Integer.parseInt(args[1]);
            Location l = getLocationFromData(p.getWorld(), locData).add(0,1,0);

            for (Entity e : p.getWorld().getNearbyEntities(l, range, range, range)) {
                if (!(e instanceof LivingEntity))
                    continue;
                if (e.getUniqueId().equals(p.getUniqueId()))
                    continue;

                e.setVelocity(l.toVector().subtract(e.getLocation().toVector()).multiply(0.1));
            }
        } else if (function.equals("applyPotionAtLocation")) {
            String[] locData = args[0].replace("{", "").replace("}", "").split(",");
            int radius = Integer.parseInt(args[1]);
            Location l = getLocationFromData(p.getWorld(), locData);

            String[] potionData = (args[2] + "," + args[3] + "," + args[4]).split(",");
            applyPotion(l, radius, potionData);
        } else if (function.equalsIgnoreCase("playSoundAtLocation")) {
            String[] locData = args[0].replace("{", "").replace("}", "").split(",");
            int volume = Integer.parseInt(args[2]);
            Sound sound = Sound.valueOf(args[1]);
            Location l = getLocationFromData(p.getWorld(), locData);

            if (sound == null) {
                RPGMagic.getInstance().getLogger().severe("FAILED TO FIND SOUND NAMED " + args[1]);
                return;
            }

            l.getWorld().playSound(l, sound, volume, 1);
        } else if (function.equalsIgnoreCase("damageEntitiesAtLocation")) {
            String[] locData = args[0].replace("{", "").replace("}", "").split(",");
            Location l = getLocationFromData(p.getWorld(), locData);
            int amount = Integer.parseInt(args[1]);

            for (Entity e : l.getWorld().getNearbyEntities(l, 1.5, 1.5, 1.5)) {
                if (!(e instanceof LivingEntity))
                    continue;
                LivingEntity le = (LivingEntity) e;
                le.damage(amount);
            }
        } else if (function.equalsIgnoreCase("teleportEntitiesToWorld")) {
            String[] locData = args[0].replace("{", "").replace("}", "").split(",");
            Location from = getLocationFromData(p.getWorld(), locData);
            World toWorld = Bukkit.getWorld(args[1]);

            if (toWorld == null) {
                RPGMagic.getInstance().getLogger().severe("FAILED TO FIND WORLD CALLED " + args[1]);
                return;
            }

            for (Entity e : from.getWorld().getNearbyEntities(from, 1.5, 1.5, 1.5)) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        e.teleport(toWorld.getSpawnLocation());
                    }
                }.runTaskLater(RPGMagic.getInstance(), 1);
            }
        } else if (function.equalsIgnoreCase("throwPlayerInDirection")) {
            int power = Integer.parseInt(args[0]);
            p.setVelocity(p.getEyeLocation().getDirection().multiply(power));
        } else if (function.equalsIgnoreCase("protectPlayer")) {
            double radius = Double.parseDouble(args[0]);
            double power = Double.parseDouble(args[1]);

            for (Entity e : p.getWorld().getNearbyEntities(p.getLocation(), radius, radius, radius)) {
                if (e.getUniqueId().equals(p.getUniqueId()))
                    continue;

                e.setVelocity(e.getLocation().toVector().subtract(p.getLocation().toVector()).multiply(power));
            }
        } else if (function.equalsIgnoreCase("changeBlocksForPlayerInArea")) {
            String shape = wand.getShape();
            List<Location> locations = new ArrayList<>();
            List<Location> temploc;
            int up = Integer.parseInt(args[1]);
            int down = Integer.parseInt(args[2]);

            if (shape.equalsIgnoreCase("raggio"))
                temploc = shapeManager.getSphere(p.getLocation(), wand.getDistance(), up, down);
            else if (shape.equalsIgnoreCase("linee"))
                temploc = shapeManager.getLine(p.getLocation(), wand.getDistance(), true);
            else
                temploc = shapeManager.getCone(p, wand.getDistance());

            for (int i = 0; i < temploc.size(); i++)
                locations.add(temploc.get(i).subtract(0,1,0));

            Map<Location, Material> changing = new HashMap<>();
            for (Location l : locations) {
                Material m = l.getBlock().getType();
                if (m != Material.STONE)
                    continue;

                changing.put(l, m);
                Utils.disguiseBlock(p, l, Material.matchMaterial(args[0]), (byte) 0);
//                l.getBlock().setType(Material.BARRIER);

                Bukkit.getScheduler().runTaskLater(RPGMagic.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        Utils.disguiseBlock(p, l, m, (byte) 0);
//                        l.getBlock().setType(m);
                    }
                }, 19);
            }

            changedBlocks.put(p.getUniqueId(), changing);
        }
    }

    public Location getLocationFromData(World w, String[] locData) {
        int x = Integer.parseInt(locData[0]);
        int y = Integer.parseInt(locData[1]);
        int z = Integer.parseInt(locData[2]);
        return new Location(w, x, y, z);
    }

    public void applyPotion(Location loc, int radius, String[] args) {
        String potionName = args[0];
        int boost = Integer.parseInt(args[1])-1;
        int duration = Integer.parseInt(args[2])*20;
        PotionEffectType effect = PotionEffectType.getByName(potionName);
        if (effect == null) {
            RPGMagic.getInstance().getLogger().severe(ChatColor.RED + "Invalid potion effect " + potionName);
            return;
        }

        PotionEffect potion = new PotionEffect(effect, duration, boost, false);
        for (Entity e : loc.getWorld().getNearbyEntities(loc, radius, radius, radius)) {
            if (e instanceof LivingEntity)
                ((LivingEntity) e).addPotionEffect(potion);
        }
    }

}
