package me.bumblebeee.rpgmagic.managers;

import me.baks.rpl.Main;
import me.baks.rpl.actionbar.ActionBar;
import me.baks.rpl.actionbar.ActionBarFormat;
import me.baks.rpl.api.API;
import me.baks.rpl.config.ConfigManager;
import me.baks.rpl.manager.ManaManager;
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
            rpl.mana.put(p.getName(), getMaxMana(p));

        rpl.mana.put(p.getName(), mana);
        if(ConfigManager.ACTION_BAR_MANA) {
            new ActionBar(p, ActionBarFormat.MANA);
        }
    }

    public int getPlayerMana(Player p) {
        if(!rpl.mana.containsKey(p.getName()))
            rpl.mana.put(p.getName(), getMaxMana(p));

        return rpl.mana.get(p.getName());
    }

    public int getMaxMana(Player var1) {
        int var2 = ManaManager.getMaxMana(var1.getName());
        return var2;
    }

}
