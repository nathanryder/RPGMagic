package me.bumblebeee.rpgmagic.managers;

import me.bumblebeee.rpgmagic.RPGMagic;
import me.bumblebeee.rpgmagic.utils.Storage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Runnables {

    public static void walk() {
        List<Material> blocked = new ArrayList<>();
        blocked.add(Material.AIR);
        blocked.add(Material.LONG_GRASS);
        blocked.add(Material.DOUBLE_PLANT);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(RPGMagic.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (Player p : Storage.getWaterWalk()) {
                    if (p.getLocation().getBlock().getType() == Material.STATIONARY_WATER) {
                        p.setVelocity(p.getVelocity().add(new Vector(0, 0.3, 0)));
                    }

                    if (!(p.getLocation().add(0, -0.5, 0).getBlock().getType().equals(Material.STATIONARY_WATER)
                            || p.getLocation().add(0, -0.5, 0).getBlock().getType().equals(Material.WATER))
                            && p.isFlying()) {
                        p.setFlying(false);
                        p.setAllowFlight(false);
                        return;
                    }

                    if (p.getLocation().add(0, -1, 0).getBlock().getType().equals(Material.STATIONARY_WATER)
                            || p.getLocation().add(0, -1, 0).getBlock().getType().equals(Material.WATER)) {
                        p.setAllowFlight(true);
                        p.setFlying(true);
                    }
                }

                for (Player p : Storage.getWallWalk()) {
                    Location l = p.getLocation().add(0, 1, 0);

                if (!blocked.contains(l.clone().add(0.5, 0, 0).getBlock().getType())) {
                    p.setVelocity(p.getVelocity().setY(0.4F));
                } else if (!blocked.contains(l.clone().add(0, 0, 0.5).getBlock().getType())) {
                    p.setVelocity(p.getVelocity().setY(0.4F));
                } else if (!blocked.contains(l.clone().add(-0.5, 0, 0).getBlock().getType())) {
                    p.setVelocity(p.getVelocity().setY(0.4F));
                } else if (!blocked.contains(l.clone().add(0, 0, -0.5).getBlock().getType())) {
                    p.setVelocity(p.getVelocity().setY(0.4F));
                } else if (!blocked.contains(l.clone().add(0.5, 0, 0.5).getBlock().getType())) {
                    p.setVelocity(p.getVelocity().setY(0.4F));
                } else if (!blocked.contains(l.clone().add(-0.5, 0, 0.5).getBlock().getType())) {
                    p.setVelocity(p.getVelocity().setY(0.4F));
                } else if (!blocked.contains(l.clone().add(0.5, 0, -0.5).getBlock().getType())) {
                    p.setVelocity(p.getVelocity().setY(0.4F));
                } else if (!blocked.contains(l.clone().add(-0.5, 0, -0.5).getBlock().getType())) {
                    p.setVelocity(p.getVelocity().setY(0.4F));
                }
                }
            }
        }, 3, 3);
    }
}
