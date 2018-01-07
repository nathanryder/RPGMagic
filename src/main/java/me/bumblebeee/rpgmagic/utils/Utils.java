package me.bumblebeee.rpgmagic.utils;

import me.bumblebeee.rpgmagic.RPGMagic;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.PacketPlayOutBlockChange;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;

import java.io.File;
import java.util.*;

public class Utils {

    public static void disguiseBlock(Player player, Location location, Material material, byte data) {
        new BukkitRunnable() {
            @Override
            public void run() {

                BlockPosition blockPosition = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
                PacketPlayOutBlockChange packet = new PacketPlayOutBlockChange(((CraftWorld) location.getWorld()).getHandle(), blockPosition);
                packet.block =  net.minecraft.server.v1_12_R1.Block.getByCombinedId(material.getId() + (data << 12));
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }
        }.runTaskAsynchronously(RPGMagic.getInstance());
    }

    public static Map<UUID, String> searchForPlayer(String name) {
        File folder = new File(RPGMagic.getInstance().getDataFolder() + File.separator + "data");
        File[] listOfFiles = folder.listFiles();
        Map<UUID, String> players = new HashMap<>();

        if (listOfFiles == null)
            return new HashMap<>();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (!listOfFiles[i].isFile())
                continue;

            String suuid = listOfFiles[i].getName().split("\\.")[0];
            if (suuid.equalsIgnoreCase("storage"))
                continue;

            UUID uuid = UUID.fromString(suuid);
            Player t = Bukkit.getServer().getPlayer(uuid);
            if (t == null) {
                OfflinePlayer ot = Bukkit.getServer().getOfflinePlayer(uuid);
                if (!ot.hasPlayedBefore())
                    continue;

                if (ot.getName().toLowerCase().contains(name.toLowerCase()))
                    players.put(uuid, ot.getName());
            } else {
                if (t.getName().toLowerCase().contains(name.toLowerCase()))
                    players.put(uuid, t.getName());
            }
        }

        return players;
    }

    public static Map<UUID,String> getAllPlayers() {
        File folder = new File(RPGMagic.getInstance().getDataFolder() + File.separator + "data");
        File[] listOfFiles = folder.listFiles();
        Map<UUID, String> players = new HashMap<>();

        if (listOfFiles == null)
            return new HashMap<>();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (!listOfFiles[i].isFile())
                continue;

            String suuid = listOfFiles[i].getName().split("\\.")[0];
            if (suuid.equalsIgnoreCase("storage"))
                continue;

            UUID uuid = UUID.fromString(suuid);
            Player t = Bukkit.getServer().getPlayer(uuid);
            if (t == null) {
                OfflinePlayer ot = Bukkit.getServer().getOfflinePlayer(uuid);
                players.put(uuid, ot.getName());
            } else {
                players.put(uuid, t.getName());
            }
        }

        return players;
    }

    public static Entity getPlayerTarget(Player p, int range) {
        List<Entity> nearbyE = p.getNearbyEntities(range, range, range);
        ArrayList<LivingEntity> livingE = new ArrayList<LivingEntity>();

        for (Entity e : nearbyE) {
            if (e instanceof LivingEntity) {
                livingE.add((LivingEntity) e);
            }
        }

        Entity target = null;
        BlockIterator bItr = new BlockIterator(p, range);
        Block block;
        Location loc;
        int bx, by, bz;
        double ex, ey, ez;
        // loop through player's line of sight
        while (bItr.hasNext()) {
            block = bItr.next();
            if ((block.getType() != Material.AIR) && !block.isLiquid())
                break;

            bx = block.getX();
            by = block.getY();
            bz = block.getZ();
            // check for entities near this block in the line of sight
            for (LivingEntity e : livingE) {
                loc = e.getLocation();
                ex = loc.getX();
                ey = loc.getY();
                ez = loc.getZ();
                if ((bx-.75 <= ex && ex <= bx+1.75) && (bz-.75 <= ez && ez <= bz+1.75) && (by-1 <= ey && ey <= by+2.5)) {
                    // entity is close enough, set target and stop
                    target = e;
                    break;
                }
            }
        }

        return target;
    }

    public static String getCardinalDirection(Location loc) {
        double rot = (loc.getYaw() - 90) % 360;
        if (rot < 0) {
            rot += 360.0;
        }
        return getDirection(rot);
    }

    private static String getDirection(double rot) {
        if (45 <= rot && rot < 135) {
            return "North";
        } else if (135 <= rot && rot < 225) {
            return "East";
        } else if (225 <= rot && rot < 315) {
            return "South";
        } else if (315 <= rot && rot < 360) {
            return "West";
        } else if (0 <= rot && rot < 45) {
            return "West";
        } else {
            return null;
        }
    }
}
