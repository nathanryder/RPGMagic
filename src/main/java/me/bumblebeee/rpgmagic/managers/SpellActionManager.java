package me.bumblebeee.rpgmagic.managers;

import lombok.Getter;
import me.bumblebeee.rpgmagic.RPGMagic;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SpellActionManager {

    private @Getter List<UUID> actionsRunning = new ArrayList<>();

    public SpellActionManager() {
    }

    public void addRunning(UUID uuid) {
        actionsRunning.add(uuid);
    }

    public void removeRunning(UUID uuid) {
        actionsRunning.remove(uuid);
    }

    public void manageAction(Player p, String action) {
        String[] data = action.split(":");
        String function = data[0];
        String[] args = new String[100];

        for (int i = 1; i < data.length; i++)
            args[i - 1] = data[i];

        UUID uuid = UUID.randomUUID();
        addRunning(uuid);

        if (function.equalsIgnoreCase("loop")) {
            int time = Integer.parseInt(data[data.length-2]);
            int iterations = Integer.parseInt(data[data.length-1]);

            new BukkitRunnable() {
                int count = 0;
                @Override
                public void run() {
                    StringBuilder ac = new StringBuilder();
                    for (String s : args)
                        ac.append(s).append(":");

                    manageAction(p, ac.toString());

                    count++;
                    if (count >= iterations) {
                        removeRunning(uuid);
                        this.cancel();
                    }
                }
            }.runTaskTimer(RPGMagic.getInstance(), 0, time);
        } else if (function.equalsIgnoreCase("applyPotion")) {
            applyPotion(p, args);
            removeRunning(uuid);
        }
    }

    public void applyPotion(Player p, String[] args) {
        String potionName = args[0];
        int boost = Integer.parseInt(args[1])-1;
        int duration = Integer.parseInt(args[2])*20;
        PotionEffectType effect = PotionEffectType.getByName(potionName);
        if (effect == null) {
            p.sendMessage(ChatColor.RED + "Invalid potion effect " + potionName);
            return;
        }

        PotionEffect potion = new PotionEffect(effect, duration, boost, false);
        p.addPotionEffect(potion);
    }

}
