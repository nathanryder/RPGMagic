package me.bumblebeee.rpgmagic.managers;

import me.bumblebeee.rpgmagic.RPGMagic;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public enum Messages {

    INVALID_ARGS("invalidArguments"),
    NO_PERMISSIONS("noPermissions"),
    NPC_DESTROYED("npcRemoved"),
    NOT_ENOUGH_MONEY("notEnoughMoney"),
    NOT_A_NUMBER("notANumber"),
    ADDED_PAPER("addedPaper"),
    CATEGORY_NOT_FOUND("categoryNotFound"),
    TYPE_NOT_FOUND("typeNotFound"),
    SHAPE_DOES_NOT_EXIST("shapeNotFound"),
    ITEM_GIVEN("itemGiven"),
    SPELL_NOT_FOUND("spellNotFound"),
    ENTER_A_NUMBER("enterANumber"),
    SPELL_ON_COOLDOWN("spellOnCooldown"),
    NOT_ENOUGH_MANA("notEnoughMana"),
    ERROR_WITH_SCHEMATIC("errorWithSchematic"),
    PASTED_SCHEMATIC("pastedSchematic"),
    NOT_HOLDING_WAND("notHoldingWand"),
    ENTER_A_PLAYER_NAME("enterPlayerName"),
    HELP_LINE("helpCommand"),
    RELOAD_SUCCESS("reloadSuccess");

    String key;
    Messages(String key) {
        this.key = key;
    }

    public String get() {
        YamlConfiguration c = getYaml();
        if (c.getString(key) == null)
            return ChatColor.translateAlternateColorCodes('&', "&cFailed to find a message " + key + "! Please report this to an administrator");
        return ChatColor.translateAlternateColorCodes('&', c.getString(key));
    }

    public static YamlConfiguration getYaml() {
        File f = new File(RPGMagic.getInstance().getDataFolder() + File.separator + "messages.yml");
        YamlConfiguration c = null;
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            c = YamlConfiguration.loadConfiguration(f);
            c.set("invalidArguments", "&cInvalid arguments! Correct usage: %usage%");
            c.set("noPermissions", "&cYou do not have the required permissions!");
            c.set("reloadSuccess", "&aSuccessfully reloaded config!");
            c.set("npcRemoved", "&aSuccessfully removed NPC");
            c.set("notEnoughMoney", "&cYou need &e%price% &cor more to buy this!");
            c.set("notANumber", "&c%number% is not a number!");
            c.set("addedPaper", "&aSuccessfully added paper!");
            c.set("categoryNotFound", "&cFailed to find category called %type%");
            c.set("typeNotFound", "&cFailed to find paper type called %type%");
            c.set("shapeNotFound", "&cFailed to find a shape named %shape%");
            c.set("itemGiven", "&aYou have been given an item!");
            c.set("spellNotFound", "&cFailed to find a spell named %spell%");
            c.set("playerNotFound", "&cFailed to find a a player named %name%");
            c.set("inputLevel", "&aEnter the level");
            c.set("inputPower", "&aEnter the power");
            c.set("inputAreaInfo", "&aEnter the shape and area (e.g cono 4)");
            c.set("invalidFormatting", "&cInvalid formatting!");
            c.set("enterANumber", "&aEnter a number:");
            c.set("spellOnCooldown", "&cYou must wait &e%time% &cmore seconds!");
            c.set("notEnoughMana", "&cYou do not have enough mana!");
            c.set("errorWithSchematic", "&cFailed to %operation% schematic%reason%");
            c.set("pastedSchematic", "&aSuccessfully pasted schematic!");
            c.set("notHoldingWand", "&aYou must be holding a wand!");
            c.set("enterPlayerName", "&aEnter a player name to search:");
            c.set("helpCommand", "&6%command%&f: %description%");

            try {
                c.save(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return c == null ? YamlConfiguration.loadConfiguration(f) : c;
    }
}
