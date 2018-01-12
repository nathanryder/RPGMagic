package me.bumblebeee.rpgmagic.commands;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.world.DataException;
import me.bumblebeee.rpgmagic.RPGMagic;
import me.bumblebeee.rpgmagic.Spell;
import me.bumblebeee.rpgmagic.managers.*;
import me.bumblebeee.rpgmagic.utils.HiddenStringUtils;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MagicCommand implements CommandExecutor {

    PaperManager papers = new PaperManager();
    StructureManager spells = new StructureManager();
    InventoryManager inventoryManager = new InventoryManager();
    SpellManager spellManager = new SpellManager();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
        if (cmd.getName().equalsIgnoreCase("magic")) {
            if (!(args.length > 0)) {
                sendHelpMessage(sender);
                return false;
            }

            if (args[0].equalsIgnoreCase("bazar")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "Console cannot use this command!");
                    return false;
                }
                Player p = (Player) sender;
                if (!p.hasPermission("rpgmagic.bazar")) {
                    p.sendMessage(Messages.NO_PERMISSIONS.get());
                    return false;
                }

                String name = RPGMagic.getInstance().getConfig().getString("bazarSchematic");
                File f = new File(RPGMagic.getInstance().getDataFolder() + File.separator + "schematics" + File.separator + name + ".schematic");
                if (!f.exists()) {
                    p.sendMessage(Messages.ERROR_WITH_SCHEMATIC.get().replace("%operation%", "find").replace("%reason%", ""));
                    return false;
                }
                EditSession es = new EditSession(new BukkitWorld(p.getWorld()), 999999999);
                CuboidClipboard cc = null;
                try {
                    cc = CuboidClipboard.loadSchematic(f);
                } catch (DataException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    p.sendMessage(Messages.ERROR_WITH_SCHEMATIC.get().replace("%operation%", "open").replace("%reason%", ""));
                    return false;
                }

                if (cc == null) {
                    p.sendMessage(Messages.ERROR_WITH_SCHEMATIC.get().replace("%operation%", "open").replace("%reason%", ""));
                    return false;
                }

                Vector v = new Vector(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
                try {
                    cc.paste(es, v, false);
                } catch (MaxChangedBlocksException e) {
                    p.sendMessage(Messages.ERROR_WITH_SCHEMATIC.get().replace("%operation%", "paste").replace("%reason%", ": Too many blocks"));
                    return false;
                }

                p.sendMessage(Messages.PASTED_SCHEMATIC.get());
                return true;
            } else if (args[0].equalsIgnoreCase("structurebazar") || args[0].equalsIgnoreCase("paperbazar") || args[0].equalsIgnoreCase("wandbazar")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "Console cannot use this command!");
                    return false;
                }
                Player p = (Player) sender;
                if (!p.hasPermission("rpgmagic.npc")) {
                    p.sendMessage(Messages.NO_PERMISSIONS.get());
                    return false;
                }

                String type = args[0].replace("bazar", "");

                Location l = p.getLocation();
                String skin = RPGMagic.getInstance().getConfig().getString("npcs." + type + ".skin");
                NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "BumbleBeee_");
                npc.setName("Setup Required");
                npc.data().setPersistent(NPC.PLAYER_SKIN_UUID_METADATA, skin);
                npc.spawn(l);
                return true;
            } else if (args[0].equalsIgnoreCase("create")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "Console cannot use this command!");
                    return false;
                }
                Player p = (Player) sender;
                if (!p.hasPermission("rpgmagic.create")) {
                    p.sendMessage(Messages.NO_PERMISSIONS.get());
                    return false;
                }

                if (args.length < 3) {
                    p.sendMessage(Messages.INVALID_ARGS.get().replace("%usage%", "/magic create <type> <info>"));
                    return false;
                }

                String cat = args[1];
                if (cat.equalsIgnoreCase("paper")) {
                    if (args.length < 4) {
                        p.sendMessage(Messages.INVALID_ARGS.get().replace("%usage%", "/magic create <type> <info>"));
                        return false;
                    }

                    String type = args[2];
                    if (type.equalsIgnoreCase("level")) {
                        int level;
                        try {
                            level = Integer.parseInt(args[3]);
                        } catch (NumberFormatException e) {
                            p.sendMessage(Messages.NOT_A_NUMBER.get().replace("%number%", args[3]));
                            return false;
                        }

                        ItemStack i = papers.getPaperItem("level", level, "", 0, 0);
                        p.getInventory().addItem(i);
                        p.sendMessage(Messages.ITEM_GIVEN.get());
                    } else if (type.equalsIgnoreCase("power")) {
                        int power;
                        try {
                            power = Integer.parseInt(args[3]);
                        } catch (NumberFormatException e) {
                            p.sendMessage(Messages.NOT_A_NUMBER.get().replace("%number%", args[3]));
                            return false;
                        }

                        ItemStack i = papers.getPaperItem("power", 0, "", 0, power);
                        p.getInventory().addItem(i);
                        p.sendMessage(Messages.ITEM_GIVEN.get());
                    } else if (type.equalsIgnoreCase("area")) {
                        if (args.length < 5) {
                            p.sendMessage(Messages.INVALID_ARGS.get());
                            return false;
                        }

                        String shape = args[3];
                        if (!shapeExists(shape)) {
                            p.sendMessage(Messages.SHAPE_DOES_NOT_EXIST.get().replace("%shape%", shape));
                            return false;
                        }

                        int distance;
                        try {
                            distance = Integer.parseInt(args[4]);
                        } catch (NumberFormatException e) {
                            p.sendMessage(Messages.NOT_A_NUMBER.get().replace("%number%", args[4]));
                            return false;
                        }

                        ItemStack i = papers.getPaperItem("effectArea", 0, shape, distance, 0);
                        if (i == null)
                            return false;
                        p.getInventory().addItem(i);
                        p.sendMessage(Messages.ITEM_GIVEN.get());
                    } else {
                        p.sendMessage(Messages.TYPE_NOT_FOUND.get().replace("%type%", type));
                        return false;
                    }
                } else if (cat.equalsIgnoreCase("spell")) {
                    ItemStack i = spells.getItemById(args[2], false);
                    if (i == null) {
                        p.sendMessage(Messages.SPELL_NOT_FOUND.get().replace("%spell%", args[2]));
                        return false;
                    }

                    p.getInventory().addItem(i);
                    p.sendMessage(Messages.ITEM_GIVEN.get());
                } else if (cat.equalsIgnoreCase("wand")) {
                    if (p.getInventory().getItemInMainHand() == null) {
                        p.sendMessage(Messages.NOT_HOLDING_WAND.get());
                        return false;
                    }
                    ItemStack i = p.getInventory().getItemInMainHand();
                    if (i.getType() != Material.DIAMOND_SWORD) {
                        p.sendMessage(Messages.NOT_HOLDING_WAND.get());
                        return false;
                    }

                    String shape = args[5].toLowerCase();
                    String spell = args[2].toLowerCase();
                    int level;
                    int power;
                    int distance;

                    if (spell.split("_").length > 1)
                        spell = spell.split("_")[0] + " " + spell.split("_")[1];

                    try {
                        level = Integer.parseInt(args[3]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(Messages.NOT_A_NUMBER.get().replace("%number%", args[3]));
                        return false;
                    }
                    try {
                        power = Integer.parseInt(args[4]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(Messages.NOT_A_NUMBER.get().replace("%number%", args[4]));
                        return false;
                    }
                    try {
                        distance = Integer.parseInt(args[6]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(Messages.NOT_A_NUMBER.get().replace("%number%", args[6]));
                        return false;
                    }

                    if (!shapeExists(shape)) {
                        p.sendMessage(Messages.SHAPE_DOES_NOT_EXIST.get().replace("%shape%", shape));
                        return false;
                    }

                    if (!spellManager.spellExists(spell)) {
                        p.sendMessage(Messages.SPELL_NOT_FOUND.get().replace("%spell%", spell));
                        return false;
                    }

                    String display = RPGMagic.getInstance().getConfig().getString("wandItem.display").replace("%spell%", spell);
                    List<String> lore = new ArrayList<>();
                    Spell actualSpell = new Spell(spell);
                    for (String li : RPGMagic.getInstance().getConfig().getStringList("wandItem.lore")) {
                        li = ChatColor.translateAlternateColorCodes('&', li);
                        li = li.replace("%totalpower%", String.valueOf(power));
                        li = li.replace("%lvl%", String.valueOf(level));
                        li = li.replace("%area%", shape).replace("%distance%", String.valueOf(distance));
                        li = li.replace("%description%", actualSpell.getDescription()).replace("%spell%", spell);
                        lore.add(li);
                    }
                    String data = power + ":" + level + ":" + shape + ":" + distance + ":" + spell + ":" + i.getType() + ":" + actualSpell.getDescription();
                    lore.add(HiddenStringUtils.encodeString(data));

                    ItemStack ni = new ItemStack(i.getType(), i.getAmount());
                    ni.setDurability((short) 9);
                    ItemMeta nim = ni.getItemMeta();
                    nim.setUnbreakable(true);
                    nim.setDisplayName(ChatColor.translateAlternateColorCodes('&', display));
                    nim.setLore(lore);
                    nim.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
                    nim.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    ni.setItemMeta(nim);

                    p.getInventory().removeItem(i);
                    p.getInventory().addItem(ni);
                    p.sendMessage(Messages.ITEM_GIVEN.get());
                } else {
                    p.sendMessage(Messages.CATEGORY_NOT_FOUND.get().replace("%type%", cat));
                }
                return true;
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("rpgmagic.reload")) {
                    sender.sendMessage(Messages.NO_PERMISSIONS.get());
                    return false;
                }
                if (!sender.hasPermission("rpgmagic.chest")) {
                    sender.sendMessage(Messages.NO_PERMISSIONS.get());
                    return false;
                }

                RPGMagic.getInstance().reloadConfig();
                sender.sendMessage(Messages.RELOAD_SUCCESS.get());
                return true;
            } else if (args[0].equalsIgnoreCase("chest")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "Console cannot use this command!");
                    return false;
                }
                Player p = (Player) sender;
                if (!p.hasPermission("rpgmagic.chest")) {
                    p.sendMessage(Messages.NO_PERMISSIONS.get());
                    return false;
                }

                inventoryManager.openGlobalChest(p);
            } else if (args[0].equalsIgnoreCase("menu")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "Console cannot use this command!");
                    return false;
                }
                Player p = (Player) sender;

                if (!p.hasPermission("rpgmagic.menu")) {
                    p.sendMessage(Messages.NO_PERMISSIONS.get());
                    return false;
                }

                inventoryManager.openMenu(p);
                return true;
            } else if (args[0].equalsIgnoreCase("adminmenu")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "Console cannot use this command!");
                    return false;
                }
                Player p = (Player) sender;

                if (!p.hasPermission("rpgmagic.adminmenu")) {
                    p.sendMessage(Messages.NO_PERMISSIONS.get());
                    return false;
                }

                inventoryManager.openAdminMainMenu(p);
                return true;
            }
            else if (args[0].equalsIgnoreCase("wanditembazar")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "Console cannot use this command!");
                    return false;
                }
                Player p = (Player) sender;

                if (!p.hasPermission("rpgmagic.bazarwand")) {
                    p.sendMessage(Messages.NO_PERMISSIONS.get());
                    return false;
                }

                String name = RPGMagic.getInstance().getConfig().getString("npcs.wanditems.name");
                String skin = RPGMagic.getInstance().getConfig().getString("npcs.wanditems.skin");
                Location l = p.getLocation();
                NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "BumbleBeee_");
                npc.setName(name);
                npc.data().setPersistent(NPC.PLAYER_SKIN_UUID_METADATA, skin);
                npc.spawn(l);
                return true;
            } else if (args[0].equalsIgnoreCase("altaritembazar")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "Console cannot use this command!");
                    return false;
                }
                Player p = (Player) sender;

                if (!p.hasPermission("rpgmagic.bazaraltar")) {
                    p.sendMessage(Messages.NO_PERMISSIONS.get());
                    return false;
                }

                String name = RPGMagic.getInstance().getConfig().getString("npcs.altar.name");
                String skin = RPGMagic.getInstance().getConfig().getString("npcs.altar.skin");
                Location l = p.getLocation();
                NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "BumbleBeee_");
                npc.setName(name);
                npc.data().setPersistent(NPC.PLAYER_SKIN_UUID_METADATA, skin);
                npc.spawn(l);
                return true;
            }
            else {
                sendHelpMessage(sender);
                return true;
            }
        }
        return false;
    }

    public void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(Messages.HELP_LINE.get()
                .replace("%command%", "/magic bazar")
                .replace("%description%", "Spawn in the shop structure"));
        sender.sendMessage(Messages.HELP_LINE.get()
                .replace("%command%", "/magic structurebazar")
                .replace("%description%", "Spawns the NPC for spells"));
        sender.sendMessage(Messages.HELP_LINE.get()
                .replace("%command%", "/magic paperbazar")
                .replace("%description%", "Spawns the NPC for papers"));
        sender.sendMessage(Messages.HELP_LINE.get()
                .replace("%command%", "/magic wandbazar")
                .replace("%description%", "Spawn the NPC for wands"));
        sender.sendMessage(Messages.HELP_LINE.get()
                .replace("%command%", "/magic wanditembazar")
                .replace("%description%", "Spawns the NPC for wand items"));
        sender.sendMessage(Messages.HELP_LINE.get()
                .replace("%command%", "/magic altaritembazar")
                .replace("%description%", "Spawns the NPC for altar items"));
        sender.sendMessage(Messages.HELP_LINE.get()
                .replace("%command%", "/magic create paper <level:power:area> <levelPowerDistance> [Shape]")
                .replace("%description%", "Create a paper with the given values"));
        sender.sendMessage(Messages.HELP_LINE.get()
                .replace("%command%", "/magic create spell <spellName>")
                .replace("%description%", "Create a spell"));
        sender.sendMessage(Messages.HELP_LINE.get()
                .replace("%command%", "/magic create wand <spellName> <level> <power> <shape> <distance>")
                .replace("%description%", "Create a wand with the given values"));
        sender.sendMessage(Messages.HELP_LINE.get()
                .replace("%command%", "/magic reload")
                .replace("%description%", "Reload the plugin"));
        sender.sendMessage(Messages.HELP_LINE.get()
                .replace("%command%", "/magic chest")
                .replace("%description%", "Open the wizards chest"));
        sender.sendMessage(Messages.HELP_LINE.get()
                .replace("%command%", "/magic menu")
                .replace("%description%", "Open the main menu"));
        sender.sendMessage(Messages.HELP_LINE.get()
                .replace("%command%", "/magic adminmenu")
                .replace("%description%", "Open the admin menu"));
    }

    public boolean shapeExists(String shape) {
        if (shape.equalsIgnoreCase("coni"))
            return true;
        else if (shape.equalsIgnoreCase("raggio"))
            return true;
        else if (shape.equalsIgnoreCase("linee"))
            return true;
        else
            return false;
    }
}