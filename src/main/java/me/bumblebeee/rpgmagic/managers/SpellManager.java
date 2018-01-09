package me.bumblebeee.rpgmagic.managers;

import lombok.Getter;
import me.bumblebeee.rpgmagic.RPGMagic;
import me.bumblebeee.rpgmagic.Spell;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SpellManager {

    private static @Getter Map<String, Spell> spells = new HashMap<>();

    public static Spell getSpell(String spell) {
        return spells.get(spell.toLowerCase());
    }

    public void saveDefaultSpells() {
        //TODO check if default spells exists
        saveDefaultSpell("speed.yml");
        saveDefaultSpell("teleport.yml");
        saveDefaultSpell("missile.yml");
        saveDefaultSpell("blackhole.yml");
        saveDefaultSpell("fling.yml");
        saveDefaultSpell("shield.yml");
        saveDefaultSpell("peek.yml");
        saveDefaultSpell("zeus.yml");
        saveDefaultSpell("freeze.yml");
        saveDefaultSpell("wallhack.yml");
    }

    public void saveDefaultSpell(String spellFile) {
        File f = new File(RPGMagic.getInstance().getDataFolder() + File.separator + "spells" + File.separator + spellFile);
        if (f.exists())
            return;

        RPGMagic.getInstance().saveResource("spells/" + spellFile, false);
    }

    public void cacheSpellFiles() {
        File folder = new File(RPGMagic.getInstance().getDataFolder() + File.separator + "spells");
        File[] files = folder.listFiles();
        if (files == null)
            return;

        for (File f : files) {
            YamlConfiguration c = YamlConfiguration.loadConfiguration(f);
            String name = c.getString("name");
            if (name == null)
                continue;

            spells.put(name.toLowerCase(), new Spell(name, f));
        }
    }

    public boolean spellExists(String spell) {
        return spells.containsKey(spell.toLowerCase());
    }

    public int getAmountOfSpells() {
        File folder = new File(RPGMagic.getInstance().getDataFolder() + File.separator + "spells");
        File[] files = folder.listFiles();
        if (files == null)
            return 0;

        int amount = 0;
        for (File f : files)
            amount++;

        return amount;
    }

    public File getSpellFile(String name) {
        File folder = new File(RPGMagic.getInstance().getDataFolder() + File.separator + "spells");
        File[] files = folder.listFiles();
        if (files == null)
            return null;

        for (File f : files) {
            YamlConfiguration c = YamlConfiguration.loadConfiguration(f);
            String spellName = c.getString("name");
            if (spellName == null)
                continue;
            if (name.equalsIgnoreCase(spellName))
                return f;
        }

        return null;
    }
}
