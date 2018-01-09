package me.bumblebeee.rpgmagic;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import lombok.Getter;
import me.bumblebeee.rpgmagic.commands.MagicCommand;
import me.bumblebeee.rpgmagic.listeners.*;
import me.bumblebeee.rpgmagic.listeners.inventoryClicks.*;
import me.bumblebeee.rpgmagic.managers.Messages;
import me.bumblebeee.rpgmagic.managers.Runnables;
import me.bumblebeee.rpgmagic.managers.SpellManager;
import me.bumblebeee.rpgmagic.utils.Storage;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RPGMagic extends JavaPlugin implements Listener {

    private static @Getter List<Player> seeGlow = new ArrayList<>();

    private static @Getter Plugin instance;
    private static @Getter Economy econ = null;

    Storage storage = new Storage();
    SpellManager spellManager = new SpellManager();

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
        setupFiles();
        spellManager.cacheSpellFiles();
        storage.loadStorageChest();

        Bukkit.getServer().getPluginCommand("magic").setExecutor(new MagicCommand());
        Runnables.walk();

        /* Glow effect */
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, PacketType.Play.Server.ENTITY_METADATA, PacketType.Play.Server.NAMED_ENTITY_SPAWN) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (seeGlow.contains(event.getPlayer())) {
                    Player p = event.getPlayer();
                    for (Entity e : p.getWorld().getNearbyEntities(p.getLocation(), 20, 20, 20)) {
                        if (e.getEntityId() == event.getPacket().getIntegers().read(0)) {
                            if (event.getPacketType() == PacketType.Play.Server.ENTITY_METADATA) {
                                List<WrappedWatchableObject> watchableObjectList = event.getPacket().getWatchableCollectionModifier().read(0);
                                for (WrappedWatchableObject metadata : watchableObjectList) {
                                    if (metadata.getIndex() == 0) {
                                        byte b = (byte) metadata.getValue();
                                        b |= 0b01000000;
                                        metadata.setValue(b);
                                    }
                                }
                            } else {
                                WrappedDataWatcher watcher = event.getPacket().getDataWatcherModifier().read(0);
                                if (watcher.hasIndex(0)) {
                                    byte b = watcher.getByte(0);
                                    b |= 0b01000000;
                                    watcher.setObject(0, b);
                                }
                            }
                        }
                    }
                }
            }
        });
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
        Bukkit.getServer().getPluginManager().registerEvents(new SpellCast(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerMove(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerDamage(), this);
        Bukkit.getServer().getPluginManager().registerEvents(this, this);

        //Inventory Click Events
        Bukkit.getServer().getPluginManager().registerEvents(new Admin(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new Spells(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new Papers(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ShopAlterOther(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new Wands(), this);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e)
    {
        if(e.getPlayer().getInventory().getItemInMainHand().getType() == Material.SPECTRAL_ARROW)
            if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
            {
                e.getPlayer().sendMessage("Click accepted!");
                if (seeGlow.contains(e.getPlayer())) {
                    seeGlow.remove(e.getPlayer());
                    return;
                }
                seeGlow.add(e.getPlayer());
                for (Player p : Bukkit.getOnlinePlayers())
                {
                    if(p != e.getPlayer())
                    {
                        e.getPlayer().hidePlayer(p);
                        e.getPlayer().showPlayer(p);
                    }
                }
            }
    }

    public void setupFiles() {
        spellManager.saveDefaultSpells();
        if (!new File(getDataFolder() + File.separator + "papers.yml").exists())
            saveResource("papers.yml", false);
        if (!new File(getDataFolder() + File.separator + "inventories.yml").exists())
            saveResource("inventories.yml", false);
        if (!new File(getDataFolder() + File.separator + "shops.yml").exists())
            saveResource("shops.yml", false);
        Messages.getYaml();
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
