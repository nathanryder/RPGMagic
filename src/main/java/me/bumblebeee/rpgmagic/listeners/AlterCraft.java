package me.bumblebeee.rpgmagic.listeners;

import me.baks.rpl.Files;
import me.baks.rpl.PlayerList;
import me.baks.rpl.api.API;
import me.bumblebeee.rpgmagic.RPGMagic;
import me.bumblebeee.rpgmagic.events.AlterCraftEvent;
import me.bumblebeee.rpgmagic.managers.SpellManager;
import me.bumblebeee.rpgmagic.managers.StructureManager;
import me.bumblebeee.rpgmagic.utils.HiddenStringUtils;
import me.bumblebeee.rpgmagic.utils.Storage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlterCraft implements Listener {

    StructureManager spellManager = new StructureManager();
    Storage storage = new Storage();

    @EventHandler
    public void onAlterCraft(AlterCraftEvent e) {
        ItemStack[] matrix = e.getMatrix();
        Player p = e.getPlayer();

        if (matrix[0] != null) {
            if (matrix[0].getType() != Material.AIR)
                e.setCancelled(true);
        }
        if (matrix[2] != null) {
            if (matrix[2].getType() != Material.AIR)
                e.setCancelled(true);
        }
        if (matrix[6] != null) {
            if (matrix[6].getType() != Material.AIR)
                e.setCancelled(true);
        }
        if (matrix[8] != null) {
            if (matrix[8].getType() != Material.AIR)
                e.setCancelled(true);
        }
        if (matrix[1] == null || matrix[3] == null || matrix[4] == null
                    || matrix[5] == null || matrix[7] == null) {
            e.setCancelled(true);
            return;
        }

        if (matrix[7].getType() != Material.STRUCTURE_VOID) {
            e.setCancelled(true);
            return;
        }
        if (matrix[3].getType() != Material.PAPER) {
            e.setCancelled(true);
            return;
        }
        if (matrix[4].getType() != Material.PAPER) {
            e.setCancelled(true);
            return;
        }
        if (matrix[5].getType() != Material.PAPER) {
            e.setCancelled(true);
            return;
        }

        if (!(matrix[1].getType() == Material.END_ROD)) {
            e.setCancelled(true);
            return;
        }

        double level = -1;
        double power = -1;
        double distance = -1;
        String shape = "null";

        for (int i = 3; i < 6; i++) {
            if (!matrix[i].hasItemMeta())
                continue;
            if (!matrix[i].getItemMeta().hasLore())
                continue;

            List<String> lore = matrix[i].getItemMeta().getLore();
            if (!HiddenStringUtils.hasHiddenString(lore.get(lore.size()-1)))
                return;

            String[] data = HiddenStringUtils.extractHiddenString(lore.get(lore.size()-1)).split(":");
            String type = data[0];

            if (type.equalsIgnoreCase("level")) {
                try {
                    level = Double.parseDouble(data[1]);
                } catch (Exception e1) {
                    p.sendMessage(ChatColor.RED + "Failed to get level. Continuing anyway..");
                }
            } else if (type.equalsIgnoreCase("power")) {
                try {
                    power = Double.parseDouble(data[1]);
                } catch (Exception e1) {
                    p.sendMessage(ChatColor.RED + "Failed to get power. Continuing anyway..");
                }
            } else if (type.equalsIgnoreCase("shape")) {
                String main = data[1].split("\\|")[0];
                shape = main.substring(0,1).toUpperCase() + main.substring(1);
                try {
                    distance = Double.parseDouble(data[2]);
                } catch (Exception e1) {
                    p.sendMessage(ChatColor.RED + "Failed to get distance. Continuing anyway..");
                }
            }
        }

        if (level == -1 || power == -1 || distance == -1 || shape.equals("null"))
            return;

        FileConfiguration c = Files.manaConfig;
        int rplPower = c.getInt("MagicWands." + matrix[1].getType().name() + ".Damage");
        int rplDistance = c.getInt("MagicWands." + matrix[1].getType().name() + ".Distance");
        distance += rplDistance;

        /*
            This gets the player level - reason this is here is because of an error when accessing RPL API
        */
        PlayerList t = PlayerList.getByName(p.getName());
        int plevel = t != null ? t.getPlayerLevel():-1;

        double itemPower = power + rplPower + plevel;
        if (itemPower >= 5000)
            itemPower = 4999;

        String spellName = null;
        if (matrix[7].hasItemMeta() && matrix[7].getItemMeta().hasLore()) {
            List<String> spellLore = matrix[7].getItemMeta().getLore();
            if (HiddenStringUtils.hasHiddenString(spellLore.get(spellLore.size()-1)))
                spellName = HiddenStringUtils.extractHiddenString(spellLore.get(spellLore.size()-1)).split(":")[1];
        }
        if (spellName == null)
            return;

        String display = RPGMagic.getInstance().getConfig().getString("wandItem.display").replace("%spell%", spellName);
        List<String> lore = new ArrayList<>();
        String desc = SpellManager.getSpell(spellName.toLowerCase()).getDescription();
        for (String li : RPGMagic.getInstance().getConfig().getStringList("wandItem.lore")) {
            li = ChatColor.translateAlternateColorCodes('&', li);
            li = li.replace("%totalpower%", String.valueOf(itemPower));
            li = li.replace("%lvl%", String.valueOf(level));
            li = li.replace("%area%", shape).replace("%distance%", String.valueOf(distance));
            li = li.replace("%description%", desc).replace("%spell%", spellName);
            lore.add(li);
        }
        String data = itemPower + ":" + level + ":" + shape + ":" + distance + ":" + spellName + ":" + matrix[1].getType() + ":" + desc;
        lore.add(HiddenStringUtils.encodeString(data));

        ItemStack i = new ItemStack(Material.DIAMOND_SWORD);
        i.setDurability((short) 9);
        ItemMeta im = i.getItemMeta();
        im.setUnbreakable(true);
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', display));
        im.setLore(lore);
        im.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        i.setItemMeta(im);

        ItemStack[] newMatrix = {null, null, null, null, null, null, null, null, i};
        for (int j = 0; j < matrix.length; j++) {
            ItemStack it = matrix[j];
            if (it == null)
                continue;
            if (it.getAmount() > 1) {
                it.setAmount(it.getAmount() - 1);
                newMatrix[j] = it;
            }
        }

        storage.addWandPlayer(p.getUniqueId(), data);
        Storage.getCrafts().remove(e.getLocation());
        Storage.getCrafts().put(Storage.getOpenInventories().get(p.getUniqueId()), newMatrix);
    }
}