package me.bumblebeee.rpgmagic.managers;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Cooldown {

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
        int cooldown = getCooldown(uuid, spell)-amount;

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
        return SpellManager.getSpell(spell).getCooldown();
    }

}
