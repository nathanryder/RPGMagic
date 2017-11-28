package me.bumblebeee.rpgmagic.listeners;

import me.bumblebeee.rpgmagic.RPGMagic;
import me.bumblebeee.rpgmagic.events.ItemBuyEvent;
import me.bumblebeee.rpgmagic.managers.*;
import me.bumblebeee.rpgmagic.utils.HiddenStringUtils;
import me.bumblebeee.rpgmagic.utils.Storage;
import net.citizensnpcs.api.npc.NPC;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InventoryClick implements Listener {

    InventoryManager inv = new InventoryManager();
    NPCManager npcManager = new NPCManager();
    PaperManager paperManager = new PaperManager();
    StructureManager structureManager = new StructureManager();
    WandManager wandManager = new WandManager();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        String npcSetupTitle = inv.getMessage("inventory.npcSetupTypeSelection.title", false);
        String globalChest = inv.getMessage("inventory.globalChest.title", false);
        String playerMenuTitle = inv.getMessage("inventory.playerMenu.title", false);

        String alterTitle = RPGMagic.getInstance().getConfig().getString("alterName");
        String altarItemsShopTitle = inv.getMessage("inventory.altarItemsShop.title", false);
        String spellShop = inv.getMessage("inventory.structureShop.title", false);
        String playerSpellTitle = inv.getMessage("inventory.playerSpellMenu.title", false);

        String paperOptions = inv.getMessage("inventory.npcPaperOptions.title", false);
        String addPaperTitle = inv.getMessage("inventory.addPaper.title", false);
        String removePaperTitle = inv.getMessage("inventory.removePaper.title", false);
        String paperSelectTitle = inv.getMessage("inventory.selectPaperType.title", false);
        String paperShop = inv.getMessage("inventory.paperShop.title", false);
        String playerPaperTitle = inv.getMessage("inventory.playerPaperMenu.title", false);
        String playerPaperSelectTitle = inv.getMessage("inventory.playerPaperSelectMenu.title", false);

        String playerWandTitle = inv.getMessage("inventory.playerWandMenu.title", false);
        String wandShopTitle = inv.getMessage("inventory.wandShop.title", false);
        String npcWandOptionsTitle = inv.getMessage("inventory.npcWandOptions.title", false);
        String addWandTitle = inv.getMessage("inventory.addWand.title", false);
        String removeWandTitle = inv.getMessage("inventory.removeWand.title", false);
        String wandItemShopTitle = inv.getMessage("inventory.wandItemsShop.title", false);

        String playerAdminMenuTitle = inv.getMessage("inventory.playerAdminMenu.title", false);
        String playerAdminSpellTitle = inv.getMessage("inventory.playerSpellAdminMenu.title", false);
        String playerAdminPaperSelectTitle = inv.getMessage("inventory.playerPaperAdminSelectMenu.title", false);
        String playerPaperAdminTitle = inv.getMessage("inventory.playerPaperAdminMenu.title", false);
        String playerWandAdminTitle = inv.getMessage("inventory.playerWandAdminMenu.title", false);

        String title = e.getInventory().getTitle();

        if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta()
                    && e.getCurrentItem().getItemMeta().hasDisplayName()) {
            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            if (clicked.equalsIgnoreCase("close"))
                p.closeInventory();
        }

        if (title.equalsIgnoreCase(npcSetupTitle)) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null)
                return;
            if (!e.getCurrentItem().hasItemMeta())
                return;
            if (!e.getCurrentItem().getItemMeta().hasDisplayName())
                return;
            if (!Storage.getOpenNPCs().containsKey(p.getUniqueId()))
                return;
            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            NPC npc = Storage.getOpenNPCs().get(p.getUniqueId());

            String wandName = inv.getMessage("inventory.npcSetupTypeSelection.itemNames.wand", false);
            String paperName = inv.getMessage("inventory.npcSetupTypeSelection.itemNames.paper", false);
            String structName = inv.getMessage("inventory.npcSetupTypeSelection.itemNames.structure", false);

            if (clicked.equalsIgnoreCase(wandName)) {
                npcManager.setNPCType(npc.getId(), "wand");

                String rename = RPGMagic.getInstance().getConfig().getString("npcs.wand.name");
                String skin = RPGMagic.getInstance().getConfig().getString("npcs.wand.skin");
                npc.setName(rename);
                npc.data().setPersistent(NPC.PLAYER_SKIN_UUID_METADATA, skin);
            } else if (clicked.equalsIgnoreCase(paperName)) {
                npcManager.setNPCType(npc.getId(), "paper");
                inv.openPaperSetup(p, npc, 1);

                String rename = RPGMagic.getInstance().getConfig().getString("npcs.paper.name");
                npc.setName(rename);
            } else if (clicked.equalsIgnoreCase(structName)) {
                npcManager.setNPCType(npc.getId(), "structure");
                p.closeInventory();

                String rename = RPGMagic.getInstance().getConfig().getString("npcs.structure.name");
                npc.setName(rename);
            }
        } else if (title.equalsIgnoreCase(paperOptions)) {
            
            e.setCancelled(true);
            if (e.getCurrentItem() == null)
                return;
            if (!e.getCurrentItem().hasItemMeta())
                return;
            if (!e.getCurrentItem().getItemMeta().hasDisplayName())
                return;
            if (!Storage.getOpenNPCs().containsKey(p.getUniqueId()))
                return;
            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            NPC npc = Storage.getOpenNPCs().get(p.getUniqueId());

            String addPaperName = inv.getMessage("inventory.npcPaperOptions.itemNames.addPaper", false);
            String delPaperName = inv.getMessage("inventory.npcPaperOptions.itemNames.removePaper", false);
            String delNPCName = inv.getMessage("inventory.npcPaperOptions.itemNames.deleteNPC", false);

            if (clicked.equalsIgnoreCase(addPaperName)) {
                inv.addPaperGUI(p);
            } else if (clicked.equalsIgnoreCase(delPaperName)) {
                inv.removePaperGUI(p, npc.getId());
            } else if (clicked.equalsIgnoreCase(delNPCName)) {
                npc.destroy();
                p.sendMessage(Messages.NPC_DESTROYED.get());
                p.closeInventory();
            }
            return;
        } else if (title.equalsIgnoreCase(addPaperTitle)) {
            
            if (e.getCurrentItem() == null)
                return;
            if (!e.getCurrentItem().hasItemMeta())
                return;
            if (!e.getCurrentItem().getItemMeta().hasDisplayName())
                return;
            if (!Storage.getOpenNPCs().containsKey(p.getUniqueId()))
                return;

            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            String save = inv.getMessage("inventory.addPaper.itemNames.save", false);
            String type = inv.getMessage("inventory.addPaper.itemNames.type", false);

            if (clicked.equalsIgnoreCase(save)) {
                
                e.setCancelled(true);

                ItemStack i = e.getInventory().getItem(11);
                if (i == null)
                    return;
                if (i.getType() != Material.PAPER)
                    return;
                if (!i.hasItemMeta())
                    return;
                if (!i.getItemMeta().hasDisplayName())
                    return;
                i.setAmount(1);
                String clickedItem = ChatColor.stripColor(i.getItemMeta().getDisplayName());
                if (clickedItem.equalsIgnoreCase(" "))
                    return;

                //input price
                Storage.getItemHolder().put(p.getUniqueId(), i);
                Storage.getInputingPrice().add(p.getUniqueId());
                p.sendMessage(Messages.ENTER_A_NUMBER.get());
                p.closeInventory();
            } else if (clicked.equalsIgnoreCase(type) || clicked.equalsIgnoreCase("level") ||
                        clicked.equalsIgnoreCase("power") || clicked.equalsIgnoreCase("effect area")) {
                e.setCancelled(true);

                String coloredType = inv.getMessage("inventory.addPaper.itemNames.type", true);
                int slot = e.getRawSlot();
                ItemStack i = inv.createItem(Material.STAINED_GLASS_PANE, 1, (short) 4, type, null);

                if (Storage.getTypeHolder().containsKey(p.getUniqueId())) {
                    if (Storage.getTypeHolder().get(p.getUniqueId()).equalsIgnoreCase("level"))
                        Storage.getTypeHolder().put(p.getUniqueId(), "Effect Area");
                    else if (Storage.getTypeHolder().get(p.getUniqueId()).equalsIgnoreCase("effect area"))
                        Storage.getTypeHolder().put(p.getUniqueId(), "Power");
                    else if (Storage.getTypeHolder().get(p.getUniqueId()).equalsIgnoreCase("power"))
                        Storage.getTypeHolder().put(p.getUniqueId(), "Level");
                } else {
                    Storage.getTypeHolder().put(p.getUniqueId(), "Level");
                }

                coloredType = coloredType.replace("%type%", Storage.getTypeHolder().get(p.getUniqueId()));
                ItemMeta im = i.getItemMeta();
                im.setDisplayName(coloredType);
                i.setItemMeta(im);
                e.getInventory().setItem(slot, i);
            }
            else if (clicked.equalsIgnoreCase("") || clicked.equalsIgnoreCase(" ")) {
                e.setCancelled(true);
            }
            return;
        } else if (title.equalsIgnoreCase(removePaperTitle)) {
            
            e.setCancelled(true);
            if (e.getCurrentItem() == null)
                return;
            if (e.getCurrentItem().getType() != Material.PAPER)
                return;
            if (!e.getCurrentItem().hasItemMeta())
                return;
            if (!e.getCurrentItem().getItemMeta().hasDisplayName())
                return;
            if (!Storage.getOpenNPCs().containsKey(p.getUniqueId()))
                return;
            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            ItemMeta im = e.getCurrentItem().getItemMeta();

            if (clicked.equalsIgnoreCase("close")) {
                p.closeInventory();
                return;
            }

            if (!HiddenStringUtils.hasHiddenString(im.getLore().get(im.getLore().size()-1)))
                return;

            NPC npc = Storage.getOpenNPCs().get(p.getUniqueId());
            String id = HiddenStringUtils.extractHiddenString(im.getLore().get(im.getLore().size()-1)).split(":")[1];
            paperManager.deletePaper(Integer.parseInt(id), npc.getId());

            e.getInventory().setItem(e.getRawSlot(), new ItemStack(Material.AIR));
        } else if (title.equalsIgnoreCase(paperSelectTitle)) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null)
                return;
            if (!e.getCurrentItem().hasItemMeta())
                return;
            if (!e.getCurrentItem().getItemMeta().hasDisplayName())
                return;
            if (!Storage.getOpenNPCs().containsKey(p.getUniqueId()))
                return;
            NPC npc = Storage.getOpenNPCs().get(p.getUniqueId());

            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            String levelMsg = inv.getMessage("inventory.selectPaperType.level", false);
            String effectAreaMsg = inv.getMessage("inventory.selectPaperType.effectArea", false);
            String powerMsg = inv.getMessage("inventory.selectPaperType.power", false);

            if (clicked.equalsIgnoreCase("close")) {
                p.closeInventory();
                return;
            }

            if (clicked.equalsIgnoreCase(levelMsg)) {
                inv.openPaperType(p, "level", npc.getId(), 0);
                Storage.getOpenPaperType().put(p.getUniqueId(), "level");
            } else if (clicked.equalsIgnoreCase(effectAreaMsg)) {
                inv.openPaperType(p, "effect area", npc.getId(), 0);
                Storage.getOpenPaperType().put(p.getUniqueId(), "effect area");
            } else if (clicked.equalsIgnoreCase(powerMsg)) {
                inv.openPaperType(p, "power", npc.getId(), 0);
                Storage.getOpenPaperType().put(p.getUniqueId(), "power");
            }
        } else if (title.equalsIgnoreCase(paperShop)) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null)
                return;
            if (!e.getCurrentItem().hasItemMeta())
                return;
            if (!e.getCurrentItem().getItemMeta().hasDisplayName())
                return;
            if (!Storage.getOpenNPCs().containsKey(p.getUniqueId()))
                return;

            String nextMsg = inv.getMessage("inventory.paperShop.next", false);
            String prevMsg = inv.getMessage("inventory.paperShop.previous", false);
            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            NPC npc = Storage.getOpenNPCs().get(p.getUniqueId());
            List<String> lore = e.getCurrentItem().getItemMeta().getLore();

            if (clicked.equalsIgnoreCase(nextMsg)) {
                String type = Storage.getOpenPaperType().get(p.getUniqueId());
                if (!Storage.getPages().containsKey(p.getUniqueId())) {
                    Storage.getPages().put(p.getUniqueId(), 1);
                    inv.openPaperType(p, type, npc.getId(), 1);
                } else {
                    int page = Storage.getPages().get(p.getUniqueId())+1;
                    Storage.getPages().put(p.getUniqueId(), page);
                    inv.openPaperType(p, type, npc.getId(), page);
                }
            } else if (clicked.equalsIgnoreCase(prevMsg)) {
                String type = Storage.getOpenPaperType().get(p.getUniqueId());
                if (!Storage.getPages().containsKey(p.getUniqueId())) {
                    Storage.getPages().put(p.getUniqueId(), -1);
                    inv.openPaperType(p, type, npc.getId(), -1);
                } else {
                    int page = Storage.getPages().get(p.getUniqueId())-1;
                    Storage.getPages().put(p.getUniqueId(), page);
                    inv.openPaperType(p, type, npc.getId(), page);
                }
            }

            if (e.getCurrentItem().getType() == Material.PAPER) {
                if (!HiddenStringUtils.hasHiddenString(lore.get(lore.size()-1)))
                    return;
                int itemId = Integer.parseInt(HiddenStringUtils.extractHiddenString(lore.get(lore.size()-1)).split(":")[1]);

                double price = paperManager.getPrice(npc.getId(), itemId, "papers");
                EconomyResponse res = RPGMagic.getEcon().withdrawPlayer(p, price);
                if (!res.transactionSuccess()) {
                    p.sendMessage(Messages.NOT_ENOUGH_MONEY.get().replace("%price%", String.valueOf(price)));
                    p.closeInventory();
                    return;
                }

                ItemStack i = paperManager.getItemById(npc.getId(), itemId, "papers");
                if (i == null) {
                    p.sendMessage(ChatColor.RED + "Failed");
                    return;
                }

                int lvlPwrDist = paperManager.getPwrLvlDist(npc.getId(), itemId, "papers");
                String shape = paperManager.getShape(npc.getId(), itemId, "papers");
                String type = paperManager.getPaperType(npc.getId(), itemId, "papers");

                p.getInventory().addItem(i);
                p.closeInventory();

                String data = String.valueOf(itemId) + ":" + type + ":" + lvlPwrDist;
                if (shape != null)
                    data = data + ":" + shape;
                ItemBuyEvent ibe = new ItemBuyEvent(p, price, "paper", data);
                Bukkit.getServer().getPluginManager().callEvent(ibe);
            }
        } else if (title.equalsIgnoreCase(spellShop)) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null)
                return;
            if (!e.getCurrentItem().hasItemMeta())
                return;
            if (!e.getCurrentItem().getItemMeta().hasDisplayName())
                return;
            if (!Storage.getOpenNPCs().containsKey(p.getUniqueId()))
                return;

            String nextMsg = inv.getMessage("inventory.structureShop.next", false);
            String prevMsg = inv.getMessage("inventory.structureShop.previous", false);
            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            List<String> lore = e.getCurrentItem().getItemMeta().getLore();

            if (clicked.equalsIgnoreCase(nextMsg)) {
                if (!Storage.getPages().containsKey(p.getUniqueId())) {
                    Storage.getPages().put(p.getUniqueId(), 1);
                    inv.openStructureShop(p, 1);
                } else {
                    int page = Storage.getPages().get(p.getUniqueId())+1;
                    Storage.getPages().put(p.getUniqueId(), page);
                    inv.openStructureShop(p, page);
                }
            } else if (clicked.equalsIgnoreCase(prevMsg)) {
                if (!Storage.getPages().containsKey(p.getUniqueId())) {
                    Storage.getPages().put(p.getUniqueId(), -1);
                    inv.openStructureShop(p, -1);
                } else {
                    int page = Storage.getPages().get(p.getUniqueId())-1;
                    Storage.getPages().put(p.getUniqueId(), page);
                    inv.openStructureShop(p, page);
                }
            }

            if (e.getCurrentItem().getType() == Material.STRUCTURE_VOID) {
                if (!HiddenStringUtils.hasHiddenString(lore.get(lore.size()-1)))
                    return;

                String itemId = HiddenStringUtils.extractHiddenString(lore.get(lore.size()-1)).split(":")[1];
                double price = structureManager.getPrice(itemId);

                EconomyResponse res = RPGMagic.getEcon().withdrawPlayer(p, price);
                if (!res.transactionSuccess()) {
                    p.sendMessage(Messages.NOT_ENOUGH_MONEY.get().replace("%price%", String.valueOf(price)));
                    p.closeInventory();
                    return;
                }

                ItemStack i = structureManager.getItemById(itemId, false);
                if (i == null) {
                    p.sendMessage(ChatColor.RED + "Failed to get item!");
                    return;
                }

                p.getInventory().addItem(i);
                p.closeInventory();
                ItemBuyEvent ibe = new ItemBuyEvent(p, price, "spell", itemId);
                Bukkit.getServer().getPluginManager().callEvent(ibe);
            }
        } else if (title.equalsIgnoreCase(globalChest)) {
            if (e.getRawSlot() > 53 && e.getAction() != InventoryAction.MOVE_TO_OTHER_INVENTORY)
                return;

            Bukkit.getServer().getScheduler().runTaskLater(RPGMagic.getInstance(), new Runnable() {
                @Override
                public void run() {
                    Storage.getGlobalChestStorage().clear();
                    Storage.getGlobalChestStorage().add(e.getInventory().getContents());
                }
            }, 1);
        } else if (title.equalsIgnoreCase(playerMenuTitle)) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null)
                return;
            if (!e.getCurrentItem().hasItemMeta())
                return;
            if (!e.getCurrentItem().getItemMeta().hasDisplayName())
                return;

            String closeMsg = inv.getMessage("inventory.playerMenu.close", false);
            String spellMsg = inv.getMessage("inventory.playerMenu.structure", false);
            String paperMsg = inv.getMessage("inventory.playerMenu.paper", false);
            String wandMsg = inv.getMessage("inventory.playerMenu.wand", false);
            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());

            if (clicked.equalsIgnoreCase(closeMsg))
                p.closeInventory();
            else if (clicked.equalsIgnoreCase(spellMsg))
                inv.openSpellsMenu(p, 0, true);
            else if (clicked.equalsIgnoreCase(paperMsg))
                inv.openPaperMenu(p, null, 0, true);
            else if (clicked.equalsIgnoreCase(wandMsg))
                inv.openWandMenu(p, 0, true);
        } else if (title.equalsIgnoreCase(playerSpellTitle) || title.equalsIgnoreCase(playerAdminSpellTitle)) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null)
                return;
            if (!e.getCurrentItem().hasItemMeta())
                return;
            if (!e.getCurrentItem().getItemMeta().hasDisplayName())
                return;
            boolean admin = false;
            if (title.equalsIgnoreCase(playerAdminSpellTitle))
                admin = true;

            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            String nextMsg = inv.getMessage("inventory." + title + ".next", false);
            String prevMsg = inv.getMessage("inventory." + title + ".previous", false);

            UUID target = Storage.getAdminMenuHolder().get(p.getUniqueId());
            if (clicked.equalsIgnoreCase(nextMsg)) {
                if (Storage.getPages().containsKey(p.getUniqueId())) {
                    int page = Storage.getPages().get(p.getUniqueId());
                    Storage.getPages().put(p.getUniqueId(), page+1);
                    if (admin)
                        inv.openSpellsAdminMenu(target, p, page, false);
                    else
                        inv.openSpellsMenu(p, page, false);
                } else {
                    Storage.getPages().put(p.getUniqueId(), 1);
                    if (admin)
                        inv.openSpellsAdminMenu(target, p, 1, false);
                    else
                        inv.openSpellsMenu(p, 1, false);
                }
            } else if (clicked.equalsIgnoreCase(prevMsg)) {
                if (Storage.getPages().containsKey(p.getUniqueId())) {
                    int page = Storage.getPages().get(p.getUniqueId());
                    Storage.getPages().put(p.getUniqueId(), page-1);
                    if (admin)
                        inv.openSpellsAdminMenu(target, p, page, false);
                    else
                        inv.openSpellsMenu(p, page, false);
                } else {
                    Storage.getPages().put(p.getUniqueId(), 0);
                    if (admin)
                        inv.openSpellsAdminMenu(target, p, 0, false);
                    else
                        inv.openSpellsMenu(p, 0, false);
                }
            } else if (e.getCurrentItem().getType() == Material.STRUCTURE_VOID) {
                p.getInventory().addItem(e.getCurrentItem());
            }
        } else if (title.equalsIgnoreCase(playerPaperTitle) || title.equalsIgnoreCase(playerPaperAdminTitle)) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null)
                return;
            if (!e.getCurrentItem().hasItemMeta())
                return;
            if (!e.getCurrentItem().getItemMeta().hasDisplayName())
                return;
            boolean admin = false;
            if (title.equalsIgnoreCase(playerPaperAdminTitle))
                admin = true;

            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            String nextMsg = inv.getMessage("inventory.playerPaperMenu.next", false);
            String prevMsg = inv.getMessage("inventory.playerSpellMenu.previous", false);

            String type = Storage.getCategorySelector().get(p.getUniqueId());
            if (clicked.equalsIgnoreCase(nextMsg)) {
                if (Storage.getPages().containsKey(p.getUniqueId())) {
                    int page = Storage.getPages().get(p.getUniqueId());

                    if (admin)
                        inv.openPaperAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, type, page, false);
                    else
                        inv.openPaperMenu(p, type, page, false);
                    Storage.getPages().put(p.getUniqueId(), page+1);
                } else {
                    if (admin)
                        inv.openPaperAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, type, 1, false);
                    else
                        inv.openPaperMenu(p, type, 1, false);
                    Storage.getPages().put(p.getUniqueId(), 1);
                }
            } else if (clicked.equalsIgnoreCase(prevMsg)) {
                if (Storage.getPages().containsKey(p.getUniqueId())) {
                    int page = Storage.getPages().get(p.getUniqueId());

                    if (admin)
                        inv.openPaperAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, type, page, false);
                    else
                        inv.openPaperMenu(p, type, page, false);
                    Storage.getPages().put(p.getUniqueId(), page-1);
                } else {
                    if (admin)
                        inv.openPaperAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, type, 0, false);
                    else
                        inv.openPaperMenu(p, type, 0, true);
                    Storage.getPages().put(p.getUniqueId(), 0);
                }
            } else if (e.getCurrentItem().getType() == Material.PAPER) {
                p.getInventory().addItem(e.getCurrentItem());
            }
        } else if (title.equalsIgnoreCase(playerWandTitle) || title.equalsIgnoreCase(playerWandAdminTitle)) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null)
                return;
            if (!e.getCurrentItem().hasItemMeta())
                return;
            if (!e.getCurrentItem().getItemMeta().hasDisplayName())
                return;
            boolean admin = false;
            if (title.equalsIgnoreCase(playerWandAdminTitle))
                admin = true;

            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            String nextMsg = inv.getMessage("inventory.playerWandTitle.next", false);
            String prevMsg = inv.getMessage("inventory.playerWandTitle.previous", false);

            if (clicked.equalsIgnoreCase(nextMsg)) {
                if (Storage.getPages().containsKey(p.getUniqueId())) {
                    int page = Storage.getPages().get(p.getUniqueId());

                    if (admin)
                        inv.openWandAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, page, false);
                    else
                        inv.openWandMenu(p, page, false);
                    Storage.getPages().put(p.getUniqueId(), page+1);
                } else {
                    if (admin)
                        inv.openWandAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, 1, false);
                    else
                        inv.openWandMenu(p, 1, false);
                    Storage.getPages().put(p.getUniqueId(), 1);
                }
            } else if (clicked.equalsIgnoreCase(prevMsg)) {
                if (Storage.getPages().containsKey(p.getUniqueId())) {
                    int page = Storage.getPages().get(p.getUniqueId());

                    if (admin)
                        inv.openWandAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, page, false);
                    else
                        inv.openWandMenu(p, page, false);
                    Storage.getPages().put(p.getUniqueId(), page-1);
                } else {
                    if (admin)
                        inv.openWandAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, 0, true);
                    else
                        inv.openWandMenu(p, 0, true);
                    Storage.getPages().put(p.getUniqueId(), 0);
                }
            } else if (e.getCurrentItem().getType() != Material.STAINED_GLASS_PANE) {
                p.getInventory().addItem(e.getCurrentItem());
            }
        } else if (title.equalsIgnoreCase(wandShopTitle)) {
            e.setCancelled(true);
            ItemStack i = e.getCurrentItem();
            if (!i.hasItemMeta())
                return;
            if (!i.getItemMeta().hasLore())
                return;

            List<String> lore = i.getItemMeta().getLore();
            int index = i.getItemMeta().getLore().size();
            if (!HiddenStringUtils.hasHiddenString(lore.get(index-1)))
                return;

            double price = Double.parseDouble(HiddenStringUtils.extractHiddenString(lore.get(index-1)).split(":")[1]);
            EconomyResponse res = RPGMagic.getEcon().withdrawPlayer(p, price);
            if (!res.transactionSuccess()) {
                p.sendMessage(Messages.NOT_ENOUGH_MONEY.get().replace("%price%", String.valueOf(price)));
                p.closeInventory();
                return;
            }

            String data = HiddenStringUtils.extractHiddenString(lore.get(index-2));
            ItemMeta im = i.getItemMeta();
            lore.add(HiddenStringUtils.encodeString(data));
            im.setLore(lore);
            i.setItemMeta(im);

            p.getInventory().addItem(i);

            ItemBuyEvent ibe = new ItemBuyEvent(p, price, "wand", data);
            Bukkit.getServer().getPluginManager().callEvent(ibe);
        } else if (title.equalsIgnoreCase(altarItemsShopTitle)) {
            e.setCancelled(true);
            ItemStack i = e.getCurrentItem();
            if (!i.hasItemMeta())
                return;
            if (!i.getItemMeta().hasLore())
                return;

            List<String> lore = i.getItemMeta().getLore();
            int index = i.getItemMeta().getLore().size();
            if (!HiddenStringUtils.hasHiddenString(lore.get(index-1)))
                return;

            double price = Double.parseDouble(HiddenStringUtils.extractHiddenString(lore.get(index-1)).split(":")[1]);
            EconomyResponse res = RPGMagic.getEcon().withdrawPlayer(p, price);
            if (!res.transactionSuccess()) {
                p.sendMessage(Messages.NOT_ENOUGH_MONEY.get().replace("%price%", String.valueOf(price)));
                p.closeInventory();
                return;
            }

            p.getInventory().addItem(i);
        }
        else if (title.equalsIgnoreCase(playerPaperSelectTitle) || title.equalsIgnoreCase(playerAdminPaperSelectTitle)) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null)
                return;
            if (!e.getCurrentItem().hasItemMeta())
                return;
            if (!e.getCurrentItem().getItemMeta().hasDisplayName())
                return;
            boolean admin = false;
            if (title.equalsIgnoreCase(playerAdminPaperSelectTitle))
                admin = true;

            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            String levelMsg = inv.getMessage("inventory.playerPaperSelectMenu.level", false);
            String powerMsg = inv.getMessage("inventory.playerPaperSelectMenu.power", false);
            String areaMsg = inv.getMessage("inventory.playerPaperSelectMenu.effectArea", false);

            if (clicked.equalsIgnoreCase(levelMsg)) {
                if (admin)
                    inv.openPaperAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, "level", 0, true);
                else
                    inv.openPaperMenu(p, "level", 0, true);
                Storage.getCategorySelector().put(p.getUniqueId(), "level");
            } else if (clicked.equalsIgnoreCase(powerMsg)) {
                if (admin)
                    inv.openPaperAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, "power", 0, true);
                else
                    inv.openPaperMenu(p, "power", 0, true);
                Storage.getCategorySelector().put(p.getUniqueId(), "power");
            } else if (clicked.equalsIgnoreCase(areaMsg)) {
                if (admin)
                    inv.openPaperAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, "effect area", 0, true);
                else
                    inv.openPaperMenu(p, "effect area", 0, true);
                Storage.getCategorySelector().put(p.getUniqueId(), "effect area");
            }
        } else if (title.equalsIgnoreCase(npcWandOptionsTitle)) {
            if (e.getCurrentItem() == null)
                return;
            if (!e.getCurrentItem().hasItemMeta())
                return;
            if (!e.getCurrentItem().getItemMeta().hasDisplayName())
                return;
            if (!Storage.getOpenNPCs().containsKey(p.getUniqueId()))
                return;
            NPC npc = Storage.getOpenNPCs().get(p.getUniqueId());

            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            String addWandMsg = inv.getMessage("inventory.npcWandOptions.addWand", false);
            String removeWandMsg = inv.getMessage("inventory.npcWandOptions.removeWand", false);
            String deleteNpcMsg = inv.getMessage("inventory.npcWandOptions.deleteNPC", false);

            if (clicked.equalsIgnoreCase(addWandMsg)) {
                inv.addWandGUI(p);
            } else if (clicked.equalsIgnoreCase(removeWandMsg)) {
                inv.removeWandGUI(p, npc.getId());
            } else if (clicked.equals(deleteNpcMsg)) {
                npc.destroy();
                p.sendMessage(Messages.NPC_DESTROYED.get());
                p.closeInventory();
            }
        } else if (title.equalsIgnoreCase(addWandTitle)) {
            if (e.getCurrentItem() == null)
                return;
            if (!e.getCurrentItem().hasItemMeta())
                return;
            if (!e.getCurrentItem().getItemMeta().hasDisplayName())
                return;

            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            String saveMsg = inv.getMessage("inventory.addWand.save", false);

            if (clicked.equalsIgnoreCase(saveMsg)) {
                e.setCancelled(true);

                ItemStack i = e.getInventory().getItem(13);
                if (i == null)
                    return;
                if (!i.hasItemMeta())
                    return;
                if (!i.getItemMeta().hasDisplayName())
                    return;
                i.setAmount(1);
                String clickedItem = ChatColor.stripColor(i.getItemMeta().getDisplayName());
                if (clickedItem.equalsIgnoreCase(" "))
                    return;

                //input price
                Storage.getItemHolder().put(p.getUniqueId(), i);
                Storage.getInputingWandPrice().add(p.getUniqueId());
                p.sendMessage(Messages.ENTER_A_NUMBER.get());
                p.closeInventory();
            } else if (clicked.equalsIgnoreCase(" ")) {
                e.setCancelled(true);
            }
        } else if (title.equalsIgnoreCase(removeWandTitle)) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null)
                return;
            if (!e.getCurrentItem().hasItemMeta())
                return;
            if (!Storage.getOpenNPCs().containsKey(p.getUniqueId()))
                return;
            NPC npc = Storage.getOpenNPCs().get(p.getUniqueId());

            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());

            if (clicked == null)
                return;
            ItemMeta im = e.getCurrentItem().getItemMeta();

            if (clicked.equalsIgnoreCase("close")) {
                p.closeInventory();
                return;
            }

            if (!HiddenStringUtils.hasHiddenString(im.getLore().get(im.getLore().size()-1)))
                return;

            String id = HiddenStringUtils.extractHiddenString(im.getLore().get(im.getLore().size()-1)).split(":")[1];
            wandManager.deleteWand(Integer.parseInt(id), npc.getId());
            e.getInventory().setItem(e.getRawSlot(), new ItemStack(Material.AIR));
        } else if (title.equalsIgnoreCase(wandItemShopTitle)) {
            e.setCancelled(true);
            ItemStack i = e.getCurrentItem();
            if (!i.hasItemMeta())
                return;
            if (!i.getItemMeta().hasLore())
                return;

            List<String> lore = i.getItemMeta().getLore();
            int index = i.getItemMeta().getLore().size();
            if (!HiddenStringUtils.hasHiddenString(lore.get(index-1)))
                return;

            double price = Double.parseDouble(HiddenStringUtils.extractHiddenString(lore.get(index-1)).split(":")[1]);
            EconomyResponse res = RPGMagic.getEcon().withdrawPlayer(p, price);
            if (!res.transactionSuccess()) {
                p.sendMessage(Messages.NOT_ENOUGH_MONEY.get().replace("%price%", String.valueOf(price)));
                p.closeInventory();
                return;
            }

            p.getInventory().addItem(i);
        } else if (title.equalsIgnoreCase(playerAdminMenuTitle)) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null)
                return;
            if (!e.getCurrentItem().hasItemMeta())
                return;

            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            String spellMsg = inv.getMessage("inventory.playerAdminMenu.structure", false);
            String paperMsg = inv.getMessage("inventory.playerAdminMenu.paper", false);
            String wandMsg = inv.getMessage("inventory.playerAdminMenu.wand", false);

            if (clicked.equalsIgnoreCase(spellMsg))
                inv.openSpellsAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, 0, true);
            else if (clicked.equalsIgnoreCase(paperMsg))
                inv.openPaperAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, null, 0, true);
            else if (clicked.equalsIgnoreCase(wandMsg))
                inv.openWandAdminMenu(Storage.getAdminMenuHolder().get(p.getUniqueId()), p, 0, true);
        }
        else if (title.equalsIgnoreCase(alterTitle)) {
            //Dispenser crafting
            if (!Storage.getOpenInventories().containsKey(p.getUniqueId()))
                return;
            if (e.isShiftClick()) {
                e.setCancelled(true);
                return;
            }

            Bukkit.getServer().getScheduler().runTaskLater(RPGMagic.getInstance(), new Runnable() {
                @Override
                public void run() {
                    Location l = Storage.getOpenInventories().get(p.getUniqueId());
                    Storage.getCrafts().clear();
                    Storage.getCrafts().put(l, e.getInventory().getContents());
                }
            }, 1);

        }
    }
}