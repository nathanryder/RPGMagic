package me.bumblebeee.rpgmagic.managers;

import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Cooldown {

    StructureManager spells = new StructureManager();

    private static @Getter Map<UUID, Map<String, Integer>> cooldowns = new HashMap<>();

    public void giveCooldown(UUID uuid, String spell) {
        int cooldown = getSpellCooldown(spell);

        Map<String, Integer> spells = cooldowns.get(uuid);
        if (spells == null)
            spells = new HashMap<>();
        spells.put(spell, cooldown);
        cooldowns.put(uuid, spells);
    }

    public void reduceCooldown(UUID uuid, String spell, int amount) {
        int cooldown = getCooldown(uuid, spell)-1;

        Map<String, Integer> spells = cooldowns.get(uuid);
        spells.put(spell, cooldown);
        cooldowns.put(uuid, spells);
    }

    public int getCooldown(UUID uuid, String spell) {
        if (cooldowns.get(uuid) == null)
            return 0;
        if (cooldowns.get(uuid).get(spell) == null)
            return 0;
        return cooldowns.get(uuid).get(spell);
    }

    public int getSpellCooldown(String spell) {
        YamlConfiguration c = spells.getFile();
        return c.getInt("spells." + spell + ".cooldown");
    }

}
