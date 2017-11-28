package me.bumblebeee.rpgmagic.listeners;

import me.bumblebeee.rpgmagic.RPGMagic;
import me.bumblebeee.rpgmagic.managers.InventoryManager;
import me.bumblebeee.rpgmagic.utils.Storage;
import net.citizensnpcs.api.event.NPCClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NPCClick implements Listener {

    InventoryManager inv = new InventoryManager();

    @EventHandler
    public void onNPCClick(NPCRightClickEvent e) {
        Player p = e.getClicker();
        NPC npc = e.getNPC();

        String paperName = RPGMagic.getInstance().getConfig().getString("npcs.paper.name");
        String wandName = RPGMagic.getInstance().getConfig().getString("npcs.wand.name");
        String structureName = RPGMagic.getInstance().getConfig().getString("npcs.structure.name");
        String alterName = RPGMagic.getInstance().getConfig().getString("npcs.altar.name");
        String wandItemsName = RPGMagic.getInstance().getConfig().getString("npcs.wanditems.name");


        if (npc.getName().equalsIgnoreCase("Setup Required")) {
            inv.openNPCSetup(p);
            Storage.getOpenNPCs().put(p.getUniqueId(), npc);
        } else if (npc.getName().equalsIgnoreCase(paperName)) {
            if (p.isSneaking()) {
                if (p.hasPermission("rpgmagic.npcsetup"))
                    inv.openPaperSetup(p, npc, 1);
                else
                    inv.openPaper(p);
            } else {
                inv.openPaper(p);
            }
            Storage.getOpenNPCs().put(p.getUniqueId(), npc);
        } else if (npc.getName().equalsIgnoreCase(structureName)) {
            inv.openStructureShop(p, 0);
            Storage.getOpenNPCs().put(p.getUniqueId(), npc);
        } else if (npc.getName().equalsIgnoreCase(wandName)) {
            if (p.isSneaking() && p.hasPermission("rpgmagic.wandsetup")) {
                inv.openWandSetup(p, npc, 1);
            } else {
                inv.openWandShop(p, npc.getId(), 0);
            }
            Storage.getOpenNPCs().put(p.getUniqueId(), npc);
        } else if (npc.getName().equalsIgnoreCase(alterName)) {
            inv.openAltarItemsShop(p, 0);
            Storage.getOpenNPCs().put(p.getUniqueId(), npc);
        } else if (npc.getName().equalsIgnoreCase(wandItemsName)) {
            inv.openWandItemsShop(p, 0);
            Storage.getOpenNPCs().put(p.getUniqueId(), npc);
        }
    }

}