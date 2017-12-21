package me.bumblebeee.rpgmagic.utils;

import lombok.Getter;
import me.bumblebeee.rpgmagic.RPGMagic;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Storage {

    private static @Getter Map<Location, ItemStack[]> craftings = new HashMap<>(); //remove this
    private static @Getter Map<Location, ItemStack[]> crafts = new HashMap<>();
    private static @Getter Map<UUID, Location> openInventories = new HashMap<>();
    private static @Getter Map<UUID, NPC> openNPCs = new HashMap<>();
    private static @Getter Map<UUID, String> typeHolder = new HashMap<>();
    private static @Getter Map<UUID, Double> priceHolder = new HashMap<>();
    private static @Getter Map<UUID, ItemStack> itemHolder = new HashMap<>();
    private static @Getter Map<UUID, UUID> adminMenuHolder = new HashMap<>();
    private static @Getter List<UUID> inputingWandPrice = new ArrayList<>();
    private static @Getter List<UUID> inputingPrice = new ArrayList<>();
    private static @Getter List<UUID> inputingPower = new ArrayList<>();
    private static @Getter List<UUID> inputingLevel = new ArrayList<>();
    private static @Getter List<UUID> inputingEA = new ArrayList<>();
    private static @Getter List<UUID> inputingSearch = new ArrayList<>();
    private static @Getter Map<UUID, Integer> pages = new HashMap<>();
    private static @Getter Map<UUID, String> openPaperType = new HashMap<>();
    private static @Getter Map<UUID, String> categorySelector = new HashMap<>();
    private static @Getter List<ItemStack[]> globalChestStorage = new ArrayList<>();

    private static @Getter List<Player> waterWalk = new ArrayList<>();
    private static @Getter List<Player> wallWalk = new ArrayList<>();

    public void setStorage(ItemStack[] items) {
        YamlConfiguration c = getStorage();

        int slot = 0;
        for (ItemStack item : items) {
            if (item == null)
                continue;
            Material m = item.getType();
            short durability = item.getDurability();
            String name = "";
            List<String> lore = new ArrayList<>();
            if (item.hasItemMeta()) {
                if (item.getItemMeta().hasDisplayName())
                    name = item.getItemMeta().getDisplayName();
                if (item.getItemMeta().hasLore())
                    lore = item.getItemMeta().getLore();
            }

            c.set("chest." + slot + ".material", m.name());
            c.set("chest." + slot + ".amount", item.getAmount());
            c.set("chest." + slot + ".durability", durability);
            c.set("chest." + slot + ".name", name);
            c.set("chest." + slot + ".lore", lore);
            ++slot;
        }
        saveStorage(c);
    }

    public void addWandPlayer(UUID uuid, String args) {
        YamlConfiguration c = getPlayerFile(uuid);
        String[] data = args.split(":");

        String power = data[0];
        String level = data[1];
        String shape = data[2];
        String distance = data[3];
        String spellName = data[4];
        String type = data[5];
        String desc = data[6];

        int counter = c.getInt(uuid + ".wands.counter");
        c.set(uuid + ".wands." + counter + ".type", type);
        c.set(uuid + ".wands." + counter + ".level", level);
        c.set(uuid + ".wands." + counter + ".power", power);
        c.set(uuid + ".wands." + counter + ".shape", shape);
        c.set(uuid + ".wands." + counter + ".distance", distance);
        c.set(uuid + ".wands." + counter + ".spell", spellName);
        c.set(uuid + ".wands." + counter + ".description", desc);

        c.set(uuid + ".wands.counter", counter+1);
        savePlayerFile(uuid, c);
    }

    public List<ItemStack> getPlayerWands(UUID uuid) {
        YamlConfiguration c = getPlayerFile(uuid);
        List<ItemStack> wands = new ArrayList<>();

        if (c.getConfigurationSection(uuid + ".wands") == null)
            return new ArrayList<>();

        for (String id : c.getConfigurationSection(uuid + ".wands").getKeys(false)) {
            if (id.equalsIgnoreCase("counter"))
                continue;
            String type = c.getString(uuid + ".wands." + id + ".type");
            String level = c.getString(uuid + ".wands." + id + ".level");
            String power = c.getString(uuid + ".wands." + id + ".power");
            String shape = c.getString(uuid + ".wands." + id + ".shape");
            String distance = c.getString(uuid + ".wands." + id + ".distance");
            String spellName = c.getString(uuid + ".wands." + id + ".spell");
            String description = c.getString(uuid + ".wands." + id + ".description");

            Material m = Material.matchMaterial(type);
            ItemStack i = new ItemStack(m);
            ItemMeta im = i.getItemMeta();
            im.setDisplayName(ChatColor.translateAlternateColorCodes('&', RPGMagic.getInstance().getConfig().getString("wandItem.display")));

            List<String> lore = new ArrayList<>();
            for (String li : RPGMagic.getInstance().getConfig().getStringList("wandItem.lore")) {
                li = ChatColor.translateAlternateColorCodes('&', li);
                li = li.replace("%totalpower%", power);
                li = li.replace("%lvl%", level);
                li = li.replace("%spell%", spellName);
                li = li.replace("%area%", shape).replace("%distance%", distance);
                li = li.replace("%description%", description);
                lore.add(li);
            }
            String data = power + ":" + level + ":" + shape + ":" + distance + ":" + spellName + ":" + type + ":" + description;
            lore.add(HiddenStringUtils.encodeString(data));

            im.setLore(lore);
            im.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            i.setItemMeta(im);

            wands.add(i);
        }
        return wands;
    }

    public void addSpellPlayer(UUID uuid, String spellName) {
        YamlConfiguration c = getPlayerFile(uuid);
        List<String> spells = c.getStringList(uuid + ".spells");
        if (spells.contains(spellName))
            return;

        spells.add(spellName.toLowerCase());
        c.set(uuid + ".spells", spells);
        savePlayerFile(uuid, c);
    }

    public List<String> getSpellsForPlayer(UUID uuid) {
        YamlConfiguration c = getPlayerFile(uuid);
        return c.getStringList(uuid + ".spells");
    }

    public List<String> getPapersForPlayer(UUID uuid, String category) {
        YamlConfiguration c = getPlayerFile(uuid);
        List<String> cs = c.getStringList(uuid + ".papers." + category);
        if (cs == null)
            return null;
        return cs;
    }

    public void addPaperPlayer(UUID uuid, String paperType, int lvlPwrDist, String shape) {
        YamlConfiguration c = getPlayerFile(uuid);
        paperType = paperType.toLowerCase();

        if (paperType.equalsIgnoreCase("level")) {
            List<String> levels = c.getStringList(uuid + ".papers." + paperType);
            if (levels.contains(String.valueOf(lvlPwrDist)))
                return;
            levels.add(String.valueOf(lvlPwrDist));
            c.set(uuid + ".papers." + paperType, levels);
        } else if (paperType.equalsIgnoreCase("power")) {
            List<String> powers = c.getStringList(uuid + ".papers." + paperType);
            if (powers.contains(String.valueOf(lvlPwrDist)))
                return;
            powers.add(String.valueOf(lvlPwrDist));
            c.set(uuid + ".papers." + paperType, powers);
        } else if (paperType.equalsIgnoreCase("effect area")) {
            String str = shape + ":" + String.valueOf(lvlPwrDist);
            List<String> areas = c.getStringList(uuid + ".papers." + paperType);
            if (areas.contains(String.valueOf(str)))
                return;
            areas.add(str);

            c.set(uuid + ".papers." + paperType, areas);
        }
        savePlayerFile(uuid, c);
    }

    public YamlConfiguration getPlayerFile(UUID uuid) {
        File folder = new File(RPGMagic.getInstance().getDataFolder() + File.separator + "data");
        if (!folder.exists())
            folder.mkdirs();

        File f = new File(RPGMagic.getInstance().getDataFolder() + File.separator + "data" + File.separator + uuid+".yml");

        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return YamlConfiguration.loadConfiguration(f);
    }

    public void savePlayerFile(UUID uuid, YamlConfiguration c) {
        File f = new File(RPGMagic.getInstance().getDataFolder() + File.separator + "data" + File.separator + uuid+".yml");

        try {
            c.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public YamlConfiguration getStorage() {
        File f = new File(RPGMagic.getInstance().getDataFolder() + File.separator + "data" + File.separator +"storage.yml");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return YamlConfiguration.loadConfiguration(f);
    }

    public void saveStorage(YamlConfiguration c) {
        File f = new File(RPGMagic.getInstance().getDataFolder() + File.separator + "data" + File.separator + "storage.yml");
        try {
            c.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveStorageChest() {
        if (Storage.getGlobalChestStorage().size() < 1)
            return;

        YamlConfiguration c = getStorage();
        ItemStack[] items = Storage.getGlobalChestStorage().get(0);
        Inventory inv = Bukkit.getServer().createInventory(null, 54, "");
        inv.setContents(items);

        String store = InventoryToBase64.toBase64(inv);
        c.set("storageChest", store);
        saveStorage(c);
    }

    public void loadStorageChest() {
        YamlConfiguration c = getStorage();
        String data = c.getString("storageChest");
        if (data == null)
            return;

        Inventory inv = null;
        try {
            inv = InventoryToBase64.fromBase64(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (inv == null)
            return;

        Storage.getGlobalChestStorage().clear();
        Storage.getGlobalChestStorage().add(inv.getContents());
    }

}
