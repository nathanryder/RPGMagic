package me.bumblebeee.rpgmagic.listeners.inventoryClicks;

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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ShopAlterOther implements Listener {

        InventoryManager inv = new InventoryManager();
        NPCManager npcManager = new NPCManager();
        PaperManager paperManager = new PaperManager();
        StructureManager structureManager = new StructureManager();

        @EventHandler
        public void onInventoryClick(InventoryClickEvent e) {
            Player p = (Player) e.getWhoClicked();

            String npcSetupTitle = inv.getMessage("inventory.npcSetupTypeSelection.title", false);
            String globalChest = inv.getMessage("inventory.globalChest.title", false);
            String playerMenuTitle = inv.getMessage("inventory.playerMenu.title", false);
            String alterTitle = RPGMagic.getInstance().getConfig().getString("alterName");
            String altarItemsShopTitle = inv.getMessage("inventory.altarItemsShop.title", false);
            String spellShop = inv.getMessage("inventory.structureShop.title", false);
            String paperShop = inv.getMessage("inventory.paperShop.title", false);
            String wandItemShopTitle = inv.getMessage("inventory.wandItemsShop.title", false);
            String wandShopTitle = inv.getMessage("inventory.wandShop.title", false);

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

                    ItemStack i = structureManager.getItemByName(itemId, false);
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
            } else if (title.equalsIgnoreCase(alterTitle)) {
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
            }
        }
}
