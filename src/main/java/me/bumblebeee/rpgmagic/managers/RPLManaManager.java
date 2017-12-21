package me.bumblebeee.rpgmagic.managers;

import me.baks.rpl.Main;
import me.baks.rpl.api.API;
import me.baks.rpl.config.ConfigManager;
import me.baks.rpl.manager.VersionManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RPLManaManager {

    Main rpl = (Main) Bukkit.getServer().getPluginManager().getPlugin("RPGPlayerLeveling");

    public void takeMana(Player p, int mana) {
        setMana(p, getPlayerMana(p)-mana);
    }

    public void setMana(Player p, int mana) {
        if(!rpl.mana.containsKey(p.getName()))
            rpl.mana.put(p.getName(), API.getMaxMana(p));

        rpl.mana.put(p.getName(), mana);
        if(ConfigManager.ACTION_BAR_MANA) {
            VersionManager.sendActionBarMana(p);
        }
    }

    public int getPlayerMana(Player p) {
        if(!rpl.mana.containsKey(p.getName()))
            rpl.mana.put(p.getName(), API.getMaxMana(p));

        return rpl.mana.get(p.getName());
    }

}
