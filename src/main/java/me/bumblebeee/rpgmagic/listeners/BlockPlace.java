package me.bumblebeee.rpgmagic.listeners;

import me.bumblebeee.rpgmagic.utils.TableUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Block b = e.getBlockPlaced();

        if (b.getType() == Material.ENCHANTMENT_TABLE) {
            if (b.getLocation().subtract(0, 1, 0).getBlock().getType() != Material.BEACON)
                return;

            boolean check = TableUtils.checkShape(b.getLocation().subtract(0, 1, 0));
            if (check) {
                b.getWorld().strikeLightningEffect(b.getLocation());
                b.getWorld().playSound(b.getLocation(), Sound.ENTITY_ENDERDRAGON_AMBIENT, 10, 10);
            }
        } else if (b.getType() == Material.BEACON) {
            if (b.getLocation().add(0, 1, 0).getBlock().getType() != Material.ENCHANTMENT_TABLE)
                return;

            boolean check = TableUtils.checkShape(b.getLocation());
            if (check) {
                b.getWorld().strikeLightningEffect(b.getLocation());
                b.getWorld().playSound(b.getLocation(), Sound.ENTITY_ENDERDRAGON_AMBIENT, 10, 10);
            }
        } else if (b.getType() == Material.REDSTONE_WIRE) {
            Location l = b.getLocation();
            if (l.clone().add(1, 0, 0).getBlock().getType() == Material.ENCHANTMENT_TABLE) {
                Location checkLoc = l.clone().add(1,0,0).subtract(0, 1, 0);
                boolean check = TableUtils.checkShape(checkLoc);
                if (check) {
                    b.getWorld().strikeLightningEffect(checkLoc);
                    b.getWorld().playSound(checkLoc, Sound.ENTITY_ENDERDRAGON_AMBIENT, 10, 10);
                }
            } else if (l.clone().add(-1, 0, 0).getBlock().getType() == Material.ENCHANTMENT_TABLE) {
                Location checkLoc = l.clone().add(-1,0,0).subtract(0, 1, 0);
                boolean check = TableUtils.checkShape(checkLoc);
                if (check) {
                    b.getWorld().strikeLightningEffect(checkLoc);
                    b.getWorld().playSound(checkLoc, Sound.ENTITY_ENDERDRAGON_AMBIENT, 10, 10);
                }
            } else if (l.clone().add(0, 0, 1).getBlock().getType() == Material.ENCHANTMENT_TABLE) {
                Location checkLoc = l.clone().add(0,0,1).subtract(0, 1, 0);
                boolean check = TableUtils.checkShape(checkLoc);
                if (check) {
                    b.getWorld().strikeLightningEffect(checkLoc);
                    b.getWorld().playSound(checkLoc, Sound.ENTITY_ENDERDRAGON_AMBIENT, 10, 10);
                }
            } else if (l.clone().add(0, 0, -1).getBlock().getType() == Material.ENCHANTMENT_TABLE) {
                Location checkLoc = l.clone().add(0,0,-1).subtract(0, 1, 0);
                boolean check = TableUtils.checkShape(checkLoc);
                if (check) {
                    b.getWorld().strikeLightningEffect(checkLoc);
                    b.getWorld().playSound(checkLoc, Sound.ENTITY_ENDERDRAGON_AMBIENT, 10, 10);
                }
            }
        } else if (b.getType() == Material.REDSTONE_TORCH_ON) {
            Location l = b.getLocation();
            if (l.clone().add(1,0,1).getBlock().getType() == Material.ENCHANTMENT_TABLE) {
                Location checkLoc = l.clone().add(1,0,1).subtract(0, 1, 0);
                boolean check = TableUtils.checkShape(checkLoc);
                if (check) {
                    b.getWorld().strikeLightningEffect(checkLoc);
                    b.getWorld().playSound(checkLoc, Sound.ENTITY_ENDERDRAGON_AMBIENT, 10, 10);
                }
            } else if (l.clone().add(1,0,-1).getBlock().getType() == Material.ENCHANTMENT_TABLE) {
                Location checkLoc = l.clone().add(1,0,-1).subtract(0, 1, 0);
                boolean check = TableUtils.checkShape(checkLoc);
                if (check) {
                    b.getWorld().strikeLightningEffect(checkLoc);
                    b.getWorld().playSound(checkLoc, Sound.ENTITY_ENDERDRAGON_AMBIENT, 10, 10);
                }
            } else if (l.clone().add(-1,0,-1).getBlock().getType() == Material.ENCHANTMENT_TABLE) {
                Location checkLoc = l.clone().add(-1,0,-1).subtract(0, 1, 0);
                boolean check = TableUtils.checkShape(checkLoc);
                if (check) {
                    b.getWorld().strikeLightningEffect(checkLoc);
                    b.getWorld().playSound(checkLoc, Sound.ENTITY_ENDERDRAGON_AMBIENT, 10, 10);
                }
            } else if (l.clone().add(-1,0,1).getBlock().getType() == Material.ENCHANTMENT_TABLE) {
                Location checkLoc = l.clone().add(-1,0,1).subtract(0, 1, 0);
                boolean check = TableUtils.checkShape(checkLoc);
                if (check) {
                    b.getWorld().strikeLightningEffect(checkLoc);
                    b.getWorld().playSound(checkLoc, Sound.ENTITY_ENDERDRAGON_AMBIENT, 10, 10);
                }
            }
        }
    }

}