package me.bumblebeee.rpgmagic.listeners.inventoryClicks;

import me.bumblebeee.rpgmagic.managers.InventoryManager;
import me.bumblebeee.rpgmagic.managers.Messages;
import me.bumblebeee.rpgmagic.managers.PaperManager;
import me.bumblebeee.rpgmagic.managers.WandManager;
import me.bumblebeee.rpgmagic.utils.HiddenStringUtils;
import me.bumblebeee.rpgmagic.utils.Storage;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Papers implements Listener {

    InventoryManager inv = new InventoryManager();
    PaperManager paperManager = new PaperManager();
    WandManager wandManager = new WandManager();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        String paperOptions = inv.getMessage("inventory.npcPaperOptions.title", false);
        String addPaperTitle = inv.getMessage("inventory.addPaper.title", false);
        String removePaperTitle = inv.getMessage("inventory.removePaper.title", false);
        String npcWandOptionsTitle = inv.getMessage("inventory.npcWandOptions.title", false);
        String paperSelectTitle = inv.getMessage("inventory.selectPaperType.title", false);
        String playerPaperTitle = inv.getMessage("inventory.playerPaperMenu.title", false);
        String playerPaperSelectTitle = inv.getMessage("inventory.playerPaperSelectMenu.title", false);

        String title = e.getInventory().getTitle();

        if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta()
                && e.getCurrentItem().getItemMeta().hasDisplayName()) {
            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            if (clicked.equalsIgnoreCase("close"))
                p.closeInventory();
        }

        if (title.equalsIgnoreCase(paperOptions)) {

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

                String[] data = HiddenStringUtils.extractHiddenString(i.getItemMeta().getLore().get(i.getItemMeta().getLore().size()-1)).split(":");
                double lvlPwrDist;
                if (data[0].equalsIgnoreCase("shape")) {
                    lvlPwrDist = Double.valueOf(data[2]);
                    String shape = data[1].split("\\|")[0];
                    Storage.getEAHolder().put(p.getUniqueId(), shape);
                } else {
                    lvlPwrDist = Double.valueOf(data[1]);
                }

                //input price
                Storage.getItemHolder().put(p.getUniqueId(), i);
                Storage.getLvlPwrDistHolder().put(p.getUniqueId(), lvlPwrDist);
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
        } else if (title.equalsIgnoreCase(playerPaperTitle)) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null)
                return;
            if (!e.getCurrentItem().hasItemMeta())
                return;
            if (!e.getCurrentItem().getItemMeta().hasDisplayName())
                return;

            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            String nextMsg = inv.getMessage("inventory.playerPaperMenu.next", false);
            String prevMsg = inv.getMessage("inventory.playerSpellMenu.previous", false);

            String type = Storage.getCategorySelector().get(p.getUniqueId());
            if (clicked.equalsIgnoreCase(nextMsg)) {
                if (Storage.getPages().containsKey(p.getUniqueId())) {
                    int page = Storage.getPages().get(p.getUniqueId());
                    inv.openPaperMenu(p, type, page, false);
                    Storage.getPages().put(p.getUniqueId(), page+1);
                } else {
                    inv.openPaperMenu(p, type, 1, false);
                    Storage.getPages().put(p.getUniqueId(), 1);
                }
            } else if (clicked.equalsIgnoreCase(prevMsg)) {
                if (Storage.getPages().containsKey(p.getUniqueId())) {
                    int page = Storage.getPages().get(p.getUniqueId());
                    inv.openPaperMenu(p, type, page, false);
                    Storage.getPages().put(p.getUniqueId(), page-1);
                } else {
                    inv.openPaperMenu(p, type, 0, true);
                    Storage.getPages().put(p.getUniqueId(), 0);
                }
            } else if (e.getCurrentItem().getType() == Material.PAPER) {
                p.getInventory().addItem(e.getCurrentItem());
            }
        } else if (title.equalsIgnoreCase(playerPaperSelectTitle)) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null)
                return;
            if (!e.getCurrentItem().hasItemMeta())
                return;
            if (!e.getCurrentItem().getItemMeta().hasDisplayName())
                return;

            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            String levelMsg = inv.getMessage("inventory.playerPaperSelectMenu.level", false);
            String powerMsg = inv.getMessage("inventory.playerPaperSelectMenu.power", false);
            String areaMsg = inv.getMessage("inventory.playerPaperSelectMenu.effectArea", false);

            if (clicked.equalsIgnoreCase(levelMsg)) {
                inv.openPaperMenu(p, "level", 0, true);
                Storage.getCategorySelector().put(p.getUniqueId(), "level");
            } else if (clicked.equalsIgnoreCase(powerMsg)) {
                inv.openPaperMenu(p, "power", 0, true);
                Storage.getCategorySelector().put(p.getUniqueId(), "power");
            } else if (clicked.equalsIgnoreCase(areaMsg)) {
                inv.openPaperMenu(p, "effect area", 0, true);
                Storage.getCategorySelector().put(p.getUniqueId(), "effect area");
            }
        }
    }
}
