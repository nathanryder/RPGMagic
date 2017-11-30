package me.bumblebeee.rpgmagic;

import lombok.Getter;
import me.bumblebeee.rpgmagic.commands.MagicCommand;
import me.bumblebeee.rpgmagic.listeners.*;
import me.bumblebeee.rpgmagic.listeners.inventoryClicks.*;
import me.bumblebeee.rpgmagic.managers.Messages;
import me.bumblebeee.rpgmagic.spells.Lightning;
import me.bumblebeee.rpgmagic.spells.Speed;
import me.bumblebeee.rpgmagic.utils.Storage;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class RPGMagic extends JavaPlugin {

    private static @Getter Plugin instance;
    private static @Getter Economy econ = null;

    Storage storage = new Storage();

    @Override
    public void onEnable() {
        instance = this;
        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        saveDefaultConfig();
        registerEvents();
        registerSpells();
        storage.loadStorageChest();

        saveResource("papers.yml", false);
        saveResource("inventories.yml", false);
        saveResource("spells.yml", false);
        saveResource("shops.yml", false);
        Messages.getYaml();
        Bukkit.getServer().getPluginCommand("magic").setExecutor(new MagicCommand());
    }

    @Override
    public void onDisable() {
        storage.saveStorageChest();
    }

    public void registerEvents() {
        Bukkit.getServer().getPluginManager().registerEvents(new NPCClick(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new BlockPlace(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new InventoryClose(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new AsyncPlayerChat(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new AlterCraft(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ItemBuy(), this);

        //Inventory Click Events
        Bukkit.getServer().getPluginManager().registerEvents(new Admin(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new DualFunctionClicks(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new Papers(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ShopAlterOther(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new Wands(), this);
    }

    public void registerSpells() {
        Bukkit.getServer().getPluginManager().registerEvents(new Lightning(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new Speed(), this);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}
