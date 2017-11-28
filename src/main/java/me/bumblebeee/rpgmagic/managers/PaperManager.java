package me.bumblebeee.rpgmagic.managers;

import me.bumblebeee.rpgmagic.RPGMagic;
import me.bumblebeee.rpgmagic.utils.HiddenStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PaperManager {

    NPCManager npcs = new NPCManager();

    //data - type:params:params:
    public void addPaper(ItemStack paper, int npcId, String typeInfo, double price) {
        YamlConfiguration c = npcs.getFile();
        int counter = c.getInt("npcs." + npcId + ".papers.counter")+1;
        List<String> addLore = new ArrayList<>();
        for (String l : RPGMagic.getInstance().getConfig().getStringList("npcs.paper.addLore"))
            addLore.add(ChatColor.translateAlternateColorCodes('&', l).replace("%price%", String.valueOf(price)));

        String[] data = typeInfo.split(":");
        String type = data[0];

        c.set("npcs." + npcId + ".papers." + counter + ".material", paper.getType().name());
        c.set("npcs." + npcId + ".papers." + counter + ".data", paper.getDurability());
        c.set("npcs." + npcId + ".papers." + counter + ".type", type);
        c.set("npcs." + npcId + ".papers." + counter + ".price", price);
        c.set("npcs." + npcId + ".papers." + counter + ".lore", paper.getItemMeta().getLore());
        c.set("npcs." + npcId + ".papers." + counter + ".addLore", addLore);
        if (paper.hasItemMeta() && paper.getItemMeta().hasDisplayName())
            c.set("npcs." + npcId + ".papers." + counter + ".display", paper.getItemMeta().getDisplayName());
        if (paper.hasItemMeta() && paper.getItemMeta().hasLore()) {
            List<String> lore = new ArrayList<>();
            for (String li : paper.getItemMeta().getLore()) {
                if (type.equalsIgnoreCase("level")) {
                    lore.add(li.replace("%lvl%", data[1]));
                    c.set("level", data[1]);
                } else if (type.equalsIgnoreCase("power")) {
                    lore.add(li.replace("%power%", data[1]));
                    c.set("power", data[1]);
                } else if (type.equalsIgnoreCase("effect area")) {
                    lore.add(li.replace("%area%", data[1]).replace("%distance%", data[2]));
                    c.set("area", data[1]);
                    c.set("distance", data[2]);
                }
            }
            c.set("npcs." + npcId + ".papers." + counter + ".lore", lore);
        }

        if (type.equalsIgnoreCase("level")) {
            c.set("npcs." + npcId + ".papers." + counter + ".level", data[1]);
        } else if (type.equalsIgnoreCase("power")) {
            c.set("npcs." + npcId + ".papers." + counter + ".power", data[1]);
        } else if (type.equalsIgnoreCase("effect area")) {
            c.set("npcs." + npcId + ".papers." + counter + ".shape", data[1]);
            c.set("npcs." + npcId + ".papers." + counter + ".distance", data[2]);
        }

        c.set("npcs." + npcId + ".papers.counter", counter);
        npcs.saveFile(c);
    }

    public void deletePaper(int id, int npcId) {
        YamlConfiguration c = npcs.getFile();

        c.set("npcs." + npcId + ".papers." + id, null);
        npcs.saveFile(c);
    }

    public double getPrice(int npcId, int itemId, String type) {
        YamlConfiguration c = npcs.getFile();
        if (c.getConfigurationSection("npcs." + npcId + "." + type + "." + itemId) == null)
            return 0;
        type = type.toLowerCase();

        double price = c.getDouble("npcs." + npcId + "." + type + "." + itemId + ".price");
        return price;
    }
    public String getPaperType(int npcId, int itemId, String type) {
        YamlConfiguration c = npcs.getFile();
        if (c.getConfigurationSection("npcs." + npcId + "." + type + "." + itemId) == null)
            return null;
        type = type.toLowerCase();

        String paperType = c.getString("npcs." + npcId + "." + type + "." + itemId + ".type");
        return paperType;
    }


    public int getPwrLvlDist(int npcId, int itemId, String type) {
        YamlConfiguration c = npcs.getFile();
        if (c.getConfigurationSection("npcs." + npcId + "." + type + "." + itemId) == null)
            return 0;
        type = type.toLowerCase();

        String level = c.getString("npcs." + npcId + "." + type + "." + itemId + ".level");
        String power = c.getString("npcs." + npcId + "." + type + "." + itemId + ".power");
        String distance = c.getString("npcs." + npcId + "." + type + "." + itemId + ".distance");

        if (level != null)
            return Integer.parseInt(level);
        else if (power != null)
            return Integer.parseInt(power);
        else if (distance != null)
            return Integer.parseInt(distance);

        return 0;
    }

    public String getShape(int npcId, int itemId, String type) {
        YamlConfiguration c = npcs.getFile();
        if (c.getConfigurationSection("npcs." + npcId + "." + type + "." + itemId) == null)
            return null;
        type = type.toLowerCase();

        String shape = c.getString("npcs." + npcId + "." + type + "." + itemId + ".shape");

        if (shape != null)
            return shape;

        return null;
    }

    public ItemStack getItemById(int npcId, int paperId, String type) {
        YamlConfiguration c = npcs.getFile();
        if (c.getConfigurationSection("npcs." + npcId + "." + type + "." + paperId) == null)
            return null;
        type = type.toLowerCase();

        Material m;
        try {
            m = Material.matchMaterial(c.getString("npcs." + npcId + "." + type + "." + paperId + ".material"));
        } catch (Exception e) {
            return null;
        }
        short data = (short) c.getInt("npcs." + npcId + "." + type + "." + paperId + ".material");
        String display = c.getString("npcs." + npcId + "." + type + "." + paperId + ".display");
        String paperType = c.getString("npcs." + npcId + "." + type + "." + paperId + ".type");
        List<String> lore = new ArrayList<>();

        for (String loreItem : c.getStringList("npcs." + npcId + "." + type + "." + paperId + ".lore"))
            lore.add(ChatColor.translateAlternateColorCodes('&', loreItem));

        ItemStack item = new ItemStack(m, 1, data);
        ItemMeta im = item.getItemMeta();
        item.setDurability(data);
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', display));

        if (paperType.equalsIgnoreCase("level")) {
            String level = c.getString("npcs." + npcId + "." + type + "." + paperId + ".level");
            lore.add(HiddenStringUtils.encodeString("level:" + level));
        } else if (paperType.equalsIgnoreCase("power")) {
            String power = c.getString("npcs." + npcId + "." + type + "." + paperId + ".power");
            lore.add(HiddenStringUtils.encodeString("power:" + power));
        } else if (paperType.equalsIgnoreCase("effect area")) {
            String shape = c.getString("npcs." + npcId + "." + type + "." + paperId + ".shape");
            String distance = c.getString("npcs." + npcId + "." + type + "." + paperId + ".distance");
            lore.add(HiddenStringUtils.encodeString("shape:" + shape + "|distance:" + distance));
        }
        im.setLore(lore);
        item.setItemMeta(im);

        return item;
    }

    public ItemStack getPaperItem(String type, int level, String area, int distance, int power) {
        YamlConfiguration c = getFile();
        if (type.equalsIgnoreCase("effect area"))
            type = "effectArea";
        if (!c.getConfigurationSection("papers").contains(type))
            return null;

        Material m;
        try {
            m = Material.matchMaterial(c.getString("papers." + type + ".item"));
        } catch (Exception e) {
            return null;
        }

        String display = c.getString("papers." + type + ".name");
        List<String> lore = c.getStringList("papers." + type + ".lore");
        List<String> newLore = new ArrayList<>();
        for (String s : lore) {
            if (type.equalsIgnoreCase("level")) {
                newLore.add(ChatColor.translateAlternateColorCodes('&', s.replace("%lvl%", String.valueOf(level))));
            } else if (type.equalsIgnoreCase("effectarea")) {
                newLore.add(ChatColor.translateAlternateColorCodes('&', s.replace("%area%", area).replace("%distance%", String.valueOf(distance))));
            } else if (type.equalsIgnoreCase("power")) {
                newLore.add(ChatColor.translateAlternateColorCodes('&', s.replace("%power%", String.valueOf(power))));
            }
        }

        if (type.equalsIgnoreCase("level"))
            newLore.add(HiddenStringUtils.encodeString("level:" + level));
        else if (type.equalsIgnoreCase("power"))
            newLore.add(HiddenStringUtils.encodeString("power:" + power));
        else if (type.equalsIgnoreCase("effectarea"))
            newLore.add(HiddenStringUtils.encodeString("shape:" + area + "|distance:" + distance));

        ItemStack i = new ItemStack(m);
        i.setDurability(((short) c.getInt("papers." + type + ".durability")));
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', display));
        im.setLore(newLore);
        i.setItemMeta(im);

        return i;
    }

    public YamlConfiguration getFile() {
        File f = new File(RPGMagic.getInstance().getDataFolder() + File.separator + "papers.yml");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return YamlConfiguration.loadConfiguration(f);
    }

    public void saveFile(YamlConfiguration c) {
        File f = new File(RPGMagic.getInstance().getDataFolder() + File.separator + "papers.yml");
        try {
            c.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
