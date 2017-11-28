package me.bumblebeee.rpgmagic.managers;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class ShapeManager {

    public List<Location> getCircle(Location l, float r) {
        int cx = l.getBlockX();
        int cy = l.getBlockY();
        int cz = l.getBlockZ();
        World w = l.getWorld();
        float rSquared =  r * r;

        List<Location> locs = new ArrayList<>();
        for (float x = cx-r; x <= cx+r; x++) {
            for (float z = cz-r; z <= cz+r; z++) {
                if ((cx-x) * (cx-x) + (cz-z) * (cz-z) <= rSquared)
                    locs.add(new Location(w, x, cy, z));
            }
        }

        return locs;
    }

    public ArrayList<Location> getCircleBorder(Location center, int radius, int amount) {
        ArrayList<Location> locations = new ArrayList<>();
        World world = center.getWorld();
        double increment = (2 * Math.PI) / amount;

        for(int i = 0; i < amount; i++) {
            double angle = i * increment;
            double x = center.getX() + (radius * Math.cos(angle));
            double z = center.getZ() + (radius * Math.sin(angle));
            locations.add(new Location(world, x, center.getY(), z));
        }
        return locations;
    }

}
