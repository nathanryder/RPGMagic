package me.bumblebeee.rpgmagic.utils;

import org.bukkit.Location;
import org.bukkit.Material;

public class TableUtils {

    //Center is the beacon
    public static boolean checkShape(Location center) {
        if (center.getBlock().getType() != Material.BEACON)
            return false;
        else if (center.getBlock().getRelative(1,0,0).getType() != Material.LAPIS_BLOCK)
            return false;
        else if (center.getBlock().getRelative(1,0,1).getType() != Material.LAPIS_BLOCK)
            return false;
        else if (center.getBlock().getRelative(1,0,-1).getType() != Material.LAPIS_BLOCK)
            return false;
        else if (center.getBlock().getRelative(-1,0,0).getType() != Material.LAPIS_BLOCK)
            return false;
        else if (center.getBlock().getRelative(-1,0,-1).getType() != Material.LAPIS_BLOCK)
            return false;
        else if (center.getBlock().getRelative(-1,0,1).getType() != Material.LAPIS_BLOCK)
            return false;
        else if (center.getBlock().getRelative(0,0,1).getType() != Material.LAPIS_BLOCK)
            return false;
        else if (center.getBlock().getRelative(0,0,-1).getType() != Material.LAPIS_BLOCK)
            return false;
        else if (center.getBlock().getRelative(0,1,0).getType() != Material.ENCHANTMENT_TABLE)
            return false;
        else if (center.getBlock().getRelative(1,1,0).getType() != Material.REDSTONE_WIRE)
            return false;
        else if (center.getBlock().getRelative(0,1,1).getType() != Material.REDSTONE_WIRE)
            return false;
        else if (center.getBlock().getRelative(1,1,1).getType() != Material.REDSTONE_TORCH_ON)
            return false;
        else if (center.getBlock().getRelative(1,1,-1).getType() != Material.REDSTONE_TORCH_ON)
            return false;
        else if (center.getBlock().getRelative(-1,1,0).getType() != Material.REDSTONE_WIRE)
            return false;
        else if (center.getBlock().getRelative(0,1,-1).getType() != Material.REDSTONE_WIRE)
            return false;
        else if (center.getBlock().getRelative(-1,1,1).getType() != Material.REDSTONE_TORCH_ON)
            return false;
        else if (center.getBlock().getRelative(-1,1,-1).getType() != Material.REDSTONE_TORCH_ON)
            return false;

        return true;
    }

}
