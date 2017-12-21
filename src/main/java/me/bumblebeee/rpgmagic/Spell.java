package me.bumblebeee.rpgmagic;

import lombok.Getter;
import me.bumblebeee.rpgmagic.managers.SpellManager;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class Spell {

    SpellManager spellManager = new SpellManager();

    private @Getter boolean failed = false;
    private @Getter String name;
    private @Getter File file;
    private @Getter int cooldown;
    private @Getter int mana;
    private @Getter String type;
    private @Getter String description;
    private @Getter int variable;

    public Spell(String name, File f) {
        YamlConfiguration c = YamlConfiguration.loadConfiguration(f);
        if (c.getString("name") == null) return;

        this.name = name;
        this.file = f;
        this.cooldown = c.getInt("cooldown");
        this.mana = c.getInt("mana");
        this.type = c.getString("type");
        this.description = c.getString("description");
        this.variable = c.getInt("variable");
    }

    public Spell(String name) {
        File f = spellManager.getSpellFile(name);
        if (f == null) {
            failed = true;
            return;
        }

        YamlConfiguration c = YamlConfiguration.loadConfiguration(f);
        if (c.getString("name") == null) return;
        this.name = name;
        this.file = f;
        this.cooldown = c.getInt("cooldown");
        this.mana = c.getInt("mana");
        this.type = c.getString("type");
        this.description = c.getString("description");
        this.variable = c.getInt("variable");
    }
}
