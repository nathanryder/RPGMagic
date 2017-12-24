package me.bumblebeee.rpgmagic.managers;

import me.bumblebeee.rpgmagic.utils.ParticleEffect;
import me.bumblebeee.rpgmagic.utils.RespectiveLocation;
import me.bumblebeee.rpgmagic.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

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

    public List<Location> getCircleBorder(Location center, int radius, int amount) {
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

    public List<Location> getLine(Location location, int size, boolean onGround) {
        List<Location> locations = new ArrayList<>();
        location.setPitch(0);

        double x = 0;
        double z = 0;
        switch (Utils.getCardinalDirection(location)) {
            case "North":
                z = 0.5;
                break;
            case "South":
                z = 0.5;
                break;
            case "East":
                x = 0.5;
                break;
            case "West":
                x = 0.5;
                break;
        }

        BlockIterator blocksToAdd = new BlockIterator(location, 0, size);
        Location blockToAdd;
        while(blocksToAdd.hasNext()) {
            blockToAdd = blocksToAdd.next().getLocation();
            locations.add(blockToAdd.add(x,0,z));
        }

        return locations;
    }

    public List<Location> getCone(Player p, int size) {
        List<Location> locations = new ArrayList<>();

        for (int i = size; i > 1; i--) {
            locations.addAll(getConeLine(size, i, p));
        }

        return locations;
    }

    public List<Location> getConeBorder(Location loc, int size) {
        List<Location> locations = new ArrayList<>();

        locations.addAll(getConeLineEdges(size, size, loc, true));

        for (int i = size-1; i > 1; i--) {
            locations.addAll(getConeLineEdges(size, i, loc, false));
        }

        return locations;
    }


    public List<Location> getConeLineEdges(int startSize, int size, Location loc, boolean first) {
        RespectiveLocation rl = new RespectiveLocation(loc);
        List<Location> locations = new ArrayList<>();
        double width = (size-1)/2;
        rl.forward(Math.floor(size/2));

        double left = width;
        double right = width;
        if (width % 1 != 0) {
            left = Math.floor(width);
            right = Math.ceil(width);
        }

        if (startSize % 2 == 0)
            right = (right+left)-1;
        else
            right = (right+left);

        for (int i = 0; i < left; i++)
            rl.left();
        locations.add(rl.getLocation().clone());
        for (int i = 0; i < right; i++) {
            rl.right();
            if (first)
                locations.add(rl.getLocation().clone());
        }

        if (!first)
            locations.add(rl.getLocation().clone());

        return locations;
    }

    public List<Location> getConeLine(int startSize, int size, Player p) {
        RespectiveLocation rl = new RespectiveLocation(p.getLocation());
        List<Location> locations = new ArrayList<>();
        double width = (size-1)/2;
        Location start = rl.forward(Math.floor(size/2));
        locations.add(start);

        double left = width;
        double right = width;
        if (width % 1 != 0) {
            left = Math.floor(width);
            right = Math.ceil(width);
        }

        if (startSize % 2 == 0)
            right = (right+left)-1;
        else
            right = (right+left);

        for (int i = 0; i < left; i++)
            rl.left();
        for (int i = 0; i < right; i++)
            locations.add(rl.right().clone());
        return locations;
    }

}
