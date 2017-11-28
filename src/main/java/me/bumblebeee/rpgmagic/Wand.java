package me.bumblebeee.rpgmagic;

import lombok.Getter;
import me.bumblebeee.rpgmagic.managers.StructureManager;
import me.bumblebeee.rpgmagic.utils.HiddenStringUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Wand {

    StructureManager spells = new StructureManager();

    private @Getter ItemStack item;
    private @Getter int power;
    private @Getter int level;
    private @Getter int distance;
    private @Getter String shape;
    private @Getter String spell;
    private @Getter int manaRequired;

    public Wand(ItemStack i) {
        List<String> lore = i.getItemMeta().getLore();
        int index = lore.size()-1;
        if (!HiddenStringUtils.hasHiddenString(lore.get(index)))
            return;
        String[] data = HiddenStringUtils.extractHiddenString(lore.get(index)).split(":");

        this.item = i;
        this.power = Integer.parseInt(data[0]);
        this.level = Integer.parseInt(data[1]);
        this.shape = data[2];
        this.distance = Integer.parseInt(data[3]);
        this.spell = data[4];
        this.manaRequired = getManaRequired();
    }

    public int getManaRequired() {
        YamlConfiguration c = spells.getFile();

        return c.getInt("spells." + spell + ".mana");
    }

}
