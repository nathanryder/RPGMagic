package me.bumblebeee.rpgmagic.managers;

import me.bumblebeee.rpgmagic.RPGMagic;
import me.bumblebeee.rpgmagic.utils.HiddenStringUtils;
import me.bumblebeee.rpgmagic.utils.Storage;
import me.bumblebeee.rpgmagic.utils.Utils;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;
import java.util.*;

public class InventoryManager {

    NPCManager npcs = new NPCManager();
    StructureManager structures = new StructureManager();
    PaperManager papers = new PaperManager();
    Storage storage = new Storage();

    public void openMenu(Player p) {
        String title = getMessage("inventory.playerMenu.title", false);
        String closeMsg = getMessage("inventory.playerMenu.close", true);
        String spellMsg = getMessage("inventory.playerMenu.structure", true);
        String paperMsg = getMessage("inventory.playerMenu.paper", true);
        String wandMsg = getMessage("inventory.playerMenu.wand", true);
        Inventory inv = Bukkit.getServer().createInventory(null, 27, title);

        ItemStack breaker = createItem(Material.STAINED_GLASS_PANE, 1, (short) 14, " ", null);
        ItemStack close = createItem(Material.NETHER_STAR, 1, (short) 0, closeMsg, null);
        ItemStack spell = createItem(Material.STRUCTURE_VOID, 1, (short) 0, spellMsg, null);
        ItemStack paper = createItem(Material.PAPER, 1, (short) 0, paperMsg, null);
        ItemStack wand = createItem(Material.BLAZE_ROD, 1, (short) 0, wandMsg, null);

        for (int i = 0; i < 27; i++)
            inv.setItem(i, breaker);

        inv.setItem(11, paper);
        inv.setItem(13, spell);
        inv.setItem(15, wand);
        inv.setItem(22, close);

        p.openInventory(inv);
    }

    public void openAdminMenu(Player admin) {
        String title = getMessage("inventory.playerAdminMenu.title", false);
        String closeMsg = getMessage("inventory.playerAdminMenu.close", true);
        String spellMsg = getMessage("inventory.playerAdminMenu.structure", true);
        String paperMsg = getMessage("inventory.playerAdminMenu.paper", true);
        String wandMsg = getMessage("inventory.playerAdminMenu.wand", true);
        Inventory inv = Bukkit.getServer().createInventory(null, 27, title);

        ItemStack breaker = createItem(Material.STAINED_GLASS_PANE, 1, (short) 14, " ", null);
        ItemStack close = createItem(Material.NETHER_STAR, 1, (short) 0, closeMsg, null);
        ItemStack spell = createItem(Material.STRUCTURE_VOID, 1, (short) 0, spellMsg, null);
        ItemStack paper = createItem(Material.PAPER, 1, (short) 0, paperMsg, null);
        ItemStack wand = createItem(Material.BLAZE_ROD, 1, (short) 0, wandMsg, null);

        for (int i = 0; i < 27; i++)
            inv.setItem(i, breaker);

        inv.setItem(11, paper);
        inv.setItem(13, spell);
        inv.setItem(15, wand);
        inv.setItem(22, close);

        admin.openInventory(inv);
    }

    public void openAdminMainMenu(Player admin) {
        String title = getMessage("inventory.playerAdminMainMenu.title", false);
        String searchMsg = getMessage("inventory.playerAdminMainMenu.search", true);
        String allMsg = getMessage("inventory.playerAdminMainMenu.all", true);
        Inventory inv = Bukkit.getServer().createInventory(null, 27, title);

        ItemStack breaker = createItem(Material.STAINED_GLASS_PANE, 1, (short) 14, " ", null);
        ItemStack all = createItem(Material.SKULL_ITEM, 1, (short)0, allMsg, null);
        ItemStack search = createItem(Material.GLASS, 1, (short)0, searchMsg, null);

        for (int i = 0; i < 27; i++)
            inv.setItem(i, breaker);

        inv.setItem(11, search);
        inv.setItem(15, all);
        admin.openInventory(inv);
    }

    public void openAdminPlayers(Player admin, String search, int page) {
        String title = getMessage("inventory.adminPlayerMenu.title", false);
        String nextMsg = getMessage("inventory.adminPlayerMenu.next", true);
        String prevMsg = getMessage("inventory.adminPlayerMenu.previous", true);
        Inventory inv = Bukkit.getServer().createInventory(null, 36, title);

        ItemStack breaker = createItem(Material.STAINED_GLASS_PANE, 1, (short) 14, " ", null);
        ItemStack next = createItem(Material.STAINED_GLASS_PANE, 1, (short) 5, nextMsg, null);
        ItemStack prev = createItem(Material.STAINED_GLASS_PANE, 1, (short) 5, prevMsg, null);

        for (int i = 27; i < 36; i++)
            inv.setItem(i, breaker);
        inv.setItem(27, prev);
        inv.setItem(35, next);

        if (search == null) {
            Map<UUID, String> players = Utils.getAllPlayers();
            if (players == null) {
                admin.openInventory(inv);
                return;
            }

            int maxPages = (int) Math.ceil(players.size()/21)+1;
            if (page >= maxPages)
                page = 0;
            else if (page < 0)
                page = maxPages;
//            if (first)
//                page = 0;
            Storage.getPages().put(admin.getUniqueId(), page);

            int offset = 26*page;
            if (page > 0) offset++;

            int i = 0;
            for (UUID uuid : players.keySet()) {
                if (i < offset) {
                    i++;
                    continue;
                }
                if (i >= (offset+27))
                    break;

                String name = players.get(uuid);
                ItemStack skull = createItem(Material.SKULL_ITEM, 1, (short)0, name, null);
                SkullMeta sm = (SkullMeta) skull.getItemMeta();
                sm.setOwner(name);
                skull.setItemMeta(sm);

                int counter = i >= offset ? i-offset : i;
                inv.setItem(counter, skull);
                i++;
            }
        } else {
            Map<UUID, String> players = Utils.searchForPlayer(search);
            if (players == null) {
                admin.openInventory(inv);
                return;
            }

            int maxPages = (int) Math.ceil(players.size()/21)+1;
            if (page >= maxPages)
                page = 0;
            else if (page < 0)
                page = maxPages;
//            if (first)
//                page = 0;
            Storage.getPages().put(admin.getUniqueId(), page);

            int offset = 26*page;
            if (page > 0) offset++;

            int i = 0;
            for (UUID uuid : players.keySet()) {
                if (i < offset) {
                    i++;
                    continue;
                }
                if (i >= (offset+27))
                    break;

                String name = players.get(uuid);
                ItemStack skull = createItem(Material.SKULL_ITEM, 1, (short)0, name, null);
                SkullMeta sm = (SkullMeta) skull.getItemMeta();
                sm.setOwner(name);
                skull.setItemMeta(sm);

                int counter = i >= offset ? i-offset : i;
                inv.setItem(counter, skull);
                i++;
            }
        }

        admin.openInventory(inv);
    }

    public void openSpellsMenu(Player p, int page, boolean first) {
        String title = getMessage("inventory.playerSpellMenu.title", false);
        String nextMsg = getMessage("inventory.playerSpellMenu.next", true);
        String prevMsg = getMessage("inventory.playerSpellMenu.previous", true);
        Inventory inv = Bukkit.getServer().createInventory(null, 36, title);

        ItemStack breaker = createItem(Material.STAINED_GLASS_PANE, 1, (short) 14, " ", null);
        ItemStack next = createItem(Material.STAINED_GLASS_PANE, 1, (short) 5, nextMsg, null);
        ItemStack prev = createItem(Material.STAINED_GLASS_PANE, 1, (short) 5, prevMsg, null);

        YamlConfiguration c = structures.getFile();
        ConfigurationSection cs = c.getConfigurationSection("spells");
        if (cs == null)
            return;
        int maxPages = (int) Math.ceil(c.getConfigurationSection("spells").getKeys(false).size()/21)+1;

        if (page >= maxPages)
            page = 0;
        else if (page <= 0)
            page = maxPages;
        if (first)
            page = 0;
        Storage.getPages().put(p.getUniqueId(), page);


        for (int i = 27; i < 36; i++)
            inv.setItem(i, breaker);
        inv.setItem(27, prev);
        inv.setItem(35, next);

        int offset = 26*page;
        if (page > 0) offset++;
        List<String> spells = storage.getSpellsForPlayer(p.getUniqueId());
        int counter = 0;
        for (int i = offset; i < spells.size(); i++) {
            if (counter > 26)
                break;

            inv.setItem(counter, structures.getItemById(spells.get(i), false));
            counter++;
        }

        p.openInventory(inv);
    }

    public void openSpellsAdminMenu(UUID p, Player admin, int page, boolean first) {
        String title = getMessage("inventory.playerSpellAdminMenu.title", false);
        String nextMsg = getMessage("inventory.playerSpellAdminMenu.next", true);
        String prevMsg = getMessage("inventory.playerSpellAdminMenu.previous", true);
        Inventory inv = Bukkit.getServer().createInventory(null, 36, title);

        ItemStack breaker = createItem(Material.STAINED_GLASS_PANE, 1, (short) 14, " ", null);
        ItemStack next = createItem(Material.STAINED_GLASS_PANE, 1, (short) 5, nextMsg, null);
        ItemStack prev = createItem(Material.STAINED_GLASS_PANE, 1, (short) 5, prevMsg, null);

        YamlConfiguration c = structures.getFile();
        ConfigurationSection cs = c.getConfigurationSection("spells");
        if (cs == null)
            return;
        int maxPages = (int) Math.ceil(c.getConfigurationSection("spells").getKeys(false).size()/21)+1;

        if (page >= maxPages)
            page = 0;
        else if (page <= 0)
            page = maxPages;
        if (first)
            page = 0;
        Storage.getPages().put(admin.getUniqueId(), page);


        for (int i = 27; i < 36; i++)
            inv.setItem(i, breaker);
        inv.setItem(27, prev);
        inv.setItem(35, next);

        int offset = 26*page;
        if (page > 0) offset++;
        List<String> spells = storage.getSpellsForPlayer(p);
        int counter = 0;
        for (int i = offset; i < spells.size(); i++) {
            if (counter > 26)
                break;

            inv.setItem(counter, structures.getItemById(spells.get(i), false));
            counter++;
        }

        admin.openInventory(inv);
    }

    public void openWandMenu(Player p, int page, boolean first) {
        String title = getMessage("inventory.playerWandMenu.title", false);
        String nextMsg = getMessage("inventory.playerWandMenu.next", true);
        String prevMsg = getMessage("inventory.playerWandMenu.previous", true);
        Inventory inv = Bukkit.getServer().createInventory(null, 36, title);

        ItemStack breaker = createItem(Material.STAINED_GLASS_PANE, 1, (short) 14, " ", null);
        ItemStack next = createItem(Material.STAINED_GLASS_PANE, 1, (short) 5, nextMsg, null);
        ItemStack prev = createItem(Material.STAINED_GLASS_PANE, 1, (short) 5, prevMsg, null);

        YamlConfiguration c = storage.getPlayerFile(p.getUniqueId());
        int maxPages = 1;
        if (c.getConfigurationSection(p.getUniqueId() + ".wands") != null)
            maxPages = (int) Math.ceil(c.getConfigurationSection(p.getUniqueId() + ".wands").getKeys(false).size()/21)+1;

        if (page >= maxPages)
            page = 0;
        else if (page <= 0)
            page = maxPages;
        if (first)
            page = 0;
        Storage.getPages().put(p.getUniqueId(), page);


        for (int i = 27; i < 36; i++)
            inv.setItem(i, breaker);
        inv.setItem(27, prev);
        inv.setItem(35, next);

        int offset = 26*page;
        if (page > 0) offset++;

        List<ItemStack> wands = storage.getPlayerWands(p.getUniqueId());
        int counter = 0;
        for (int i = offset; i < wands.size(); i++) {
            if (counter > 26)
                break;

            inv.setItem(counter, wands.get(i));
            counter++;
        }

        p.openInventory(inv);
    }

    public void openWandAdminMenu(UUID p, Player admin, int page, boolean first) {
        String title = getMessage("inventory.playerWandAdminMenu.title", false);
        String nextMsg = getMessage("inventory.playerWandAdminMenu.next", true);
        String prevMsg = getMessage("inventory.playerWandAdminMenu.previous", true);
        Inventory inv = Bukkit.getServer().createInventory(null, 36, title);

        ItemStack breaker = createItem(Material.STAINED_GLASS_PANE, 1, (short) 14, " ", null);
        ItemStack next = createItem(Material.STAINED_GLASS_PANE, 1, (short) 5, nextMsg, null);
        ItemStack prev = createItem(Material.STAINED_GLASS_PANE, 1, (short) 5, prevMsg, null);

        YamlConfiguration c = storage.getPlayerFile(p);
        int maxPages = 1;
        if (c.getConfigurationSection(p + ".wands") != null)
            maxPages = (int) Math.ceil(c.getConfigurationSection(p + ".wands").getKeys(false).size()/21)+1;

        if (page >= maxPages)
            page = 0;
        else if (page <= 0)
            page = maxPages;
        if (first)
            page = 0;
        Storage.getPages().put(admin.getUniqueId(), page);


        for (int i = 27; i < 36; i++)
            inv.setItem(i, breaker);
        inv.setItem(27, prev);
        inv.setItem(35, next);

        int offset = 26*page;
        if (page > 0) offset++;

        List<ItemStack> wands = storage.getPlayerWands(p);
        int counter = 0;
        for (int i = offset; i < wands.size(); i++) {
            if (counter > 26)
                break;

            inv.setItem(counter, wands.get(i));
            counter++;
        }

        admin.openInventory(inv);
    }

    public void openPaperMenu(Player p, String category, int page, boolean first) {
        if (category == null) {
            String title = getMessage("inventory.playerPaperSelectMenu.title", false);
            String levelMsg = getMessage("inventory.playerPaperSelectMenu.level", true);
            String powerMsg = getMessage("inventory.playerPaperSelectMenu.power", true);
            String areaMsg = getMessage("inventory.playerPaperSelectMenu.effectArea", true);
            Inventory inv = Bukkit.getServer().createInventory(null, 27, title);

            ItemStack breaker = createItem(Material.STAINED_GLASS_PANE, 1, (short) 14, " ", null);
            ItemStack level = createItem(Material.NETHER_STAR, 1, (short) 0, levelMsg, null);
            ItemStack power = createItem(Material.STRUCTURE_VOID, 1, (short) 0, powerMsg, null);
            ItemStack area = createItem(Material.PAPER, 1, (short) 0, areaMsg, null);

            for (int i = 0; i < 27; i++)
                inv.setItem(i, breaker);
            inv.setItem(13, power);
            inv.setItem(11, level);
            inv.setItem(15, area);

            p.openInventory(inv);
        } else {
            String title = getMessage("inventory.playerPaperMenu.title", false);
            String nextMsg = getMessage("inventory.playerPaperMenu.next", true);
            String prevMsg = getMessage("inventory.playerPaperMenu.previous", true);
            Inventory inv = Bukkit.getServer().createInventory(null, 36, title);

            ItemStack breaker = createItem(Material.STAINED_GLASS_PANE, 1, (short) 14, " ", null);
            ItemStack next = createItem(Material.STAINED_GLASS_PANE, 1, (short) 5, nextMsg, null);
            ItemStack prev = createItem(Material.STAINED_GLASS_PANE, 1, (short) 5, prevMsg, null);

            YamlConfiguration c = structures.getFile();
            ConfigurationSection cs = c.getConfigurationSection("spells");
            if (cs == null)
                return;
            int maxPages = (int) Math.ceil(c.getConfigurationSection("spells").getKeys(false).size() / 21) + 1;

            if (page >= maxPages)
                page = 0;
            else if (page <= 0)
                page = maxPages;
            if (first)
                page = 0;
            Storage.getPages().put(p.getUniqueId(), page);

            for (int i = 27; i < 36; i++)
                inv.setItem(i, breaker);
            inv.setItem(27, prev);
            inv.setItem(35, next);

            int offset = 26 * page;
            if (page > 0) offset++;
            List<String> spells = storage.getPapersForPlayer(p.getUniqueId(), category);

            int counter = 0;
            for (int i = offset; i < spells.size(); i++) {
                if (counter > 26)
                    break;

                //type level area distance power
                if (category.equalsIgnoreCase("level"))
                    inv.setItem(counter, papers.getPaperItem(category, Integer.parseInt(spells.get(i)), null, 0, 0));
                else if (category.equalsIgnoreCase("power"))
                    inv.setItem(counter, papers.getPaperItem(category, 0, null, 0, Integer.parseInt(spells.get(i))));
                else if (category.equalsIgnoreCase("effect area")) {
                    String[] data = spells.get(i).split(":");
                    inv.setItem(counter, papers.getPaperItem(category, 0, data[0], Integer.parseInt(data[1]), 0));
                }
                counter++;
            }

            p.openInventory(inv);
        }
    }

    public void openPaperAdminMenu(UUID p, Player admin, String category, int page, boolean first) {
        if (category == null) {
            String title = getMessage("inventory.playerPaperAdminSelectMenu.title", false);
            String levelMsg = getMessage("inventory.playerPaperAdminSelectMenu.level", true);
            String powerMsg = getMessage("inventory.playerPaperAdminSelectMenu.power", true);
            String areaMsg = getMessage("inventory.playerPaperAdminSelectMenu.effectArea", true);
            Inventory inv = Bukkit.getServer().createInventory(null, 27, title);

            ItemStack breaker = createItem(Material.STAINED_GLASS_PANE, 1, (short) 14, " ", null);
            ItemStack level = createItem(Material.NETHER_STAR, 1, (short) 0, levelMsg, null);
            ItemStack power = createItem(Material.STRUCTURE_VOID, 1, (short) 0, powerMsg, null);
            ItemStack area = createItem(Material.PAPER, 1, (short) 0, areaMsg, null);

            for (int i = 0; i < 27; i++)
                inv.setItem(i, breaker);
            inv.setItem(13, power);
            inv.setItem(11, level);
            inv.setItem(15, area);

            admin.openInventory(inv);
        } else {
            String title = getMessage("inventory.playerPaperAdminMenu.title", false);
            String nextMsg = getMessage("inventory.playerPaperAdminMenu.next", true);
            String prevMsg = getMessage("inventory.playerPaperAdminMenu.previous", true);
            Inventory inv = Bukkit.getServer().createInventory(null, 36, title);

            ItemStack breaker = createItem(Material.STAINED_GLASS_PANE, 1, (short) 14, " ", null);
            ItemStack next = createItem(Material.STAINED_GLASS_PANE, 1, (short) 5, nextMsg, null);
            ItemStack prev = createItem(Material.STAINED_GLASS_PANE, 1, (short) 5, prevMsg, null);

            YamlConfiguration c = structures.getFile();
            ConfigurationSection cs = c.getConfigurationSection("spells");
            if (cs == null)
                return;
            int maxPages = (int) Math.ceil(c.getConfigurationSection("spells").getKeys(false).size() / 21) + 1;

            if (page >= maxPages)
                page = 0;
            else if (page <= 0)
                page = maxPages;
            if (first)
                page = 0;
            Storage.getPages().put(admin.getUniqueId(), page);

            for (int i = 27; i < 36; i++)
                inv.setItem(i, breaker);
            inv.setItem(27, prev);
            inv.setItem(35, next);

            int offset = 26 * page;
            if (page > 0) offset++;
            List<String> spells = storage.getPapersForPlayer(p, category);

            int counter = 0;
            for (int i = offset; i < spells.size(); i++) {
                if (counter > 26)
                    break;

                //type level area distance power
                if (category.equalsIgnoreCase("level"))
                    inv.setItem(counter, papers.getPaperItem(category, Integer.parseInt(spells.get(i)), null, 0, 0));
                else if (category.equalsIgnoreCase("power"))
                    inv.setItem(counter, papers.getPaperItem(category, 0, null, 0, Integer.parseInt(spells.get(i))));
                else if (category.equalsIgnoreCase("effect area")) {
                    String[] data = spells.get(i).split(":");
                    inv.setItem(counter, papers.getPaperItem(category, 0, data[0], Integer.parseInt(data[1]), 0));
                }
                counter++;
            }

            admin.openInventory(inv);
        }
    }

    public void openGlobalChest(Player p) {
        String title = getMessage("inventory.globalChest.title", false);
        Inventory inv = Bukkit.getServer().createInventory(null, 54, title);

        if (Storage.getGlobalChestStorage().size() > 0) {
            ItemStack[] contents = Storage.getGlobalChestStorage().get(0);
            inv.setContents(contents);
        }
        p.openInventory(inv);
    }

    public void openStructureShop(Player p, int page) {
        String title = getMessage("inventory.structureShop.title", false);
        String closeMsg = getMessage("inventory.structureShop.close", true);
        String nextMsg = getMessage("inventory.structureShop.next", true);
        String prevMsg = getMessage("inventory.structureShop.previous", true);
        Inventory inv = Bukkit.getServer().createInventory(null, 36, title);

        YamlConfiguration c = structures.getFile();
        ConfigurationSection cs = c.getConfigurationSection("spells");
        if (cs == null)
            return;

        int max = (int) Math.ceil(c.getConfigurationSection("spells").getKeys(false).size()/21);
        if (page < 0) {
            page = max;
            Storage.getPages().put(p.getUniqueId(), page);
        } else if (page > max) {
            page = 0;
            Storage.getPages().put(p.getUniqueId(), page);
        }

        ItemStack breaker = createItem(Material.STAINED_GLASS_PANE, 1, (short) 14, " ", null);
        ItemStack close = createItem(Material.NETHER_STAR, 1, (short) 0, closeMsg, null);
        ItemStack next = createItem(Material.STAINED_GLASS_PANE, 1, (short) 5, nextMsg, null);
        ItemStack prev = createItem(Material.STAINED_GLASS_PANE, 1, (short) 5, prevMsg, null);

        for (int i = 0; i < 36; i++) {
            if (i >= 0 && i <= 8)
                inv.setItem(i, breaker);
            else if (i >= 27 && i <= 35)
                inv.setItem(i, breaker);
        }
        inv.setItem(31, close);
        inv.setItem(35, next);
        inv.setItem(27, prev);

        int counter = 9;
        int offset = (17*page);
        if (page > 0)
            offset++;

        Map<Integer, String> ids = new HashMap<>();
        int nameCounter = 0;
        for (String name : c.getConfigurationSection("spells").getKeys(false)) {
            ids.put(nameCounter, name);
            nameCounter++;
        }

        for (int id = offset; id <= (offset+17); id++) {
            if (counter >= 27)
                break;
            if (!ids.containsKey(id))
                continue;
            if (id >= ids.size())
                break;

            ItemStack i = structures.getItemById(ids.get(id), true);
            inv.setItem(counter, i);
            counter++;
        }

        p.openInventory(inv);
    }

    public void openNPCSetup(Player p) {
        Inventory inv = Bukkit.getServer().createInventory(null, 27, getMessage("inventory.npcSetupTypeSelection.title", false));

        ItemStack breaker = createItem(Material.STAINED_GLASS_PANE, 1, (short) 14, " ", null);
        ItemStack wand = createItem(Material.BLAZE_ROD, 1, (short)0, getMessage("inventory.npcSetupTypeSelection.itemNames.wand", false), null);
        ItemStack paper = createItem(Material.PAPER, 1, (short)0, getMessage("inventory.npcSetupTypeSelection.itemNames.paper", false), null);
        ItemStack struct = createItem(Material.STRUCTURE_VOID, 1, (short)0, getMessage("inventory.npcSetupTypeSelection.itemNames.structure", false), null);


        for (int i = 0; i < 27; i++) {
            inv.setItem(i, breaker);
        }
        inv.setItem(10, paper);
        inv.setItem(13, struct);
        inv.setItem(16, wand);

        p.openInventory(inv);
    }

    /*
        stage 1 - add/delete paper, remove npc gui
        stage 2 - add paper gui
        stage 3 - delete paper gui
     */
    public void openPaperSetup(Player p, NPC npc, int stage) {
        Inventory inv = null;
        if (stage == 1) {
            String title = getMessage("inventory.npcPaperOptions.title", false);
            String addPaperMsg = getMessage("inventory.npcPaperOptions.itemNames.addPaper", true);
            String delPaperMsg = getMessage("inventory.npcPaperOptions.itemNames.removePaper", true);
            String delNPCMsg = getMessage("inventory.npcPaperOptions.itemNames.deleteNPC", true);

            inv = Bukkit.getServer().createInventory(null, 27, title);

            ItemStack breaker = createItem(Material.STAINED_GLASS_PANE, 1, (short) 14, " ", null);
            ItemStack addPaper = createItem(Material.STAINED_GLASS_PANE, 1, (short) 5, addPaperMsg, null);
            ItemStack delPaper = createItem(Material.STAINED_GLASS_PANE, 1, (short) 1, delPaperMsg, null);
            ItemStack delNPC = createItem(Material.STAINED_GLASS_PANE, 1, (short) 4, delNPCMsg, null);

            for (int i = 0; i < 27; i++)
                inv.setItem(i, breaker);
            inv.setItem(10, addPaper);
            inv.setItem(13, delPaper);
            inv.setItem(16, delNPC);

        } else if (stage == 2) {
            addPaperGUI(p);
        } else if (stage == 3) {
            removePaperGUI(p, npc.getId());
        }

        if (inv != null)
            p.openInventory(inv);
    }

    /*
        stage 1 - add/delete wand, remove npc gui
        stage 2 - add wand gui
        stage 3 - delete and gui
     */
    public void openWandSetup(Player p, NPC npc, int stage) {
        Inventory inv = null;
        if (stage == 1) {
            String title = getMessage("inventory.npcWandOptions.title", false);
            String addWandMsg = getMessage("inventory.npcWandOptions.addWand", true);
            String delWandMsg = getMessage("inventory.npcWandOptions.removeWand", true);
            String delNPCMsg = getMessage("inventory.npcWandOptions.deleteNPC", true);

            inv = Bukkit.getServer().createInventory(null, 27, title);

            ItemStack breaker = createItem(Material.STAINED_GLASS_PANE, 1, (short) 14, " ", null);
            ItemStack addWand = createItem(Material.STAINED_GLASS_PANE, 1, (short) 5, addWandMsg, null);
            ItemStack delWand = createItem(Material.STAINED_GLASS_PANE, 1, (short) 1, delWandMsg, null);
            ItemStack delNPC = createItem(Material.STAINED_GLASS_PANE, 1, (short) 4, delNPCMsg, null);

            for (int i = 0; i < 27; i++)
                inv.setItem(i, breaker);
            inv.setItem(10, addWand);
            inv.setItem(13, delWand);
            inv.setItem(16, delNPC);

        } else if (stage == 2) {
            addWandGUI(p);
        } else if (stage == 3) {
            removeWandGUI(p, npc.getId());
        }

        if (inv != null)
            p.openInventory(inv);
    }

    public void removePaperGUI(Player p, int npcId) {
        String title = getMessage("inventory.removePaper.title", false);
        Inventory inv = Bukkit.getServer().createInventory(null, 36, title);

        YamlConfiguration c = npcs.getFile();
        ConfigurationSection cs = c.getConfigurationSection("npcs." + npcId + ".papers");
        if (cs == null)
            return;

        ItemStack breaker = createItem(Material.STAINED_GLASS_PANE, 1, (short) 14, " ", null);
        ItemStack close = createItem(Material.NETHER_STAR, 1, (short) 0, "&cClose", null);
        for (int i = 0; i < 36; i++) {
            if (i >= 0 && i <= 8)
                inv.setItem(i, breaker);
            else if (i >= 27 && i <= 35)
                inv.setItem(i, breaker);
        }
        inv.setItem(31, close);

        int counter = 9;
        for (String id : cs.getKeys(false)) {
            if (counter >= 27)
                continue;

            Material m;
            try {
                m = Material.matchMaterial(c.getString("npcs." + npcId + ".papers." + id + ".material"));
            } catch (Exception e) {
                continue;
            }
            short data = (short) c.getInt("npcs." + npcId + ".papers." + id + ".data");
            String display = ChatColor.translateAlternateColorCodes('&', c.getString("npcs." + npcId + ".papers." + id + ".display"));
            List<String> lore = new ArrayList<>();
            for (String loreItem : c.getStringList("npcs." + npcId + ".papers." + id + ".lore"))
                lore.add(ChatColor.translateAlternateColorCodes('&', loreItem));

            lore.add(HiddenStringUtils.encodeString("PID:" + id));

            ItemStack i = new ItemStack(m, 1, data);
            ItemMeta im = i.getItemMeta();
            im.setDisplayName(display);
            im.setLore(lore);
            i.setItemMeta(im);

            inv.setItem(counter, i);
            counter++;
        }

        p.openInventory(inv);
    }

    public void removeWandGUI(Player p, int npcId) {
        String title = getMessage("inventory.removeWand.title", false);
        Inventory inv = Bukkit.getServer().createInventory(null, 36, title);

        YamlConfiguration c = npcs.getFile();
        ConfigurationSection cs = c.getConfigurationSection("npcs." + npcId + ".wands");
        if (cs == null)
            return;

        ItemStack breaker = createItem(Material.STAINED_GLASS_PANE, 1, (short) 14, " ", null);
        ItemStack close = createItem(Material.NETHER_STAR, 1, (short) 0, "&cClose", null);
        for (int i = 0; i < 36; i++) {
            if (i >= 0 && i <= 8)
                inv.setItem(i, breaker);
            else if (i >= 27 && i <= 35)
                inv.setItem(i, breaker);
        }
        inv.setItem(31, close);

        int counter = 9;
        for (String id : cs.getKeys(false)) {
            if (counter >= 27)
                continue;

            Material m;
            try {
                m = Material.matchMaterial(c.getString("npcs." + npcId + ".wands." + id + ".material"));
            } catch (Exception e) {
                continue;
            }
            String display = c.getString("npcs." + npcId + ".wands." + id + ".display");
            List<String> lore = new ArrayList<>();
            for (String loreItem : c.getStringList("npcs." + npcId + ".wands." + id + ".lore"))
                lore.add(ChatColor.translateAlternateColorCodes('&', loreItem));

            lore.add(HiddenStringUtils.encodeString("PID:" + id));
            ItemStack i = new ItemStack(m, 1);
            ItemMeta im = i.getItemMeta();

            if (display != null)
                im.setDisplayName(ChatColor.translateAlternateColorCodes('&', display));
            im.setLore(lore);
            i.setItemMeta(im);

            inv.setItem(counter, i);
            counter++;
        }

        p.openInventory(inv);
    }

    public void addPaperGUI(Player p) {
        String title = getMessage("inventory.addPaper.title", false);
        String save = getMessage("inventory.addPaper.itemNames.save", true);
        String type = getMessage("inventory.addPaper.itemNames.type", true);
        Inventory inv = Bukkit.getServer().createInventory(null, 27, title);

        if (Storage.getTypeHolder().containsKey(p.getUniqueId()))
            type = type.replace("%type%", Storage.getTypeHolder().get(p.getUniqueId()));
        else
            type = type.replace("%type%", "Level");
        //Level
        //EffectArea
        //Power

        ItemStack breaker = createItem(Material.STAINED_GLASS_PANE, 1, (short) 14, " ", null);
        for (int i = 0; i < 27; i++)
            inv.setItem(i, breaker);

        inv.setItem(26, createItem(Material.STAINED_GLASS_PANE, 1, (short) 5, save, null));
        inv.setItem(11, new ItemStack(Material.AIR));
        inv.setItem(15, createItem(Material.STAINED_GLASS_PANE, 1, (short) 4, type, null));

        p.openInventory(inv);
    }

    public void addWandGUI(Player p) {
        String title = getMessage("inventory.addWand.title", false);
        String save = getMessage("inventory.addWand.save", true);
        Inventory inv = Bukkit.getServer().createInventory(null, 27, title);

        ItemStack breaker = createItem(Material.STAINED_GLASS_PANE, 1, (short) 14, " ", null);
        for (int i = 0; i < 27; i++)
            inv.setItem(i, breaker);

        inv.setItem(26, createItem(Material.STAINED_GLASS_PANE, 1, (short) 5, save, null));
        inv.setItem(13, new ItemStack(Material.AIR));

        p.openInventory(inv);
    }

    public ItemStack createItem(Material material, int amount, short data, String displayName, List<String> lore) {
        ItemStack i = new ItemStack(material, amount, data);
        ItemMeta im = i.getItemMeta();

        if (displayName != null)
            im.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        if (lore != null)
            im.setLore(lore);
        i.setItemMeta(im);
        return i;
    }

    public String getMessage(String key, boolean colored) {
        File f = new File(RPGMagic.getInstance().getDataFolder() + File.separator + "inventories.yml");
        if (!f.exists()) {
           return "Message not found";
        }
        YamlConfiguration c = YamlConfiguration.loadConfiguration(f);
        if (c.getString(key) == null)
            return ChatColor.RED + "[RPGMagic] Failed to find message";

        if (colored)
            return ChatColor.translateAlternateColorCodes('&', c.getString(key));
        else
            return ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', c.getString(key)));
    }

    public void openPaper(Player p) {
        String title = getMessage("inventory.selectPaperType.title", false);
        String levelMsg = getMessage("inventory.selectPaperType.level", true);
        String effectAreaMsg = getMessage("inventory.selectPaperType.effectArea", true);
        String powerMsg = getMessage("inventory.selectPaperType.power", true);
        Inventory inv = Bukkit.getServer().createInventory(null, 27, title);

        ItemStack breaker = createItem(Material.STAINED_GLASS_PANE, 1, (short) 14, " ", null);
        ItemStack close = createItem(Material.NETHER_STAR, 1, (short) 0, "&cClose", null);
        ItemStack level = createItem(Material.STAINED_GLASS_PANE, 1, (short) 0, levelMsg, null);
        ItemStack effectArea = createItem(Material.STAINED_GLASS_PANE, 1, (short) 0, effectAreaMsg, null);
        ItemStack power = createItem(Material.STAINED_GLASS_PANE, 1, (short) 0, powerMsg, null);

        for (int i = 0; i < 27; i++)
            inv.setItem(i, breaker);
        inv.setItem(22, close);
        inv.setItem(11, level);
        inv.setItem(13, effectArea);
        inv.setItem(15, power);

        p.openInventory(inv);
    }

    public void openPaperType(Player p, String type, int npcId, int page) {
        String title = getMessage("inventory.paperShop.title", false);
        String saveMsg = getMessage("inventory.paperShop.save", true);
        String nextMsg = getMessage("inventory.paperShop.next", true);
        String prevMsg = getMessage("inventory.paperShop.previous", true);
        Inventory inv = Bukkit.getServer().createInventory(null, 36, title);

        YamlConfiguration c = npcs.getFile();
        ConfigurationSection cs = c.getConfigurationSection("npcs." + npcId + ".papers");
        if (cs == null)
            return;

        int max = (int) Math.ceil(c.getConfigurationSection("npcs." + npcId + ".papers").getKeys(false).size()/21);
        if (page < 0) {
            page = max;
            Storage.getPages().put(p.getUniqueId(), page);
        } else if (page > max) {
            page = 0;
            Storage.getPages().put(p.getUniqueId(), page);
        }

        ItemStack breaker = createItem(Material.STAINED_GLASS_PANE, 1, (short) 14, " ", null);
        ItemStack close = createItem(Material.NETHER_STAR, 1, (short) 0, saveMsg, null);
        ItemStack next = createItem(Material.STAINED_GLASS_PANE, 1, (short) 5, nextMsg, null);
        ItemStack prev = createItem(Material.STAINED_GLASS_PANE, 1, (short) 5, prevMsg, null);

        for (int i = 0; i < 36; i++) {
            if (i >= 0 && i <= 8)
                inv.setItem(i, breaker);
            else if (i >= 27 && i <= 35)
                inv.setItem(i, breaker);
        }
        inv.setItem(31, close);
        inv.setItem(35, next);
        inv.setItem(27, prev);

        int counter = 9;
        int offset = (21*page);
        if (page > 0)
            offset++;

        for (int id = offset; id <= (offset+21); id++) {
            if (counter >= 27)
                continue;

            String paperType = c.getString("npcs." + npcId + ".papers." + id + ".type");
            if (paperType == null)
                continue;
            if (!paperType.equalsIgnoreCase(type))
                continue;

            Material m;
            try {
                m = Material.matchMaterial(c.getString("npcs." + npcId + ".papers." + id + ".material"));
            } catch (Exception e) {
                continue;
            }
            short data = (short) c.getInt("npcs." + npcId + ".papers." + id + ".data");
            String display = ChatColor.translateAlternateColorCodes('&', c.getString("npcs." + npcId + ".papers." + id + ".display"));
            List<String> lore = new ArrayList<>();
            for (String loreItem : c.getStringList("npcs." + npcId + ".papers." + id + ".lore"))
                lore.add(ChatColor.translateAlternateColorCodes('&', loreItem));
            for (String loreItem : c.getStringList("npcs." + npcId + ".papers." + id + ".addLore"))
                lore.add(ChatColor.translateAlternateColorCodes('&', loreItem));

            lore.add(HiddenStringUtils.encodeString("PID:" + id));

            ItemStack i = new ItemStack(m, 1, data);
            ItemMeta im = i.getItemMeta();
            im.setDisplayName(display);
            im.setLore(lore);
            i.setItemMeta(im);

            inv.setItem(counter, i);
            counter++;
        }

        p.openInventory(inv);
    }

    public void openWandShop(Player p, int npcId, int page) {
        String title = getMessage("inventory.wandShop.title", false);
        Inventory inv = Bukkit.getServer().createInventory(null, 36, title);

        ItemStack breaker = createItem(Material.STAINED_GLASS_PANE, 1, (short) 14, " ", null);

        YamlConfiguration c = npcs.getFile();
        if (c.getConfigurationSection("npcs." + npcId + ".wands") == null) {
            p.openInventory(inv);
            return;
        }

        for (int i = 0; i < 36; i++) {
            if (i >= 0 && i <= 8)
                inv.setItem(i, breaker);
            else if (i >= 27 && i <= 35)
                inv.setItem(i, breaker);
        }

        int counter = 9;
        for (String id : c.getConfigurationSection("npcs." + npcId + ".wands").getKeys(false)) {
            if (id.equalsIgnoreCase("counter"))
                continue;

            Material m;
            try {
                m = Material.matchMaterial(c.getString("npcs." + npcId + ".wands." + id + ".material"));
            } catch (Exception e) {
                p.openInventory(inv);
                return;
            }
            double price = c.getDouble("npcs." + npcId + ".wands." + id + ".price");
            boolean enchanted = c.getBoolean("npcs." + npcId + ".wands." + id + ".enchanted");
            String name = c.getString("npcs." + npcId + ".wands." + id + ".display");
            List<String> lore = c.getStringList("npcs." + npcId + ".wands." + id + ".lore");
            String data = c.getString("npcs." + npcId + ".wands." + id + ".data");

            ItemStack i = new ItemStack(m);
            ItemMeta im = i.getItemMeta();
            if (name != null)
                im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            List<String> newLore = new ArrayList<>();
            if (lore != null) {
                for (String li : lore)
                    newLore.add(ChatColor.translateAlternateColorCodes('&', li));
            }

            newLore.add(HiddenStringUtils.encodeString(data));
            newLore.add(HiddenStringUtils.encodeString("Price:" + price));
            im.setLore(newLore);
            if (enchanted) {
                im.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
                im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                i.setItemMeta(im);
            }
            i.setItemMeta(im);

            inv.setItem(counter, i);
            ++counter;
        }

        p.openInventory(inv);
    }

    public void openAltarItemsShop(Player p, int page) {
        String title = getMessage("inventory.altarItemsShop.title", false);
        Inventory inv = Bukkit.getServer().createInventory(null, 36, title);

        File f = new File(RPGMagic.getInstance().getDataFolder() + File.separator + "shops.yml");
        if (!f.exists())
            RPGMagic.getInstance().saveResource("shops.yml", false);

        YamlConfiguration c = YamlConfiguration.loadConfiguration(f);
        if (c.getConfigurationSection("altarShop.items") == null) {
            p.openInventory(inv);
            return;
        }

        for (String id : c.getConfigurationSection("altarShop.items").getKeys(false)) {
            Material m;
            try {
                m = Material.matchMaterial(c.getString("altarShop.items." + id + ".item"));
            } catch (Exception e) {
                p.openInventory(inv);
                return;
            }
            int slot = c.getInt("altarShop.items." + id + ".slot");
            double price = c.getDouble("altarShop.items." + id + ".price");
            String name = c.getString("altarShop.items." + id + ".name");
            List<String> lore = c.getStringList("altarShop.items." + id + ".lore");

            ItemStack i = new ItemStack(m);
            ItemMeta im = i.getItemMeta();
            if (name != null) {
                String display = ChatColor.translateAlternateColorCodes('&', name);
                im.setDisplayName(display);
            }
            List<String> newLore = new ArrayList<>();
            if (lore != null) {
                for (String li : lore)
                    newLore.add(ChatColor.translateAlternateColorCodes('&', li));
            }
            newLore.add(HiddenStringUtils.encodeString("Price:" + price));
            im.setLore(newLore);
            i.setItemMeta(im);

            inv.setItem(slot, i);
        }

        p.openInventory(inv);
    }

    public void openWandItemsShop(Player p, int page) {
        String title = getMessage("inventory.wandItemsShop.title", false);
        Inventory inv = Bukkit.getServer().createInventory(null, 36, title);

        File f = new File(RPGMagic.getInstance().getDataFolder() + File.separator + "shops.yml");
        if (!f.exists())
            RPGMagic.getInstance().saveResource("shops.yml", false);

        YamlConfiguration c = YamlConfiguration.loadConfiguration(f);
        if (c.getConfigurationSection("wandShop.items") == null) {
            p.openInventory(inv);
            return;
        }

        for (String id : c.getConfigurationSection("wandShop.items").getKeys(false)) {
            Material m;
            try {
                m = Material.matchMaterial(c.getString("wandShop.items." + id + ".item"));
            } catch (Exception e) {
                p.openInventory(inv);
                return;
            }
            int slot = c.getInt("wandShop.items." + id + ".slot");
            double price = c.getDouble("wandShop.items." + id + ".price");
            String name = c.getString("wandShop.items." + id + ".name");
            List<String> lore = c.getStringList("wandShop.items." + id + ".lore");

            ItemStack i = new ItemStack(m);
            ItemMeta im = i.getItemMeta();
            if (name != null) {
                String display = ChatColor.translateAlternateColorCodes('&', name);
                im.setDisplayName(display);
            }
            List<String> newLore = new ArrayList<>();
            if (lore != null) {
                for (String li : lore)
                    newLore.add(ChatColor.translateAlternateColorCodes('&', li));
            }
            newLore.add(HiddenStringUtils.encodeString("Price:" + price));
            im.setLore(newLore);
            i.setItemMeta(im);

            inv.setItem(slot, i);
        }

        p.openInventory(inv);
    }
}
